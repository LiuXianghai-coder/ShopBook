package com.database.Servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ValidationCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session     =   req.getSession();
        String verifyCode       =   req.getParameter("verifyCode").trim();
        String validationCode   =   session.getAttribute("verifyCode").toString().trim();
        if (verifyCode.equalsIgnoreCase(validationCode)) {
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
