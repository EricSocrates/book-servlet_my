package com.test.book.books.servlet;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;
import com.test.book.books.entity.Book;
import com.test.book.books.entity.Bookpic;
import com.test.book.books.entity.Cart;
import com.test.book.books.entity.Category;
import com.test.book.books.service.BookService;
import com.test.book.books.service.BookpicService;
import com.test.book.books.vo.BookVo;
import com.test.book.commons.page.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@WebServlet(name = "BookServlet", urlPatterns = "/book")
public class BookServlet extends HttpServlet {
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
            this.addBook(request, response);
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

    //查询类别有没有重名（异步，返回JSON）
    private void checkCategoryName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        BookService bs = new BookService();
        String result = bs.checkCategoryName(name);
        PrintWriter out = response.getWriter();
        JSONObject jo = new JSONObject();
        jo.put("flag", result);
        out.println(jo);
        out.close();
    }

    //添加类别（同步，处理请求后跳转）
    private void addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        BookService bs = new BookService();
        Category c = new Category(null, name);
        //在doPost中设置的text/plain用于传递JSON这样的文本信息，为了让响应传递HTML标签，需要更改响应格式
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<script type='text/javascript'>alert('" + (("success".equals(bs.addCategory(c))) ? "添加类别成功" : "添加类别失败") + "');document.location.href = 'addcategory.jsp';</script>");
        out.close();
    }

    //查询所有类别（异步）
    private void queryAllCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bs = new BookService();
        List<Category> categoryList = bs.queryAllCategories();
        PrintWriter out = response.getWriter();
        JSONArray jo = JSONArray.fromObject(categoryList);
        out.print(jo);
        out.close();
    }

    //新增书籍（同步上传）
    private void addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SmartUpload su = new SmartUpload();
        su.initialize(this, request, response);
        try {
            su.upload();
            //1.封装除图片外其他参数
            Request req = su.getRequest();
            String name = req.getParameter("name");
            String author = req.getParameter("author");
            Double price = Double.parseDouble(req.getParameter("price"));
            String pdate_pre = req.getParameter("publishdate");
            //将字符串类型的日期格式化为Date类型
            Date d = DateUtils.parseDate(pdate_pre, new String[]{"yyyy-MM-dd"});
            //将时间转换为数据库中需要的类型
            Timestamp pdate = new Timestamp(d.getTime());
            Integer quantity = Integer.parseInt(req.getParameter("quantity"));
            Integer status = Integer.parseInt(req.getParameter("status"));
            String caid = req.getParameter("caid");
            Book b = new Book(null, name, author, price, pdate, caid, status, quantity, 0);
            //保存book对象到数据库
            BookService bs = new BookService();
            BookpicService bps = new BookpicService();
            //保存对象到数据库并返回生成的bookid供存图片时关联使用
            String bookid = bs.addBook(b);
            //2.封装文件参数
            Files fs = su.getFiles();
            //获得上传的文件个数
            Integer count = fs.getCount();
            Integer isCover;
            //获取哪一个图被选作封面
            if (req.getParameter("cover") != null) isCover = Integer.parseInt(req.getParameter("cover"));
            else isCover = -1;
            //逐个获取、设置文件信息并将图片保存至数据库
            for (int i = 0; i < count; i++) {
                //获取单个文件的信息
                File f = fs.getFile(i);
                if (f != null) {
                    //获取文件名
                    String showName = f.getFileName();
                    //获取文件后缀
                    String fExt = f.getFileExt();
                    //定义存在数据库中的新名字，防止图片重名
                    String newName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fExt;

                    //保存到服务器指定目录
                    f.saveAs("/bookpic/" + newName);

                    //把文件路径保存到数据库中
                    Bookpic bp = new Bookpic();
                    bp.setShowname(showName);
                    bp.setSavepath("/bookpic/" + newName);
                    bp.setBookid(bookid);

                    //设置是否为封面
                    if (isCover == i) bp.setIscover(1);
                    else bp.setIscover(0);

                    bps.addBookpic(bp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //先把预先设定好的返回json转为html格式返回
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect("booklist.jsp");
    }

    //查询图书列表（异步）
    private void queryBookSeparated(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bs = new BookService();

        //接收当前页码和一页条数
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        //set到Page实体中
        Page<Book> page = new Page<>();
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);

        //将查到的数据放在集合中
        List<Book> bookList = new ArrayList<>();
        String nameKey = request.getParameter("nameKey");
        String authorKey = request.getParameter("authorKey");
        String caidKey = request.getParameter("caidKey");
        String queryWay = request.getParameter("queryWay");

        BookVo bv = new BookVo(nameKey, caidKey, authorKey);
        page.setTotal(bs.countBook(page.getStart(), page.getPageSize(), bv, queryWay));
        bookList = bs.queryBookSeparated(page.getStart(), page.getPageSize(), bv, queryWay);
        page.setRows(bookList);
        PrintWriter out = response.getWriter();
        JSONObject jo = JSONObject.fromObject(page);
        out.print(jo);
        out.close();
    }

    //根据图书id查询图书（异步）
    private void queryBookById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bs = new BookService();
        String id = request.getParameter("id");
        Book book = bs.queryBookById(id);
        JSONObject jo = JSONObject.fromObject(book);
        PrintWriter out = response.getWriter();
        out.print(jo);
        out.close();
    }

    //将浏览信息加入cookie（异步）
    private void addBookCookie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bs = new BookService();
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        //将查询信息放入cookie
        //getData用来将本次的id组装到原有的Cookie中 request存有之前浏览器中存放的cookie信息
        Cookie c = new Cookie("books", getData(id, request));
        //设置cookie留存时长
        c.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(c);
        out.print("{\"msg\":\"success\"}");
        out.close();
    }

    //处理cookie中的数据
    private String getData(String id, HttpServletRequest request) {
        Cookie[] cs = request.getCookies();
        if (cs != null) {
            for (Cookie c : cs) {
                String cName = c.getName();
                //名为books的cookie
                if ("books".equals(cName)) {
                    String cValue = c.getValue();
                    String[] ss = cValue.split("#");
                    //最多显示四条数据 如果是重复数据 将老数据提前 如果是新数据 添加之前如果已经够四个 那就把最后一个删掉 新数据放在第一个位置
                    //为了方便处理 将数组转换为集合
                    LinkedList<String> lList = new LinkedList<>(Arrays.asList(ss));
                    //cookie中不够四条数据的情况
                    if (lList.size() < 4) {
                        //cookie中不够四条 新的一条与原来数据有重复 删除旧数据 将新数据填到开头
                        if (lList.contains(id)) {
                            lList.remove(id);
                            lList.addFirst(id);
                        }
                        //cookie中不够四条 新的一条与原来数据没有重复 直接加到开头
                        else {
                            lList.addFirst(id);
                        }
                    }
                    //cookie数据已经够四条
                    else {
                        //有重复数据 删掉原有的再重新加
                        if (lList.contains(id)) {
                            lList.remove(id);
                            lList.addFirst(id);
                        }
                        //没有重复数据 删掉最后一个数据 将新的加到第一个位置
                        else {
                            lList.removeLast();
                            lList.addFirst(id);
                        }
                    }
                    //拼装#
                    StringBuffer buffer = new StringBuffer();
                    for (String l : lList) {
                        buffer.append(l).append("#");
                    }
                    return buffer.toString();
                }
            }
        }
        return id;
    }

    //更新书籍（异步）
    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        String bookid = request.getParameter("bookid");
        String name = request.getParameter("name");
        String author = request.getParameter("author");
        Double price = Double.parseDouble(request.getParameter("price"));
        String pdate_pre = request.getParameter("publishdate");
        //将字符串类型的日期格式化为Date类型
        Date d = DateUtils.parseDate(pdate_pre, new String[]{"yyyy-MM-dd"});
        //将时间转换为数据库中需要的类型
        Timestamp pdate = new Timestamp(d.getTime());
        Integer quantity = Integer.parseInt(request.getParameter("quantity"));
        Integer status = Integer.parseInt(request.getParameter("status"));
        String caid = request.getParameter("caid");
        Book b = new Book(bookid, name, author, price, pdate, caid, status, quantity, 0);

        BookService bs = new BookService();
        int i = bs.updateBook(b);
        JSONObject jo = new JSONObject();
        jo.put("flag", i == 1 ? "success" : "fail");
        PrintWriter out = response.getWriter();
        out.print(jo);
        out.close();
    }

    //根据Id删除书籍（异步）
    private void deleteBookById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        BookService bs = new BookService();
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        JSONObject jo = new JSONObject();
        jo.put("msg", bs.deleteBookById(id) == 1 ? "success" : "fail");
    }

    //导出至Excel并下载
    private void export2Excel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        //准备数据
        BookService bs = new BookService();
        //接收当前页码和一页条数
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        //set到Page实体中
        Page<Book> page = new Page<>();
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);

        //将查到的数据放在集合中
        List<Book> bookList = new ArrayList<>();
        String nameKey = request.getParameter("nameKey");
        String authorKey = request.getParameter("authorKey");
        String caidKey = request.getParameter("caidKey");
        String queryWay = request.getParameter("queryWay");

        BookVo bv = new BookVo(nameKey, caidKey, authorKey);
        bookList = bs.queryBookSeparated(page.getStart(), page.getPageSize(), bv, queryWay);

        //准备表格元数据
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("book");
        Row r1 = sheet.createRow(0);
        r1.createCell(0).setCellValue("编号");
        r1.createCell(1).setCellValue("书名");
        r1.createCell(2).setCellValue("作者");
        r1.createCell(3).setCellValue("价格");

        int index = 1;
        //将数据写入Excel
        for (Book book : bookList) {
            Row r2 = sheet.createRow(index);
            r2.createCell(0).setCellValue(index);
            r2.createCell(1).setCellValue(book.getName());
            r2.createCell(2).setCellValue(book.getAuthor());
            r2.createCell(3).setCellValue(book.getPrice());
            index++;
        }

        //将表格文件以IO流方式输出到客户端，即下载
        response.setHeader("Content-Disposition", "attachment;filename=book.xls");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
    }

    //加入购物车
    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        String id = request.getParameter("id");
        BookService bookService = new BookService();
        Book book = bookService.queryBookById(id);
        if (book.getQuantity() - 1 >= 0) {
            HttpSession session = request.getSession();
            //购物车信息以Map形式存于session中
            Map<String, Cart> shoppingCart = (HashMap<String, Cart>) session.getAttribute("shoppingCart");
            Cart cart = new Cart();
            //若此时没有这个session，那就new一个
            if (shoppingCart == null) shoppingCart = new HashMap<String, Cart>();
            else {
                cart = shoppingCart.get(id);
                //若第一次买这个书
                if (cart == null) cart = new Cart();
            }
            //操作购物车
            cart.setBook(book);
            cart.addOne();
            //操作实体类
            Book book_new = new Book();
            book_new.setId(id);
            book_new.setQuantity(book.getQuantity() - 1);
            bookService.updateBookQuantity(book_new);
            shoppingCart.put(id, cart);
            session.setAttribute("shoppingCart", shoppingCart);
            response.sendRedirect("index_main.jsp");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("<script type='text/javascript'>alert('已售罄！');" +
                    "window.location.href='index_main.jsp'</script>");
            out.close();
        }
    }

    //查询cookie最近浏览（异步）
    private void queryRecentVisiting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bookService = new BookService();
        List<Book> recentList = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cName = cookie.getName();
                if ("books".equals(cName)) {
                    String cValue = cookie.getValue();
                    String[] ss = cValue.split("#");
                    for (String s : ss) {
                        Book book = bookService.queryBookById(s);
                        recentList.add(book);
                    }
                }
            }
        }
        PrintWriter out = response.getWriter();
        JSONArray jo = JSONArray.fromObject(recentList);
        out.print(jo);
        out.close();
    }
}
