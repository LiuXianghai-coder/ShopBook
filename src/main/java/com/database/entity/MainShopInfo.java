package com.database.entity;

public class MainShopInfo {
    private Long   isbn;
    private Double price;
    private String shopId;
    private String bookName;
    private String shopName;
    private String imageAddress;
    private Double shopReputation;

    // default constructor
    public MainShopInfo(){}

    public MainShopInfo(Long isbn, Double price, String shopId, String bookName, String shopName,
                        String imageAddress, Double shopReputation) {
        checkParameter(isbn);
        checkParameter(price);
        checkParameter(shopId);
        checkParameter(bookName);
        checkParameter(shopName);
        checkParameter(imageAddress);
        checkParameter(shopReputation);

        this.isbn           =   isbn;
        this.price          =   price;
        this.shopId         =   shopId;
        this.bookName       =   bookName;
        this.shopName       =   shopName;
        this.imageAddress   =   imageAddress;
        this.shopReputation =   shopReputation;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        checkParameter(bookName);
        this.bookName = bookName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        checkParameter(shopName);
        this.shopName = shopName;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        checkParameter(imageAddress);
        this.imageAddress = imageAddress;
    }

    public Double getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(Double shopReputation) {
        checkParameter(shopReputation);
        this.shopReputation = shopReputation;
    }

    // check parameter
    private void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("User parameter error.");
        }
    }

    private void checkParameter(Double parameter) {
        if (parameter <= Double.MIN_VALUE || parameter >= Double.MAX_VALUE) {
            throw new IllegalArgumentException("isbn to small or larger.");
        }
    }
    private void checkParameter(Long parameter) {
        if (parameter <= Long.MIN_VALUE || parameter >= Long.MAX_VALUE) {
            throw new IllegalArgumentException("isbn to small or larger.");
        }
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        checkParameter(isbn);
        this.isbn = isbn;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        checkParameter(shopId);
        this.shopId = shopId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        checkParameter(price);
        this.price = price;
    }
}
