package com.database.entity;

public class ShopBook {
    private BookInfo bookInfo;
    private Double   price;
    private String   describe;

    public ShopBook(){}

    public ShopBook(BookInfo bookInfo, Double price, String describe) {
        checkParameter(price);
        checkParameter(describe);

        this.bookInfo = bookInfo;
        this.price    = price;
        this.describe = describe;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        checkParameter(price);
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        checkParameter(describe);
        this.describe = describe;
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
}
