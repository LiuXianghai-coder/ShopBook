package com.database.Servlet;

import com.database.connect.DataBaseAccess;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String  userId          =   req.getParameter("userId").trim();
        String  userPassword    =   req.getParameter("userPassword").trim();
        String  userKind        =   req.getParameter("userKind").trim();

        DataBaseAccess dataBaseAccess    =   new DataBaseAccess();
        Object user = null;

        if (userKind.trim().equalsIgnoreCase("user")) {
            user = dataBaseAccess.queryUserByIdAndPass(userId, userPassword);
        } else if (userKind.trim().equalsIgnoreCase("seller")) {
            user = dataBaseAccess.querySellerByIdAndPass(userId, userPassword);
        }

        HttpSession session =   req.getSession();
        if (user != null && userKind.trim().equalsIgnoreCase("user")) {
            session.setAttribute("userLogin", user);
            session.setAttribute("userId", userId);
            session.setAttribute("login", "success");
        } else if (user != null) {
            session.setAttribute("sellerLogin", user);
            session.setAttribute("sellerId", userId);
            session.setAttribute("login", "success");
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
