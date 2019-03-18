package com.test.book.books.entity;

public class Cart {
    private Book book;
    private Integer count = 0;
    private Double sum;

    public Cart() {
    }

    public Cart(Book book, Integer count, Double sum) {
        this.book = book;
        this.count = count;
        this.sum = sum;
    }

    public void addOne() {
        count++;
    }

    public void removeOne() {
        count--;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getSum() {
        return count * book.getPrice();
    }


    @Override
    public String toString() {
        return "Cart{" +
                "book=" + book +
                ", count=" + count +
                ", sum=" + sum +
                '}';
    }
}
