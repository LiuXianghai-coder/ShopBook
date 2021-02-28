package com.database.Servlet;

import com.database.connect.DataBaseAccess;
import com.database.entity.Seller;
import com.database.entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        java.util.Date date = new java.util.Date();
        Date registerDate   = Date.valueOf(new SimpleDateFormat("YYYY-MM-dd").format(date));

        String userId       = req.getParameter("userId");
        String userName     = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String userKind     = req.getParameter("userKind");

        userId          =     decodeString(userId);
        userName        =     decodeString(userName);
        userPassword    =     decodeString(userPassword);
        userKind        =     decodeString(userKind);

        System.out.println("UserKind: " + userKind);
        int count = 0;

        DataBaseAccess dataBaseAccess = new DataBaseAccess();

        if (userKind.trim().equalsIgnoreCase("user")) {
            User user = new User(userId, userPassword, userName, 100.0, registerDate);
            count = dataBaseAccess.addUserAndSeller(user, User.class);
        } else if (userKind.trim().equalsIgnoreCase("seller")) {
            Seller seller = new Seller(userId, userPassword, userName, 100.0, registerDate);
            count = dataBaseAccess.addUserAndSeller(seller, Seller.class);
        }

        HttpSession session = req.getSession();
        if (count > 0) {
            session.setAttribute("registerStatus", "");
        } else {
            session.setAttribute("registerStatus", null);
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    private String decodeString(String parameter) throws UnsupportedEncodingException {
        parameter = URLEncoder.encode(parameter, "ISO-8859-1");
        return URLDecoder.decode(parameter, "UTF-8");
    }
}
