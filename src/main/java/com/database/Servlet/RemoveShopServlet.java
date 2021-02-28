package com.database.Servlet;

import com.database.connect.DataBaseAccess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveShopServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String shopId = req.getParameter("shopId");
        String isbn   = req.getParameter("isbn");
        System.out.println("shopId: " + shopId + "\t isbn: " + isbn);

        DataBaseAccess dataBaseAccess = new DataBaseAccess();
        int rowsShop    =   dataBaseAccess.deleteShopByShopIdAndIsbn(shopId, isbn);
        int rowsImage   =   dataBaseAccess.deleteBookImageByShopIdAndIsbn(shopId, isbn);

        if (rowsShop > 0 && rowsImage > 0) {
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
