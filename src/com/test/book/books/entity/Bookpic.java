package com.test.book.books.entity;

public class Bookpic {
    private String id;
    private String savepath;
    private String showname;
    private Integer iscover;
    private String bookid;

    public Bookpic() {
    }

    public Bookpic(String id, String savepath, String showname, Integer iscover, String bookid) {
        this.id = id;
        this.savepath = savepath;
        this.showname = showname;
        this.iscover = iscover;
        this.bookid = bookid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public Integer getIscover() {
        return iscover;
    }

    public void setIscover(Integer iscover) {
        this.iscover = iscover;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
}
