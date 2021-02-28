package com.database.Servlet;

import com.database.connect.DataBaseAccess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class UserIdVerifyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain");

        String userId   = req.getParameter("userId");
        String userKind = req.getParameter("userKind");

        DataBaseAccess access = new DataBaseAccess();
        Object obj = null;
        if (userKind.trim().equalsIgnoreCase("user")) {
            obj = access.queryUserIdByUserId(userId);
        } else if (userKind.trim().equalsIgnoreCase("seller")) {
            obj = access.querySellerIdBySellerId(userId);
        }

        if (obj == null) {
            resp.getWriter().write("1");
        } else {
            resp.getWriter().write("0");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
