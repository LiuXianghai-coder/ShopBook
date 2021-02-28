package com.database.entity;

import java.util.List;

public class SearchResult {
    private BookInfo        bookInfo;
    private List<String>    shopId;

    // default constructor
    public SearchResult(){}

    public SearchResult(BookInfo bookInfo, List<String> shopId) {
        this.bookInfo   =   bookInfo;
        this.shopId     =   shopId;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public List<String> getShopId() {
        return shopId;
    }

    public void setShopId(List<String> shopId) {
        this.shopId = shopId;
    }
}
