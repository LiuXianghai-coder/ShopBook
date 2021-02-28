package com.database.entity;

public class DataSourceConfigure {
    private     String      url;
    private     String      user;
    private     String      password;
    private     String      driverUrl;

    // constructor
    public DataSourceConfigure(){}
    public DataSourceConfigure(String url, String user,
                               String password, String driverUrl) {
        checkParameter(url);
        checkParameter(user);
        checkParameter(password);
        checkParameter(driverUrl);

        this.url        =       url;
        this.user       =       user;
        this.password   =       password;
        this.driverUrl  =       driverUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        checkParameter(url);
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        checkParameter(user);
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        checkParameter(password);
        this.password = password;
    }

    public String getDriverUrl() {
        return driverUrl;
    }

    public void setDriverUrl(String driverUrl) {
        checkParameter(driverUrl);
        this.driverUrl = driverUrl;
    }

    // check parameter
    private void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("User parameter error.");
        }
    }
}
