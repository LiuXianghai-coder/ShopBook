package com.database.Servlet;

import com.database.connect.DataBaseAccess;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@WebServlet(
        name = "modifyShops",
        urlPatterns = {"/modifyShop"})
@MultipartConfig(
        fileSizeThreshold=1024*1024,
        maxFileSize=5*1024*1024,
        maxRequestSize=2*5*1024*1024)
public class ModifyShopServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String shopId       = req.getParameter("shopId");
        String isbn         = req.getParameter("isbn");
        String bookDescribe = req.getParameter("bookDescribe");
        String bookPrice    = req.getParameter("bookPrice");

        shopId          =       decodeString(shopId).trim();
        isbn            =       decodeString(isbn).trim();
        bookDescribe    =       decodeString(bookDescribe);
        bookPrice       =       decodeString(bookPrice).trim();

        System.out.println("Context path: " + this.getServletContext().getRealPath("/Images/" + shopId));
        String imagesPath = this.getServletContext().getRealPath("/Images");
        File file = new File(imagesPath + "/" + shopId);
        if (!file.exists()) {
            System.out.println("Current shopId image folder doesn't exist, try to create it.");
            if (file.mkdirs()) {
                System.out.println("Create current shopId image folder successfully.");
            }
        }

        Collection<Part> parts = req.getParts();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记

        try {
            StringBuilder stringBuilder = new StringBuilder("Number of parts :" +
                    parts.size() + "\tDate: " + sdf.format(new Date()) + "\n");
            DataBaseAccess dataBaseAccess = new DataBaseAccess();
            for(Part part : parts) {
                String info = getPartInfo(part);
                stringBuilder.append(info);
                String filename = getFileName(part);
                if (filename != null) {
                    InputStream inputStream = req.getPart(part.
                            getName()).getInputStream();
                    file = new File(imagesPath + "/"
                            + shopId + "/" + filename);
                    if (!file.exists()) {
                        System.out.println(filename + "doesn't exit, try to create it.");
                        if (file.createNewFile()) {
                            System.out.println("create file successfully.");
                        }
                    }

                    FileOutputStream outputStream = new FileOutputStream(file);

                    int len = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                    outputStream.close();

                    if (dataBaseAccess.insertIntoBookImageAddr(shopId, isbn, filename) >= 0 &&
                            dataBaseAccess.insertIntoShopBook(shopId, isbn, bookPrice, bookDescribe) >= 0) {
                        System.out.println("上传成功！");
                    }
                }
            }

            FileWriter fileWriter = new FileWriter(imagesPath + "/"
                    + shopId + "/log.txt", true);
            stringBuilder.append("\n\n");
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpSession session = req.getSession();
        session.setAttribute("modifyStatus", "successful");

        RequestDispatcher dispatcher = req.getRequestDispatcher("ShopInfo.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getServletContext()
                .getRequestDispatcher("modifyShop.jsp")
                .forward(req, resp);
    }

    // Print the headers for the given Part
    private String getPartInfo(Part part) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(part.getName()).append("\n");
        sb.append("ContentType: ").append(part.getContentType()).append("\n");
        sb.append("Size: ").append(part.getSize()).append("\n");
        for(String header : part.getHeaderNames()) {
            sb.append(header).append(": ").append(part.getHeader(header)).append("\n");
        }

        return sb.toString();
    }

    private String decodeString(String parameter) throws UnsupportedEncodingException {
        parameter = URLEncoder.encode(parameter, "ISO-8859-1");
        return URLDecoder.decode(parameter, "UTF-8");
    }

    // Gets the file name from the "content-disposition" header
    private String getFileName(Part part) {
        for (String token : part.getHeader("content-disposition").split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }
}
