package com.database.connect;

import com.database.entity.*;
import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBaseShop {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    /**
     * 初始化创建连接
     */
    public void initConnection() {
        try {
//            Gson gson = new Gson();
//            Reader reader = Files.newBufferedReader(Paths.get("D:/javawork/IdeaProjects/ShopBook/DataSourceConfigure.json"));
//            DataSourceConfigure dataSourceConfigure =
//                    gson.fromJson(reader,
//                            DataSourceConfigure.class);
            DataSourceConfigure dataSourceConfigure = new DataSourceConfigure();
            dataSourceConfigure.setUrl("jdbc:postgresql://127.0.0.1:5432/book_shop");
            dataSourceConfigure.setDriverUrl("org.postgresql.Driver");
            dataSourceConfigure.setPassword("17358870357yi");
            dataSourceConfigure.setUser("postgres");
            Class.forName(dataSourceConfigure.getDriverUrl());
            this.connection = DriverManager.getConnection(dataSourceConfigure.getUrl(),
                    dataSourceConfigure.getUser(),
                    dataSourceConfigure.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 得到指定商家的所有店铺的信息
    public List<Shop> getAllShopBySellerId(String sellerId) {
        if (sellerId == null || sellerId.trim().length() == 0) {
            throw new IllegalArgumentException("sellerId error");
        }

        String sql = "SELECT shop.shop_id, shop.shop_name, shop.shop_reputation, shop.shop_create_date\n" +
                "       FROM shop, shop_seller WHERE shop.seller_id=shop_seller.seller_id AND " +
                "shop_seller.seller_id='" + sellerId + "'";
        List<Shop> shopList = new ArrayList<>();
        try {
            List<Element> elementList = parserMappingXml(Shop.class);
            Map<String, String> propertyToColumn = getPropertyToColumn(elementList, Shop.class);

            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Shop shop = Shop.class.newInstance();
                Field[] fields = Shop.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(shop, resultSet.getObject(propertyToColumn.get(field.getName())));
                }

                shopList.add(shop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shopList;
    }

    private <T> Map<String, String> getPropertyToColumn(List<Element> elementList, Class<T> C) {
        Map<String, String> propertyToColumn = new HashMap<>();
        for (Element element : elementList) {
            propertyToColumn.put(element.getAttribute("property"),
                    element.getAttribute("column"));
        }

        return propertyToColumn;
    }

    // 得到指定店铺的所有商品
    public List<ShopBook> getAllShopByShopIdAndSellerId(String shop_id, String seller_id) {
        if (seller_id == null || seller_id.trim().length() == 0) {
            throw new IllegalArgumentException("seller_id parameter error");
        }
        if (shop_id == null || shop_id.trim().length() == 0) {
            throw new IllegalArgumentException("shop_id parameter error");
        }

        String sql = "SELECT foo.shop_name,foo.shop_create_date, foo.shop_reputation,\n" +
                "       shop_book.isbn, shop_book.price, shop_book.describe\n" +
                "FROM (SELECT shop.shop_id,shop.shop_name,shop.shop_create_date,\n" +
                "shop.shop_reputation FROM shop, shop_seller WHERE shop.seller_id='" + seller_id + "')" +
                "AS foo, shop_book WHERE foo.shop_id='" + shop_id + "'";
        List<ShopBook> shopBookList = new ArrayList<>();
        List<Element> elementList = parserMappingXml(ShopBook.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, ShopBook.class);

        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            DataBaseAccess dataBaseAccess = new DataBaseAccess();
            while (resultSet.next()) {
                Field[] fields = ShopBook.class.getDeclaredFields();
                ShopBook obj = new ShopBook();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equalsIgnoreCase("bookInfo")) {
                        BookInfo bookInfo = dataBaseAccess.
                                queryBookInfoByIsbn(String.valueOf(resultSet.getLong(propertyToColumn.
                                        get(field.getName())))).get(0);
                        field.set(obj, bookInfo);
                    } else {
                        field.set(obj, resultSet.getObject(propertyToColumn.get(field.getName())));
                    }
                }

                shopBookList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return shopBookList;
    }

    //得到指定店铺的指定书籍的基本信息
    public ShopBook getShopBookByShopIdAndIsbn(String shopId, String isbn) {
        checkParameter(shopId);
        checkParameter(isbn);

        String sql = "SELECT foo.shop_name,foo.shop_create_date, foo.shop_reputation,\n" +
                "       shop_book.isbn, shop_book.price, shop_book.describe\n" +
                "FROM (SELECT shop.shop_id,shop.shop_name,shop.shop_create_date,\n" +
                "shop.shop_reputation FROM shop, shop_seller WHERE shop.seller_id=shop_seller.seller_id)\n" +
                "AS foo, shop_book WHERE foo.shop_id=shop_book.shop_id AND isbn=" + Long.parseLong(isbn) +
                " AND foo.shop_id='" + shopId + "'";
        List<Element> elementList = parserMappingXml(ShopBook.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, ShopBook.class);

        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            DataBaseAccess dataBaseAccess = new DataBaseAccess();
            if (resultSet.next()) {
                Field[] fields = ShopBook.class.getDeclaredFields();
                ShopBook obj = new ShopBook();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equalsIgnoreCase("bookInfo")) {
                        BookInfo bookInfo = dataBaseAccess.
                                queryBookInfoByIsbn(String.valueOf(resultSet.getLong(propertyToColumn.
                                        get(field.getName())))).get(0);
                        field.set(obj, bookInfo);
                    } else {
                        field.set(obj, resultSet.getObject(propertyToColumn.get(field.getName())));
                    }
                }

                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 通过shopId和isbn得到对应的商品图像路径
    public String getImageAddressByShopIdAndIsbn(String shopId, String isbn) {
        if (shopId == null || shopId.trim().length() == 0) {
            throw new IllegalArgumentException("shopId parameter error");
        }
        if (isbn == null || isbn.trim().length() == 0) {
            throw new IllegalArgumentException("isbn parameter error");
        }

        String sql = "SELECT image_address FROM book_image_address WHERE " +
                "shop_id='" + shopId + "' AND isbn='" + isbn + "'";
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 对搜索框输入的查找信息进行一个更详细的查找
    private List<Long> getIsbnBySearchInput(String searchInput) {
        checkParameter(searchInput);
        String sql = "SELECT isbn FROM (SELECT bookAuthor.isbn, book_name, author_name,\n" +
                "publisher_name FROM (SELECT * FROM (SELECT book.isbn,\n" +
                "book_name, author_name FROM book, book_belong, author\n" +
                "WHERE book.isbn=book_belong.isbn AND book_belong.book_author_id=author_id) AS foo) AS bookAuthor,\n" +
                "book_publisher, publisher WHERE bookAuthor.isbn=book_publisher.isbn AND\n" +
                "publisher.publisher_id=book_publisher.publisher_id ) AS bookInfo\n" +
                "WHERE upper(book_name) LIKE upper('%" + searchInput + "%') OR upper(author_name) " +
                "LIKE upper('%" + searchInput + "%')\n" +
                "OR upper(publisher_name) LIKE upper('%" + searchInput + "%') GROUP BY isbn";
        List<Long> isbnList = new ArrayList<>();
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isbnList.add(resultSet.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isbnList;
    }

    // 将含有对应的isbn的店铺的id存在列表中， 便于查询相关信息
    private List<String> getShopIdByIsbn(String isbn) {
        checkParameter(isbn);
        List<String> shopIdList = new ArrayList<>();
        String sql = "SELECT shop_id FROM shop_book WHERE isbn=" + Long.parseLong(isbn);
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                shopIdList.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shopIdList;
    }

    // 将得到的书籍信息存储在列表中
    public List<SearchResult> getSearchResult(String searchInput) {
        checkParameter(searchInput);
        List<SearchResult> searchResults = new ArrayList<>();
        try {
            initConnection();
            List<Long> isbnList = getIsbnBySearchInput(searchInput);
            GetBookInfo getBookInfo = new GetBookInfo();
            for (Long isbn : isbnList) {
                List<String> list = getShopIdByIsbn(String.valueOf(isbn));
                searchResults.add(new SearchResult(getBookInfo.
                        getBookInfoByIsbn(String.valueOf(isbn)).get(0), list));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return searchResults;
    }

    // 如果输入时isbn号
    public List<SearchResult> getSearchResult(Long isbn) {
        List<SearchResult> searchResults = new ArrayList<>();
        try {
            initConnection();
            GetBookInfo getBookInfo = new GetBookInfo();
            List<String> list = getShopIdByIsbn(String.valueOf(isbn));
            searchResults.add(new SearchResult(getBookInfo.
                    getBookInfoByIsbn(String.valueOf(isbn)).get(0), list));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    // 通过sql得到指定的商品序列
    public List<BookShow> getAllBookByShopIdAndSellerId(String sql, Object[] objects) {
        if (sql == null || sql.trim().length() == 0) {
            throw new IllegalArgumentException("SQL parameter error");
        }

        List<Element> elementList = parserMappingXml(Book.class);
        List<Element> elementList1 = parserMappingXml(Publisher.class);
        List<Element> elementList2 = parserMappingXml(BookShow.class);
        Map<String, String> bookToCol = getPropertyToColumn(elementList, Book.class);
        Map<String, String> publisherToCol = getPropertyToColumn(elementList1, Publisher.class);
        Map<String, String> bookShowToCol = getPropertyToColumn(elementList2, BookShow.class);

        List<BookShow> bookShowList = new ArrayList<>();
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (objects != null) {
                int index = 1;
                for (Object object : objects) {
                    preparedStatement.setObject(index++, object);
                }
            }

            resultSet = preparedStatement.executeQuery();
            Field[] fields = BookShow.class.getDeclaredFields();
            while (resultSet.next()) {
                BookShow obj = new BookShow();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equalsIgnoreCase("book")) {
                        Book book = new Book();
                        Field[] fields1 = Book.class.getDeclaredFields();
                        for (Field field1 : fields1) {
                            field1.setAccessible(true);
                            field1.set(book, resultSet.getObject(bookToCol.get(field1.getName())));
                        }

                        field.set(obj, book);
                    } else if (field.getName().equalsIgnoreCase("publisher")) {
                        Publisher publisher = new Publisher();
                        Field[] fields1 = Publisher.class.getDeclaredFields();
                        for (Field field1 : fields1) {
                            field1.setAccessible(true);
                            field1.set(publisher, resultSet.getObject(publisherToCol.get(field1.getName())));
                        }
                        field.set(obj, publisher);
                    } else {
                        field.set(obj, resultSet.getObject(bookShowToCol.get(field.getName())));
                    }
                }

                bookShowList.add(obj);
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bookShowList;
    }

    /**
     * 刪除指定的商品
     *
     * @param shopId: 刪除的商店的id
     * @param isbn:   刪除的商店内的isbn
     * @return int: 成功刪除的行数, 如果失败则返回-1
     */
    public int deleteShopByShopIdAndIsbn(String shopId, String isbn) throws SQLException {
        if (shopId == null || shopId.trim().length() == 0) {
            throw new IllegalArgumentException("shopId parameter error");
        }
        if (isbn == null || isbn.trim().length() == 0) {
            throw new IllegalArgumentException("isbn parameter error.");
        }

        String sql = "DELETE FROM shop_book WHERE shop_id='"
                + shopId + "' AND isbn=" + Long.parseLong(isbn);
        int rows = -1;
        try {
            initConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            rows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            close();
        }

        return rows;
    }

    /**
     * 刪除对应的商品的图像地址
     *
     * @param shopId：店铺的id
     * @param isbn：书籍的isbn
     * @return rows：删除成功的记录数
     * @throws SQLException ： 如果出现异常
     */
    public int deleteBookImageByShopIdAndIsbn(String shopId, String isbn) throws SQLException {
        if (shopId == null || shopId.trim().length() == 0) {
            throw new IllegalArgumentException("shopId parameter error");
        }
        if (isbn == null || isbn.trim().length() == 0) {
            throw new IllegalArgumentException("isbn parameter error.");
        }

        String sql = "DELETE FROM book_image_address WHERE shop_id='"
                + shopId + "' AND isbn=" + Long.parseLong(isbn);

        int rows = -1;
        try {
            initConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            rows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            close();
        }

        return rows;
    }

    private void insertIntoBookRelation(String sql, String[] authorNames) {
        checkParameter(sql);
        try {
            for (String authorName : authorNames) {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, authorName);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getBookRelationId(String sql, String[] parameters) {
        checkParameter(sql);
        for (String parameter : parameters) {
            checkParameter(parameter);
        }

        List<Integer> integerList = new ArrayList<>();
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (String parameter : parameters) {
                preparedStatement.setObject(1, parameter);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) integerList.add(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return integerList;
    }

    public boolean insertBook(String isbn, String bookName, String[] bookAuthor,
                              String publisherName, String publisherDate) throws SQLException {
        checkParameter(isbn);
        checkParameter(bookName);
        for (String string : bookAuthor) {
            checkParameter(string);
        }
        checkParameter(publisherName);
        checkParameter(publisherDate);

        String insertIntoAuthor = "INSERT INTO author(author_name) VALUES (?) " +
                "ON CONFLICT (author_name) DO NOTHING";
        String insertIntoPublisher = "INSERT INTO publisher(publisher_name) " +
                "VALUES (?) ON CONFLICT (publisher_name) DO NOTHING";
        String insertIntoBook = "INSERT INTO book(isbn, book_name) VALUES (?, ?)" +
                " ON CONFLICT (isbn) DO NOTHING";
        String getAuthorId = "SELECT author_id FROM author WHERE author_name=?";
        String getPublisherId = "SELECT publisher_id FROM publisher WHERE publisher_name=?";
        String insertBookBelong = "INSERT INTO book_belong(isbn, book_author_id) VALUES (?, ?) " +
                "ON CONFLICT(isbn, book_author_id) DO NOTHING";
        String insertBookPublish = "INSERT INTO book_publisher(isbn, publisher_id, publisher_date)\n" +
                "VALUES (?, ?, ?) ON CONFLICT (isbn, publisher_id) DO UPDATE SET publisher_date=?";

        operatingDataBase(insertIntoBook, new Object[]{Long.parseLong(isbn), bookName});
        try {
            initConnection();

            insertIntoBookRelation(insertIntoAuthor, bookAuthor);
            operatingDataBase(insertIntoPublisher, new String[]{publisherName});

            initConnection();
            List<Integer> authorIdList = getBookRelationId(getAuthorId, bookAuthor);
            List<Integer> publisherList = getBookRelationId(getPublisherId, new String[]{publisherName});

            for (Integer integer : authorIdList) {
                operatingDataBase(insertBookBelong, new Object[]{Long.parseLong(isbn), integer});
            }

            for (Integer integer : publisherList) {
                operatingDataBase(insertBookPublish, new
                        Object[]{Long.parseLong(isbn), integer,
                        Date.valueOf(publisherDate), Date.valueOf(publisherDate)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void manufactureDataBase(String sql, Object[] objects) {
        checkParameter(sql);
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (objects != null) {
                int index = 1;
                for (Object object : objects) {
                    preparedStatement.setObject(index++, object);
                }
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int operatingDataBase(String sql, Object[] objects) {
        checkParameter(sql);
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (objects != null) {
                int index = 1;
                for (Object object : objects) {
                    preparedStatement.setObject(index++, object);
                }
            }
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    public int controlDataBase(String sql, Object[] objects) {
        checkParameter(sql);
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (objects != null) {
                int index = 1;
                for (Object object : objects) {
                    preparedStatement.setObject(index++, object);
                }
            }
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    // 记录上传的信息
    public void takeNoteUploadInfo(String sellerId, String isbn,
                                   String shopId, String uploadDate) throws SQLException {
        checkParameter(sellerId);
        checkParameter(isbn);
        checkParameter(shopId);
        checkParameter(uploadDate);

        try {
            initConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO book_upload(seller_id, isbn, shop_id, upload_date) " +
                    "VALUES (?, ?, ?, ?) ON CONFLICT (seller_id, isbn, shop_id) DO UPDATE SET upload_date=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, sellerId);
            preparedStatement.setObject(2, Long.parseLong(isbn));
            preparedStatement.setObject(3, shopId);
            preparedStatement.setObject(4, Date.valueOf(uploadDate));
            preparedStatement.setObject(5, Date.valueOf(uploadDate));
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        }

        close();
    }

    public List<MainShopInfo> getMainShopInfo() {
        List<MainShopInfo> shopInfoList = new ArrayList<>();
        List<Element> elementList = parserMappingXml(MainShopInfo.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, MainShopInfo.class);
        try {
            initConnection();
            String sql = "SELECT shop_name, foo.shop_id, price, shop_reputation, book.isbn, image_address, book_name\n" +
                    "FROM (SELECT shop.shop_id, shop_reputation, shop_name, isbn, image_address FROM shop,\n" +
                    "book_image_address WHERE shop_reputation > 50 AND shop.shop_id=book_image_address.shop_id)\n" +
                    "AS foo, book, shop_book WHERE foo.isbn=book.isbn AND shop_book.isbn=book.isbn;";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            Field[] fields = MainShopInfo.class.getDeclaredFields();
            while (resultSet.next()) {
                MainShopInfo obj = new MainShopInfo();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(propertyToColumn.get(field.getName())));
                }
                shopInfoList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shopInfoList;
    }

    public CartShop getCartShop(String shopId, String isbn) {
        checkParameter(shopId);
        checkParameter(isbn);

        List<Element> elementList = parserMappingXml(CartShop.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, CartShop.class);
        try {
            initConnection();
            String sql = "SELECT shop_name, image_address, book_name, price FROM (SELECT shop.shop_id, shop_name,\n" +
                    "book_image_address.isbn, image_address FROM shop, book_image_address WHERE shop.shop_id=?\n" +
                    "AND shop.shop_id=book_image_address.shop_id) AS foo, shop_book, book WHERE foo.isbn=shop_book.isbn\n" +
                    "AND book.isbn=foo.isbn AND book.isbn=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, shopId);
            preparedStatement.setObject(2, Long.parseLong(isbn));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Field[] fields = CartShop.class.getDeclaredFields();
                CartShop cartShop = new CartShop();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(cartShop, resultSet.getObject(propertyToColumn.get(field.getName())));
                }

                return cartShop;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ShopIdAndISBN> getShopIdAndISBN(String userId) {
        checkParameter(userId);
        List<ShopIdAndISBN> shopIdAndISBNList = new ArrayList<>();
        List<Element> elementList = parserMappingXml(ShopIdAndISBN.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, ShopIdAndISBN.class);
        try {
            initConnection();
            String sql = "SELECT shop_id, isbn FROM add_shop_cart WHERE user_id='" + userId + "'";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            Field[] fields = ShopIdAndISBN.class.getDeclaredFields();
            while (resultSet.next()) {
                ShopIdAndISBN obj = new ShopIdAndISBN();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(propertyToColumn.get(field.getName())));
                }

                shopIdAndISBNList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shopIdAndISBNList;
    }

    // 得到店铺的所有店铺图片
    public List<String> getAllShopImageByShopId(String shopId) {
        checkParameter(shopId);
        String sql = "SELECT * FROM shop_image WHERE shop_id='" + shopId + "'";
        List<String> shopImageList = new ArrayList<>();
        try {
            initConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                shopImageList.add(resultSet.getString("image"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shopImageList;
    }

    public void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("addOrUpdate bookImageAddr parameter error.");
        }
    }

    // load Book object and book table mapping
    private static <T> List<Element> parserMappingXml(Class<T> c) {
        List<Element> list = new ArrayList<>();

        // Get current class, to get Element in mapping.xml class table mapping
        Pattern regexClass = Pattern.compile("[^\\S](.+)");
        Matcher matcherClass = regexClass.matcher(c.toString());
        String kindClass = null;
        while (matcherClass.find()) {
            kindClass = matcherClass.group(1);
        }

        File file = new File("D:/javawork/IdeaProjects/ShopBook/PropertyToColumn.xml");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(file);

            Element rootElement = document.getDocumentElement();

            NodeList nodeList = rootElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) node;

                    if (childElement.getAttribute("type").equals(kindClass)) {
                        NodeList nodeList1 = childElement.getChildNodes();
                        for (int j = 0; j < nodeList1.getLength(); j++) {
                            Node node1 = nodeList1.item(j);
                            if (node1.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) node1;
                                list.add(element);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 关闭当先数据库的连接
     */
    private void close() throws SQLException {
        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (connection != null) connection.close();
    }

    public static void main(String[] args) {
        DataBaseShop dataBaseShop = new DataBaseShop();
        List<ShopBook> shopBookList = dataBaseShop.getAllShopByShopIdAndSellerId("abc_123456", "123456");
        System.out.println(shopBookList.size());
    }
}
