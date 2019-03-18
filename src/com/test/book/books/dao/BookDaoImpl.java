package com.test.book.books.dao;

import com.test.book.books.entity.Book;
import com.test.book.books.entity.Category;
import com.test.book.books.vo.BookVo;
import com.test.book.commons.factory.ConnectionFactory;
import com.test.book.commons.util.BookIDStringHandler;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookDaoImpl implements IBookDao {

    QueryRunner qr = new QueryRunner(ConnectionFactory.getDataSource());
    String sql;

    /**
     * 查询类别有没有重名
     *
     * @param name
     */
    @Override
    public Category queryCategoryByName(String name) {
        try {
            sql = "select * from category where name=?";
            return qr.query(sql, new BeanHandler<Category>(Category.class), name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加类别
     *
     * @param c
     */
    @Override
    public Integer addCategory(Category c) {
        try {
            sql = "insert into category (id, name) values (sys_guid(), ?)";
            return qr.update(sql, c.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询所有类别
     */
    @Override
    public List<Category> queryAllCategories() {
        try {
            sql = "select * from category";
            return qr.query(sql, new BeanListHandler<Category>(Category.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增书籍
     *
     * @param b
     */
    @Override
    public String addBook(Book b) {
        try {
            //更方便地返回书籍id供添加图片时取用
            String selectKey = "select sys_guid() as id from dual";
            //继承ResultHandler对查询结果进行处理，得到或生成id并返回
            String id = (String) qr.query(selectKey, new BookIDStringHandler());
            b.setId(id);
            sql = "insert into book values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            qr.update(sql, b.getId(), b.getName(), b.getAuthor(), b.getPrice(), b.getPublishdate(), b.getCaid(), b.getStatus(), b.getQuantity(), b.getDel());
            return b.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 后台计算书籍条数
     *
     * @param start
     * @param pageSize
     * @param bv
     */
    @Override
    public Integer countBookBg(int start, int pageSize, BookVo bv) {
        try {
            sql = "select count(*) from (select * from book where del = 0";
            if (bv.getName() != null && bv.getName() != "") sql += " and name like '%" + bv.getName() + "%'";
            if (bv.getCaid() != null && bv.getCaid() != "" && !"-1".equals(bv.getCaid()))
                sql += " and caid = '" + bv.getCaid() + "'";
            sql += ")";
            return Integer.parseInt(qr.query(sql, new ScalarHandler<>()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 前端展示的计算书籍条数
     *
     * @param start
     * @param pageSize
     */
    @Override
    public int countBookFr(int start, int pageSize) {
        try {
            sql = "select count(*) from (select * from book where del = 0 and status = 0)";
            return Integer.parseInt(qr.query(sql, new ScalarHandler<>()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 后台分页数据
     *
     * @param start
     * @param pageSize
     * @param bv
     */
    @Override
    public List<Book> queryBookSeparatedBg(int start, int pageSize, BookVo bv) {
        try {
            sql = "select rownum, a.* from " +
                    "(select rownum rn, c.* from " +
                    "(select b.id as bid, b.name as bname, b.publishdate, b.status, b.del, b.quantity, b.caid as bcaid, b.price, b.author, ca.id as cid, ca.name as cname from book b left join category ca on b.caid = ca.id where del = 0";
            //按条件查询
            if (bv.getName() != null && !"".equals(bv.getName())) {
                sql += " and b.name like '%" + bv.getName() + "%'";
            }
            if (bv.getAuthor() != null && !"".equals(bv.getAuthor())) {
                sql += " and author like '%" + bv.getAuthor() + "%'";
            }
            if (bv.getCaid() != null && !"".equals(bv.getCaid()) && !"-1".equals(bv.getCaid())) {
                sql += " and b.caid = '" + bv.getCaid() + "'";
            }
            sql += "order by b.status, rownum asc) c " +
                    "where rownum <= ? ) a " +
                    "where rn >= ?";
            System.out.println(sql);
            // 组装查询结果
            List<Map<String, Object>> data = qr.query(sql, new MapListHandler(), start + pageSize, start + 1);
            List<Book> result = new ArrayList<>();
            for (Map<String, Object> d : data) {
                result.add(mapBookCategory(d));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 前端展示的分页数据
     *
     * @param start
     * @param pageSize
     */
    @Override
    public List<Book> queryBookSeparatedFr(int start, int pageSize) {
        try {
            sql = "select rownum, a.* from " +
                    "(select rownum rn, c.* from " +
                    "(select b.id as bid, b.name as bname, b.publishdate, b.quantity, b.caid as bcaid, b.price, b.author, ca.name as cname from book b left join category ca on b.caid = ca.id where del = 0 and status = 0) c where rownum <= ? ) a where rn >= ?";
            System.out.println(sql);
            // 组装查询结果
            List<Map<String, Object>> data = qr.query(sql, new MapListHandler(), start + pageSize, start + 1);
            List<Book> result = new ArrayList<>();
            for (Map<String, Object> d : data) {
                result.add(mapBookBookpicFr(d));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据id查询书籍信息
     *
     * @param id
     */
    @Override
    public Book queryBookById(String id) {
        try {
            sql = "select b.id as bid, b.name as bname, b.publishdate, b.status, b.del, b.quantity, b.caid as bcid, b.price, b.author, " +
                    "ca.id as cid, ca.name as cname from book b left join category ca on b.caid = ca.id " +
                    "where b.id = ?";
            return mapBookCategory(qr.query(sql, new MapHandler(), id));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Book和Category多表查询时，对得到的Map进行拆包封装到实体中
    private Book mapBookCategory(Map<String, Object> data) throws Exception {
        Book b = new Book();
        b.setId(data.get("bid") + "");
        b.setName(data.get("bname") + "");
        b.setAuthor(data.get("author") + "");
        b.setPrice(Double.parseDouble(data.get("price") + ""));
        b.setPublishdate(new Timestamp(DateUtils.parseDate(data.get("publishdate") + "", new String[]{"yyyy-MM-dd hh:mm:ss.S"}).getTime()));
        b.setCaid(data.get("bcaid") + "");
        b.setStatus(Integer.parseInt(data.get("status") + ""));
        b.setQuantity(Integer.parseInt(data.get("quantity") + ""));
        b.setDel(Integer.parseInt(data.get("del") + ""));
        Category ca = new Category();
        ca.setId(data.get("cid") + "");
        ca.setName(data.get("cname") + "");
        b.setCa(ca);
        return b;
    }

    private Book mapBookBookpicFr(Map<String, Object> data) throws ParseException {
        Book b = new Book();
        b.setId(data.get("bid") + "");
        b.setName(data.get("bname") + "");
        b.setAuthor(data.get("author") + "");
        b.setPrice(Double.parseDouble(data.get("price") + ""));
        b.setPublishdate(new Timestamp(DateUtils.parseDate(data.get("publishdate") + "", new String[]{"yyyy-MM-dd hh:mm:ss.S"}).getTime()));
        b.setCaid(data.get("bcaid") + "");
        b.setCaid(data.get("cname") + "");
        b.setQuantity(Integer.parseInt(data.get("quantity") + ""));
        IBookpicDao bpdao = new BookpicDaoImpl();
        Category ca = new Category();
        ca.setId(data.get("bcid") + "");
        ca.setName(data.get("cname") + "");
        b.setCa(ca);
        b.setBps(bpdao.queryBookpicByBookId(b.getId()));
        return b;
    }

    /**
     * 更新图书
     *
     * @param book
     */
    @Override
    public int updateBook(Book book) {
        try {
            sql = "update book set name = ?, " +
                    "author = ?," +
                    "price = ?," +
                    "publishdate = ?," +
                    "caid = ?," +
                    "status = ?," +
                    "quantity = ?," +
                    "del = ?" +
                    "where id = ?";
            return qr.update(sql, book.getName(),
                    book.getAuthor(),
                    book.getPrice(),
                    book.getPublishdate(),
                    book.getCaid(),
                    book.getStatus(),
                    book.getQuantity(),
                    book.getDel(),
                    book.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除图书
     *
     * @param id
     */
    @Override
    public int deleteBookById(String id) {
        try {
            sql = "update book set del = 1 where id = ?";
            return qr.update(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 改变书籍数量
     *
     * @param book
     */
    @Override
    public int updateBookCount(Book book) {
        try {
            sql = "update book set quantity = ? where id = ?";
            return qr.update(sql, book.getQuantity(), book.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
