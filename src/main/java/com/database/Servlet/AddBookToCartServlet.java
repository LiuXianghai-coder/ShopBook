package com.database.Servlet;

import com.database.connect.DataBaseAccess;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@WebServlet(
        name = "addBookToCart",
        urlPatterns = "/addBookToCart"
)
public class AddBookToCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String  userId    =     req.getParameter("userId");
        String  shopId    =     req.getParameter("shopId");
        String  isbn      =     req.getParameter("isbn");

        userId      =       decodeString(userId);
        shopId      =       decodeString(shopId);
        isbn        =       decodeString(isbn);

        DataBaseAccess  dataBaseAccess  =   new DataBaseAccess();
        if (dataBaseAccess.addBookToCart(userId, shopId, isbn) > 0) {
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

    private String decodeString(String parameter)
            throws UnsupportedEncodingException {
        parameter = URLEncoder.encode(parameter, "ISO-8859-1");
        return URLDecoder.decode(parameter, "UTF-8");
    }
}
