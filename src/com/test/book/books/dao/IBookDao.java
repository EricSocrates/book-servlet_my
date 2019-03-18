package com.test.book.books.dao;

import com.test.book.books.entity.Book;
import com.test.book.books.entity.Category;
import com.test.book.books.vo.BookVo;

import java.util.List;

public interface IBookDao {

    /**
     * 查询类别有没有重名
     */
    Category queryCategoryByName(String name);

    /**
     * 添加类别
     */
    Integer addCategory(Category c);

    /**
     * 查询所有类别
     */
    List<Category> queryAllCategories();

    /**
     * 新增书籍
     */
    String addBook(Book b);

    /**
     * 后台计算书籍条数
     */
    Integer countBookBg(int start, int pageSize, BookVo bv);

    /**
     * 后台分页数据
     */
    List<Book> queryBookSeparatedBg(int start, int pageSize, BookVo bv);

    /**
     * 根据id查询书籍信息
     */
    Book queryBookById(String id);

    /**
     * 更新图书
     */
    int updateBook(Book book);

    /**
     * 删除图书
     */
    int deleteBookById(String id);

    /**
     * 前端展示的计算书籍条数
     */
    int countBookFr(int start, int pageSize);

    /**
     * 前端展示的分页数据
     */
    List<Book> queryBookSeparatedFr(int start, int pageSize);

    /**
     * 改变书籍数量
     */
    int updateBookCount(Book book);
}
