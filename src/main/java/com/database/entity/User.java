package com.database.entity;

import java.sql.Date;

public class User {
    private String  userId;
    private String  userPassword;
    private String  userName;
    private Double  userBalance;
    private Date    registerDate;

    // construction
    public User() {}

    public User(String userId, String userPassword, String userName,
                Double userBalance, Date registerDate) {
        checkParameter(userId);
        checkParameter(userName);
        checkParameter(userPassword);
        checkParameter(userBalance);

        this.userId         =       userId;
        this.userName       =       userName;
        this.userPassword   =       userPassword;
        this.userBalance    =       userBalance;
        this.registerDate   =       registerDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        checkParameter(userId);
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        checkParameter(userName);
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        checkParameter(userPassword);
        this.userPassword = userPassword;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        checkParameter(userBalance);
        this.userBalance = userBalance;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
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
