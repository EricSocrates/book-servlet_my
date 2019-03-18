package com.test.book.books.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class Book {
    private String id;
    private String name;
    private String author;
    private Double price;
    private Timestamp publishdate;
    private String publishdateFormatted;
    private String caid;
    /**
     * =0 上架
     * =1 下架
     */
    private Integer status;
    private Integer quantity;
    /**
     * =0 显示
     * =1 删除
     */
    private Integer del;

    // 表关联
    private Category ca;
    private Bookpic bp;
    private List<Bookpic> bps;

    public Book() {
    }

    public Book(String id, String name, String author, Double price, Timestamp publishdate, String caid, Integer status, Integer quantity, Integer del) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.publishdate = publishdate;
        this.caid = caid;
        this.status = status;
        this.quantity = quantity;
        this.del = del;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Timestamp publishdate) {
        this.publishdate = publishdate;
    }

    public String getPublishdateFormatted() {
        return new SimpleDateFormat("yyyy-MM-dd").format(publishdate);
    }

    public String getCaid() {
        return caid;
    }

    public void setCaid(String caid) {
        this.caid = caid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Category getCa() {
        return ca;
    }

    public void setCa(Category ca) {
        this.ca = ca;
    }

    public Bookpic getBp() {
        return bp;
    }

    public void setBp(Bookpic bp) {
        this.bp = bp;
    }

    public List<Bookpic> getBps() {
        return bps;
    }

    public void setBps(List<Bookpic> bps) {
        this.bps = bps;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", publishdate=" + publishdate +
                ", caid='" + caid + '\'' +
                ", status=" + status +
                ", quantity=" + quantity +
                ", del=" + del +
                ", ca=" + ca +
                ", bp=" + bp +
                ", bps=" + bps +
                '}';
    }
}
