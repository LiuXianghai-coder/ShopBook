package com.database.entity;

public class Author {
    private     Integer     authorId;
    private     String      authorName;

    // constructor
    public Author(Integer authorId, String authorName) {
        checkParameter(authorId);
        checkParameter(authorName);

        this.authorId   =   authorId;
        this.authorName =   authorName;
    }

    // default constructor
    public Author(){}

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "AuthorId: " + this.authorId +
                "\tAuthorName: " + this.authorName;
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
