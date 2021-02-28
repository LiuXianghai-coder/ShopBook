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
        name = "addBook",
        urlPatterns = {"/addBook"})
@MultipartConfig(
        fileSizeThreshold=1024*1024,
        maxFileSize=5*1024*1024,
        maxRequestSize=2*5*1024*1024)
public class AddBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String  bookAuthor      =   req.getParameter("authorName");
        String  shopId          =   req.getParameter("shopId");
        String  bookIsbn        =   req.getParameter("bookIsbn");
        String  bookName        =   req.getParameter("bookName");
        String  bookPrice       =   req.getParameter("bookPrice");
        String  bookPublisher   =   req.getParameter("publisherName");
        String  publisherDate   =   req.getParameter("publisherDate");
        String  bookDescribe    =   req.getParameter("bookDescribe");

        shopId          =   decodeString(shopId).trim();
        bookIsbn        =   decodeString(bookIsbn).trim();
        bookAuthor      =   decodeString(bookAuthor).trim();
        bookName        =   decodeString(bookName).trim();
        bookPrice       =   decodeString(bookPrice).trim();
        bookPublisher   =   decodeString(bookPublisher).trim();
        publisherDate   =   decodeString(publisherDate).trim();
        bookDescribe    =   decodeString(bookDescribe).trim();

        System.out.println("shopId: " + shopId);
        System.out.println("bookIsbn: " + bookIsbn);
        System.out.println("bookName: " + bookName);
        System.out.println("bookAuthor: " + bookAuthor);
        System.out.println("bookPrice: " + bookPrice);
        System.out.println("bookPublisher: " + bookPublisher);
        System.out.println("publisherDate: " + publisherDate);
        System.out.println("bookDescribe: " + bookDescribe);

        String imagesPath = this.getServletContext().getRealPath("/Images");
        File file = new File(imagesPath + "/" + shopId);
        if (!file.exists()) {
            System.out.println("Current shopId image folder doesn't exist, try to create it.");
            if (file.mkdirs()) {
                System.out.println("Create current shopId image folder successfully.");
            }
        }

        Collection<Part> parts = req.getParts();
        SimpleDateFormat sdf    =   new SimpleDateFormat();// 格式化时间
        SimpleDateFormat sdf1   =   new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标
        sdf1.applyPattern("yyyy-MM-dd");

        String[]  bookAuthors = bookAuthor.split("#");
        HttpSession session = req.getSession();
//        for (String string: bookAuthors) {
//            System.out.println(string);
//        }

        try {
            StringBuilder stringBuilder = new StringBuilder("Number of parts :" +
                    parts.size() + "\tDate: " + sdf.format(new Date()) + "\n");
            DataBaseAccess dataBaseAccess = new DataBaseAccess();
            for (Part part : parts) {
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

                    System.out.println("file name: " + filename);

                    FileOutputStream outputStream = new FileOutputStream(file);

                    int len = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                    }
                    outputStream.close();

                    if (dataBaseAccess.insertBook(bookIsbn, bookName,
                            bookAuthors, bookPublisher, publisherDate) &&
                        dataBaseAccess.insertIntoShopBook(shopId, bookIsbn, bookPrice, bookDescribe) >= 0 &&
                        dataBaseAccess.insertIntoBookImageAddr(shopId, bookIsbn, filename) >= 0 ) {

                        String sellerId = (String) session.getAttribute("sellerId");
                        dataBaseAccess.takeNoteUploadInfo(sellerId, bookIsbn, shopId, sdf1.format(new Date()));
                        session.setAttribute("addBookStatus", "successful");
                        System.out.println("上传成功！");
                    } else {
                        session.setAttribute("addBookStatus", null);
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

        RequestDispatcher dispatcher = req.getRequestDispatcher("ShopInfo.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getServletContext()
                .getRequestDispatcher("addBook.jsp")
                .forward(req, resp);
    }

    private String decodeString(String parameter) throws UnsupportedEncodingException {
        parameter = URLEncoder.encode(parameter, "ISO-8859-1");
        return URLDecoder.decode(parameter, "UTF-8");
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
