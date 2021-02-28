package com.database.Servlet;

import com.database.connect.DataBaseAccess;
import com.database.entity.Seller;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(
        name = "createShop",
        urlPatterns = {"/createShop"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 2 * 5 * 1024 * 1024)
public class CreateShopServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String shopName = req.getParameter("shopName");
        // 对输入的请求文本进行一个编码和解码操作
        shopName = URLEncoder.encode(shopName, "ISO-8859-1");
        shopName = URLDecoder.decode(shopName, "UTF-8");
        System.out.println("Create Shop Name: " + shopName);
        HttpSession session = req.getSession();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-MM-dd");

        String dateString = simpleDateFormat.format(new Date());
        StringBuilder markString = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(dateString);
        while (matcher.find()) {
            markString.append(matcher.group(0));
        }

        Seller seller = (Seller) session.getAttribute("sellerLogin");
        String shopId = seller.getSellerId() + markString.toString();
        System.out.println("shopId: " + shopId);

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

                    if (dataBaseAccess.addShop(shopId, seller.getSellerId(),
                            filename, shopName, sdf1.format(new Date()), 100.0) > 0) {
                        session.setAttribute("createShopStatus", "successful");
                        System.out.println("创建成功！");
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

        RequestDispatcher dispatcher = req.getRequestDispatcher("manageShop.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getServletContext()
                .getRequestDispatcher("CreateShop.jsp")
                .forward(req, resp);
    }

    // Print the headers for the given Part
    private String getPartInfo(Part part) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(part.getName()).append("\n");
        sb.append("ContentType: ").append(part.getContentType()).append("\n");
        sb.append("Size: ").append(part.getSize()).append("\n");
        for (String header : part.getHeaderNames()) {
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
