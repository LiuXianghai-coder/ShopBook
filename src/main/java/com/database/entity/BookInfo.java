package com.database.entity;

import java.util.List;
import java.sql.Date;

public class BookInfo {
    private     Book            book;
    private     List<Author>    bookAuthor;
    private     List<Publisher> bookPublisher;


    // constructor
    public BookInfo(Book book, List<Author> bookAuthor,
                List<Publisher> bookPublisher) {

        this.book            =       book;
        this.bookAuthor      =       bookAuthor;
        this.bookPublisher   =       bookPublisher;
    }

    // default constructor
    public BookInfo(){}

    public List<Author> getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(List<Author> bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public List<Publisher> getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(List<Publisher> bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
