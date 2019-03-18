package com.test.book.books.dao;

import com.test.book.books.entity.Bookpic;
import com.test.book.commons.factory.ConnectionFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class BookpicDaoImpl implements IBookpicDao {

    QueryRunner qr = new QueryRunner(ConnectionFactory.getDataSource());
    String sql;

    /**
     * 新增书籍图片
     *
     * @param bp
     */
    @Override
    public void addBookpic(Bookpic bp) {
        try {
            sql = "insert into bookpic values(sys_guid(), ?, ?, ?, ?)";
            qr.update(sql, bp.getSavepath(), bp.getShowname(), bp.getIscover(), bp.getBookid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将封面全部置于0
     *
     * @param bid
     */
    @Override
    public void updateBookSetCover0(String bid) {
        try {
            sql = "update bookpic set iscover=0 where bookid = ?";
            qr.update(sql, bid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将某个封面置于1
     *
     * @param bpid
     */
    @Override
    public int updateBookSetCover1(String bpid) {
        try {
            sql = "update bookpic set iscover = 1 where id = ?";
            return qr.update(sql, bpid);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据图片的id查询图片
     *
     * @param id
     */
    @Override
    public Bookpic queryBookpicById(String id) {
        try {
            sql = "select * from bookpic where id = ?";
            return qr.query(sql, new BeanHandler<Bookpic>(Bookpic.class), id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据图片的id删除图片
     *
     * @param id
     */
    @Override
    public int deleteBookpicById(String id) {
        try {
            sql = "delete from bookpic where id = ?";
            return qr.update(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据书籍的id查询图片
     *
     * @param id
     */
    @Override
    public List<Bookpic> queryBookpicByBookId(String id) {
        try {
            sql = "select * from bookpic where bookid = ? order by iscover desc";
            return qr.query(sql, new BeanListHandler<Bookpic>(Bookpic.class), id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
