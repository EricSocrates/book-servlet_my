package com.test.book.books.servlet;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.test.book.books.entity.Bookpic;
import com.test.book.books.service.BookService;
import com.test.book.books.service.BookpicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.UUID;

@WebServlet(name = "BookpicServlet", urlPatterns = "/bookpic")
public class BookpicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //预设为传json数据
        response.setContentType("text/plain;charset=UTF-8");
        String method = request.getParameter("method");
        //使用multipart/form-data后，使用k-v传值，不在
        if (null == method) {
            this.addBookpic(request, response);
            return;
        }
        try {
            //寻找名字是method 参数是Request和Response的方法
            Method m = this.getClass().getDeclaredMethod(method, HttpServletRequest.class, HttpServletResponse.class);
            //执行方法名为method字符串内容的方法
            m.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加书籍图片（异步）
    private void addBookpic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //SmartUpload文件上传
        SmartUpload su = new SmartUpload();
        su.initialize(this, request, response);
        try {
            su.upload();
            Request req = su.getRequest();
            String bookid = req.getParameter("bookid");
            BookService bs = new BookService();
            BookpicService bps = new BookpicService();
            Files fs = su.getFiles();
            int count = fs.getCount();

            for (int i = 0; i < count; i++) {
                File f = fs.getFile(i);
                String showName = f.getFileName();
                String fExt = f.getFileExt();
                String newName = UUID.randomUUID().toString().replaceAll("-", "");
                f.saveAs("/bookpic/" + newName);

                Bookpic bp = new Bookpic();
                bp.setShowname(showName);
                bp.setSavepath("/bookpic/" + newName);
                bp.setBookid(bookid);
                bp.setIscover(0);
                bps.addBookpic(bp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //异步上传
        PrintWriter out = response.getWriter();
        out.print("{\"flag\":\"success\"}");
        out.close();
    }

    //设置书籍封面（异步）
    private void setCover(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bid = request.getParameter("bid");
        String bpid = request.getParameter("bpid");
        BookpicService bps = new BookpicService();
        //先将所有bid的封面图片修改为0
        bps.updateBookSetCover0(bid);
        int affectedRow = bps.updateBookSetCover1(bpid);
        PrintWriter out = response.getWriter();
        //手写json
        out.print("{\"flag\":\"" + (affectedRow == 1 ? "success" : "fail") + "\"}");
        out.close();
    }

    //删除书籍图片（异步）
    private void deleteBookpic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BookpicService bps = new BookpicService();
        String id = request.getParameter("id");
        Bookpic bp = bps.queryBookpicById(id);
        String savepath = bp.getSavepath();
        String fPath = this.getServletContext().getRealPath(savepath);
        PrintWriter out = response.getWriter();
        if (fPath != null) {
            java.io.File f = new java.io.File(fPath);
            if (f.exists()) f.delete();
        }
        int affectedRows = bps.deleteBookpicById(id);
        out.println("{\"flag\":\"" + (affectedRows == 1 ? "success" : "fail") + "\"}");
        out.close();
    }
}
