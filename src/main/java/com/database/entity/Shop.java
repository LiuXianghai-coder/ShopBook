package com.database.entity;

import java.sql.Date;

public class Shop {
    private     String      shopId;
    private     String      shopName;
    private     Date        shopRegister;
    private     Double      shopReputation;

    // constructor
    public Shop(String shopName, String shopId,
                Date shopRegister, Double shopReputation) {
        checkParameter(shopId);
        checkParameter(shopName);
        checkParameter(shopReputation);

        this.shopId            =       shopId;
        this.shopName          =       shopName;
        this.shopReputation    =       shopReputation;
        this.shopRegister      =        shopRegister;
    }


    // default constructor
    public Shop(){}

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Double getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(Double shopReputation) {
        this.shopReputation = shopReputation;
    }

    public Date getShopRegister() {
        return shopRegister;
    }

    public void setShopRegister(Date shopRegister) {
        this.shopRegister = shopRegister;
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
