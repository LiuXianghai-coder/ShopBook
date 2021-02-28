package com.database.entity;

import java.sql.Date;

public class Seller {
    private String  sellerId;
    private String  sellerName;
    private String  sellerPassword;
    private Double  sellerMoney;
    private Date    sellerRegisterDate;

    // construction
    public Seller(String sellerId, String sellerPassword, String sellerName,
                  Double sellerMoney, Date sellerRegisterDate) {
        checkParameter(sellerId);
        checkParameter(sellerName);
        checkParameter(sellerPassword);
        checkParameter(sellerMoney);

        this.sellerId           =           sellerId;
        this.sellerName         =           sellerName;
        this.sellerPassword     =           sellerPassword;
        this.sellerMoney        =           sellerMoney;
        this.sellerRegisterDate =           sellerRegisterDate;
    }

    public Seller(){}

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        checkParameter(sellerId);
        this.sellerId = sellerId;
    }

    public String getSellerPassword() {
        return sellerPassword;
    }

    public void setSellerPassword(String sellerPassword) {
        checkParameter(sellerPassword);
        this.sellerPassword = sellerPassword;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        checkParameter(sellerName);
        this.sellerName = sellerName;
    }

    public Double getSellerMoney() {
        return sellerMoney;
    }

    public void setSellerMoney(Double sellerMoney) {
        checkParameter(sellerMoney);
        this.sellerMoney = sellerMoney;
    }

    public Date getSellerRegisterDate() {
        return sellerRegisterDate;
    }

    public void setSellerRegisterDate(Date sellerRegisterDate) {
        this.sellerRegisterDate = sellerRegisterDate;
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
