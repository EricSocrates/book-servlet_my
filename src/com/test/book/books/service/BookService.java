package com.test.book.books.service;

import com.test.book.books.dao.BookDaoImpl;
import com.test.book.books.dao.BookpicDaoImpl;
import com.test.book.books.dao.IBookDao;
import com.test.book.books.dao.IBookpicDao;
import com.test.book.books.entity.Book;
import com.test.book.books.entity.Category;
import com.test.book.books.vo.BookVo;

import java.util.List;

public class BookService {
    private IBookDao dao = new BookDaoImpl();
    private IBookpicDao pdao = new BookpicDaoImpl();

    //查询类别有没有重名
    public String checkCategoryName(String name) {
        return dao.queryCategoryByName(name) == null ? "success" : "exists";
    }

    //添加类别
    public String addCategory(Category c) {
        return dao.addCategory(c) == 1 ? "success" : "fail";
    }

    //查询所有类别
    public List<Category> queryAllCategories() {
        return dao.queryAllCategories();
    }

    //添加书籍
    public String addBook(Book b) {
        return dao.addBook(b);
    }

    //后台分页的条数
    public int countBook(int start, int pageSize, BookVo bv, String queryWay) {
        if ("front".equals(queryWay)) return dao.countBookFr(start, pageSize);
        else return dao.countBookBg(start, pageSize, bv);
    }

    //后台数据分页
    public List<Book> queryBookSeparated(int start, int pageSize, BookVo bv, String queryWay) {
        if ("front".equals(queryWay)) return dao.queryBookSeparatedFr(start, pageSize);
        else return dao.queryBookSeparatedBg(start, pageSize, bv);
    }

    //根据id查询书籍信息
    public Book queryBookById(String id) {
        Book b = dao.queryBookById(id);
        b.setBps(pdao.queryBookpicByBookId(id));
        return b;
    }

    //更新书籍
    public int updateBook(Book b) {
        return dao.updateBook(b);
    }

    //更新书籍剩余数量
    public int updateBookQuantity(Book book) {
        return dao.updateBookCount(book);
    }

    //根据Id删除图书
    public int deleteBookById(String id) {
        return dao.deleteBookById(id);
    }
}
