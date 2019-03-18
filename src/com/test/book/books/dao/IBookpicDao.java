package com.test.book.books.dao;

import com.test.book.books.entity.Bookpic;

import java.util.List;

public interface IBookpicDao {

    /**
     * 新增书籍图片
     */
    void addBookpic(Bookpic bp);

    /**
     * 将封面全部置于0
     */
    void updateBookSetCover0(String bid);

    /**
     * 将某个封面置于1
     */
    int updateBookSetCover1(String bpid);

    /**
     * 根据图片的id查询图片
     */
    Bookpic queryBookpicById(String id);

    /**
     * 根据图片的id删除图片
     */
    int deleteBookpicById(String id);

    /**
     * 根据书籍的id查询图片
     */
    List<Bookpic> queryBookpicByBookId(String id);
}
