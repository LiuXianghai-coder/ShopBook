package com.database.entity;

public class BookShow {
    private Book      book;
    private Publisher publisher;
    private Double    price;
    private String    imageAddress;

    public BookShow(){}

    public BookShow(Book book, Publisher publisher,
                    Double price, String imageAddress){
        checkParameter(price);
        checkParameter(imageAddress);

        this.book           =   book;
        this.publisher      =   publisher;
        this.price          =   price;
        this.imageAddress   =   imageAddress;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        checkParameter(price);
        this.price = price;
    }

    // check parameter
    private void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("User parameter error.");
        }
    }

    private void checkParameter(Double parameter) {
        if (parameter <= Double.MIN_VALUE || parameter >= Double.MAX_VALUE) {
            throw new IllegalArgumentException("User Balance parameter error.");
        }
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        checkParameter(imageAddress);
        this.imageAddress = imageAddress;
    }
}
