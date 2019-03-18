package com.test.book.books.vo;

public class BookVo {
    private String name;
    private String caid;
    private String author;

    public BookVo() {
    }

    public BookVo(String name, String caid, String author) {
        this.name = name;
        this.caid = caid;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaid() {
        return caid;
    }

    public void setCaid(String caid) {
        this.caid = caid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookVo{" +
                "name='" + name + '\'' +
                ", caid='" + caid + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
