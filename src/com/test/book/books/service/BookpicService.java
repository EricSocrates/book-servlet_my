package com.test.book.books.service;

import com.test.book.books.dao.BookpicDaoImpl;
import com.test.book.books.dao.IBookpicDao;
import com.test.book.books.entity.Bookpic;

public class BookpicService {

    IBookpicDao dao = new BookpicDaoImpl();

    //添加书籍图片
    public void addBookpic(Bookpic bp) {
        dao.addBookpic(bp);
    }

    //将封面全部置于0
    public void updateBookSetCover0(String bid) {
        dao.updateBookSetCover0(bid);
    }

    //将某个封面置于1
    public int updateBookSetCover1(String bpid) {
        return dao.updateBookSetCover1(bpid);
    }

    //根据图片的id查询图片
    public Bookpic queryBookpicById(String id) {
        return dao.queryBookpicById(id);
    }

    //根据图片的id删除图片
    public int deleteBookpicById(String id) {
        return dao.deleteBookpicById(id);
    }
}
