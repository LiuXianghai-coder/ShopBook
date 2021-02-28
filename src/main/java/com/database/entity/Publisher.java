package com.database.entity;

import java.sql.Date;

public class Publisher {
    private     Integer     publisherId;
    private     String      publisherName;
    private     Date        publisherDate;

    // constructor
    public Publisher(Integer publisherId, String publisherName,
                     Date publisherDate) {
        checkParameter(publisherId);
        checkParameter(publisherName);

        this.publisherId    =   publisherId;
        this.publisherName  =   publisherName;
        this.publisherDate  =   publisherDate;
    }

    // default constructor
    public Publisher(){}

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        checkParameter(publisherId);
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        checkParameter(publisherName);
        this.publisherName = publisherName;
    }

    public Date getPublisherDate() {
        return publisherDate;
    }

    public void setPublisherDate(Date publisherDate) {
        this.publisherDate = publisherDate;
    }

    @Override
    public String toString() {
        return "publisherId: " + this.publisherId + "\tpublisherName: " + publisherName +
                "\tpublisherDate: " + this.publisherDate.toString();
    }

    // check parameter
    private void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("User parameter error.");
        }
    }

    public void checkParameter(Integer parameter) {
        if (parameter <= Integer.MIN_VALUE || parameter >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("author id to small or too large");
        }
    }
}
