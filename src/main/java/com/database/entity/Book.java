package com.database.entity;

public class Book {
    private     Long            isbn;
    private     String          bookName;

    // constructor
    public Book(){}

    // default constructor
    public Book(Long isbn, String bookName) {
        checkParameter(isbn);
        checkParameter(bookName);

        this.isbn       =     isbn;
        this.bookName   =     bookName;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        checkParameter(isbn);
        this.isbn = isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        checkParameter(bookName);
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "bookISBN: " + this.isbn + "\tBookName: " + this.bookName;
    }

    // check parameter
    private void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("User parameter error.");
        }
    }

    private void checkParameter(Long parameter) {
        if (parameter <= Long.MIN_VALUE || parameter >= Long.MAX_VALUE) {
            throw new IllegalArgumentException("isbn to small or larger.");
        }
    }
}
