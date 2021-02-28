package com.database.connect;

import com.database.entity.*;
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
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetBookInfo {
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

    /**
     * 得到指定isbn的作者
     *
     * @param isbn: 　书籍的isbn号
     * @return List<Author>: 指定书籍的作者对象的列表
     */
    private List<Author> getBookAuthorsByIsbn(String isbn) throws SQLException {
        String getAuthorSql;
        if (isbn == null || isbn.trim().length() == 0) {
            getAuthorSql = "WITH book_authors AS\n" +
                    "    (select book.isbn,book.book_name,\n" +
                    "            author.author_name, author.author_id from book, book_belong, author\n" +
                    "            where book.isbn=book_belong.isbn\n" +
                    "              and book_belong.book_author_id= author.author_id),\n" +
                    "book_publishers AS (\n" +
                    "         select book_publisher.isbn, publisher.publisher_name,\n" +
                    "                publisher_date from book_publisher,publisher\n" +
                    "         where book_publisher.publisher_id=publisher.publisher_id\n" +
                    "     )\n" +
                    "SELECT author_id, author_name FROM (SELECT book_authors.isbn, " +
                    "book_authors.book_name,book_authors.author_id,\n" +
                    "       book_authors.author_name, book_publishers.publisher_name,\n" +
                    "       book_publishers.publisher_date FROM book_authors, book_publishers\n" +
                    "WHERE book_authors.isbn=book_publishers.isbn) AS foo";
        } else {
            getAuthorSql = "WITH book_authors AS\n" +
                    "    (select book.isbn,book.book_name,\n" +
                    "            author.author_name, author.author_id from book, book_belong, author\n" +
                    "            where book.isbn=book_belong.isbn\n" +
                    "              and book_belong.book_author_id= author.author_id),\n" +
                    "book_publishers AS (\n" +
                    "         select book_publisher.isbn, publisher.publisher_name,\n" +
                    "                publisher_date from book_publisher,publisher\n" +
                    "         where book_publisher.publisher_id=publisher.publisher_id\n" +
                    "     )\n" +
                    "SELECT author_id, author_name FROM (SELECT book_authors.isbn, " +
                    "book_authors.book_name,book_authors.author_id,\n" +
                    "       book_authors.author_name, book_publishers.publisher_name,\n" +
                    "       book_publishers.publisher_date FROM book_authors, book_publishers\n" +
                    "WHERE book_authors.isbn=book_publishers.isbn) AS foo WHERE isbn=" + isbn;
        }
        List<Element> elementList = parseBookMapping(Author.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, Author.class);

        initConnection();
        List<Author> authors = new ArrayList<>();
        try {
            preparedStatement = this.connection.prepareStatement(getAuthorSql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Author author = new Author();
                Field[] fields = Author.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(author, resultSet.
                            getObject(propertyToColumn.get(field.getName())));
                }

                authors.add(author);
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return authors;
    }

    /**
     * 得到指定isbn的书籍的对应出版社
     *
     * @param isbn: 书籍的isbn号
     * @return List<Publisher>: 返回当前书籍对应的所有的出版社和出版日期
     * @throws SQLException : 如果连接关闭出现异常
     */
    public List<Publisher> getBookPublisherByIsbn(String isbn) throws SQLException {
        String sql;

        if (isbn == null || isbn.trim().length() == 0) {
            sql = "select * from (select book.book_name, book_publisher.*, publisher_name from" +
                    " book,book_publisher,publisher where\n" +
                    "book.isbn=book_publisher.isbn and book_publisher.publisher_id=publisher.publisher_id)\n" +
                    "as foo";
        } else {
            sql = "select * from (select book.book_name, book_publisher.*, publisher_name from" +
                    " book,book_publisher,publisher where\n" +
                    "book.isbn=book_publisher.isbn and book_publisher.publisher_id=publisher.publisher_id)\n" +
                    "as foo where foo.isbn=" + isbn;
        }
        List<Element> elementList = parseBookMapping(Publisher.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, Publisher.class);

        initConnection();
        List<Publisher> publisherList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Publisher obj = new Publisher();
                Field[] fields = Publisher.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (Integer.class.equals(field.getType())) {
                        field.set(obj, resultSet.getInt(propertyToColumn.get(field.getName())));
                    } else if (java.sql.Date.class.equals(field.getType())) {
                        field.set(obj, resultSet.getDate(propertyToColumn.get(field.getName())));
                    } else {
                        field.set(obj, resultSet.getString(propertyToColumn.get(field.getName())));
                    }
                }

                publisherList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return publisherList;
    }


    /**
     * 得到isbn指定的书籍
     *
     * @param isbn: 需要查询的书籍的isbn
     * @return List<Book>: 返回一个包含查找到的书籍的列表
     * @throws SQLException: 如果关闭连接出现异常
     */
    public List<Book> getBookByIsbn(String isbn) throws SQLException {
        String sql;
        if (isbn == null || isbn.trim().length() == 0) {
            sql = "SELECT * FROM book";
        } else {
            sql = "SELECT * FROM book WHERE isbn=" + isbn;
        }
        List<Element> elementList = parseBookMapping(Book.class);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, Book.class);

        initConnection();
        List<Book> bookList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book obj = new Book();
                Field[] fields = Book.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (Long.class.equals(field.getType())) {
                        field.set(obj, resultSet.getLong(propertyToColumn.get(field.getName())));
                    } else {
                        field.set(obj, resultSet.getString(propertyToColumn.get(field.getName())));
                    }
                }

                bookList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return bookList;
    }

    /**
     * 查询对应的操作
     * @param sql: 指定查询的SQL语句
     * @param data:对指定SQL语句进行一个占位符的值的设置
     * @param C: 所对应查询的泛型的类型
     * @param <T>: 泛型T
     * @return List<T>: 返回对应的泛型列表
     */
    public <T> List<T> getBooks(String sql, Object[] data, Class<T> C) {
        if (sql == null ||sql.trim().length() == 0) {
            throw new IllegalArgumentException("query sql error.");
        }

        List<Element> elementList = parseBookMapping(C);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, C);

        initConnection();
        List<T> list = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (data != null) {
                int index = 1;
                for (Object object : data) {
                    preparedStatement.setObject(index++, object);
                }
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T obj = C.newInstance();
                Field[] fields = C.getDeclaredFields();
                for (Field field: fields) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(propertyToColumn.get(field.getName())));
                }
                list.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 更新数据
     * @param sql: 执行的SQL语句
     * @param data: 对SQL语句占位符的设置参数
     * @param C: 泛型T的class
     * @param <T>: 泛型T
     * @return int: 成功更新记录的条数， 如果出现异常， 返回-1
     */
    public <T> int updateQuery(String sql, Object[] data, Class<T> C) {
        if (sql == null || sql.trim().length() == 0) {
            throw new IllegalArgumentException("update sql error");
        }

        initConnection();
        int updateRows = -1;         // 更新数据的条数
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (data != null) {
                int index = 1;
                for (Object object : data) {
                    preparedStatement.setObject(index++, object);
                }
            }

            updateRows = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateRows;
    }

    public String queryUserByUserId(String userId) {
        String sql = "SELECT user_id FROM shop_user WHERE user_id='" + userId + "'";
        initConnection();
        try {
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

    public String querySellerBySellerId(String sellerId) {
        String sql = "SELECT seller_id FROM shop_seller WHERE seller_id='" + sellerId + "'";
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

    public <T> int insertQuery(String tableName, List<T> list, Class<T> C) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(");

        List<Element> elementList = parseBookMapping(C);
        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, C);

        Field[] fields1 = C.getDeclaredFields();
        for (Field field: fields1) {
            sql.append(propertyToColumn.get(field.getName())).append(", ");
        }

        sql.deleteCharAt(sql.length() - 2);
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") VALUES ");

        for (T t: list) {
            Field[] fields = C.getDeclaredFields();
            StringBuilder values = new StringBuilder("(");
            for (Field field: fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(t);
                    values.append("'").append(value).append("', ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            values.deleteCharAt(values.length() - 2);
            values.deleteCharAt(values.length() - 1);
            values.append(")");
            sql.append(values);
        }

        initConnection();
        try {
            preparedStatement = connection.prepareStatement(String.valueOf(sql));
            System.out.println(sql );
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     *  通过指定的isbn查询书籍的信息， 如果为null或空则查询所有书籍的信息
     * @param isbn: 指定书籍的isbn， null或空则查询所有
     * @return List<BookInfo>: 返回查找到的书籍
     * @throws SQLException: 有连接关闭异常出现则抛出
     */
    public List<BookInfo> getBookInfoByIsbn(String isbn) throws SQLException {
        List<BookInfo> bookInfoList = new ArrayList<>();
        if (isbn == null || isbn.trim().length() == 0) {
            List<Book> bookList = getBookByIsbn(null);
            for (Book book: bookList) {
                BookInfo obj = new BookInfo(book, getBookAuthorsByIsbn(String.valueOf(book.getIsbn())),
                        getBookPublisherByIsbn(String.valueOf(book.getIsbn())));
                bookInfoList.add(obj);
            }
        } else {
            bookInfoList.add(new BookInfo(getBookByIsbn(isbn).get(0), getBookAuthorsByIsbn(isbn),
                    getBookPublisherByIsbn(isbn)));
        }
        close();

        return bookInfoList;
    }

    public int insertBookInfo(List<BookInfo> bookInfos) {
        int count = 0;
        for (BookInfo bookInfo: bookInfos) {
            insertBook(bookInfo.getBook());
            insertAuthor(bookInfo.getBookAuthor());
            insertPublisher(bookInfo.getBookPublisher());
            insertBookPublisher(bookInfo.getBook(), bookInfo.getBookPublisher());
            count++;
        }

        return count;
    }

    private int insertBook(Book book) {
        String sql = "INSERT INTO book VALUES(?, ?) ON CONFLICT (?) DO UPDATE SET book_name=?";
        Object[] objects = {book.getIsbn(), book.getBookName(), book.getIsbn(), book.getBookName()};
        return updateQuery(sql, objects, Book.class);
    }

    private int insertAuthor(List<Author> authors) {
        int count = 0;
        initConnection();
        for (Author author: authors) {
            String sql = "INSERT INTO author(author_name) VALUES (" + author.getAuthorName() + ")"
                    + "ON CONFLICT DO UPDATE SET author_name=" + author.getAuthorName();
            try {
                preparedStatement = connection.prepareStatement(sql);
                count += preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private int insertPublisher(List<Publisher> publisherList) {
        int count = 0;
        initConnection();
        for (Publisher publisher: publisherList) {
            String sql = "INSERT INTO publisher(publisher_name) VALUES (" + publisher.getPublisherName() + ")"
                    + "ON CONFLICT DO UPDATE SET publisher_name=" + publisher.getPublisherName();
            try {
                preparedStatement = connection.prepareStatement(sql);
                count += preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private int insertBookPublisher(Book book, List<Publisher> publisherList) {
        int count = 0;
        initConnection();
        for (Publisher publisher: publisherList) {
            String sql = "INSERT INTO book_publisher VALUES(" + book.getIsbn() + ", " + publisher.getPublisherId() +
                    ", " + publisher.getPublisherName() + "," + publisher.getPublisherDate() + ")";
            try {
                preparedStatement = connection.prepareStatement(sql);
                count += preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * 解析对应的BookInfo对象的关系映射
     *
     * @param C: 指定的要获取映射的对象类型
     * @return List<Element>:　指定对象的属性映射元素集合
     */
    private <T> List<Element> parseBookMapping(Class<T> C) {
        List<Element> elementList = new ArrayList<>();
        File file = new File("D:/javawork/IdeaProjects/ShopBook/BookMapping.xml");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (C.getName().equals(element.getAttribute("type"))) {
                        NodeList nodeList1 = node.getChildNodes();
                        for (int j = 0; j < nodeList1.getLength(); j++) {
                            Node node1 = nodeList1.item(j);
                            if (node1.getNodeType() == Node.ELEMENT_NODE) {
                                Element element1 = (Element) node1;
                                elementList.add(element1);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return elementList;
    }

    private <T> Map<String, String> getPropertyToColumn(List<Element> elementList, Class<T> C) {
        Map<String, String> propertyToColumn = new HashMap<>();
        for (Element element : elementList) {
            propertyToColumn.put(element.getAttribute("property"),
                    element.getAttribute("column"));
        }

        return propertyToColumn;
    }

    public Seller querySellerByIdAndPass(String userId, String userPassword){
        String sql = "SELECT * FROM shop_seller WHERE seller_id='" + userId +
                "' AND seller_password='" + userPassword + "'";

        List<Element> elementList = parseBookMapping(Seller.class);

        Map<String, String> propertyToColumn = getPropertyToColumn(elementList, Seller.class);
        initConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Seller seller = Seller.class.newInstance();
                Field[] fields = Seller.class.getDeclaredFields();
                for (Field field: fields) {
                    field.setAccessible(true);
                    field.set(seller, resultSet.getObject(propertyToColumn.get(field.getName())));
                }

                return seller;
            }
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

    public User queryUserByIdAndPass(String userId, String userPassword){
        String sql = "SELECT * FROM shop_user WHERE user_id='" + userId +
                "' AND user_password='" + userPassword + "'";

        List<Element> elementList = parseBookMapping(User.class);
        Map<String, String> propertyToColumn = new HashMap<>();
        for (Element element : elementList) {
            propertyToColumn.put(element.getAttribute("property"),
                    element.getAttribute("column"));
        }

        initConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = User.class.newInstance();
                Field[] fields = User.class.getDeclaredFields();
                for (Field field: fields) {
                    field.setAccessible(true);
                    field.set(user, resultSet.getObject(propertyToColumn.get(field.getName())));
                }

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> int insertQuery(List<T> objects, Class<T> c) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + getTableName(c) + "(");

        // get mapping
        List<Element> mappingList = parserMappingXml(c);
        for (Element element: mappingList) {
            sql.append(element.getAttribute("column")).append(", ");
        }

        sql.deleteCharAt(sql.length() - 2);
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") ");
        sql.append("VALUES ");

        Field[] fields = c.getDeclaredFields();
        for (T obj: objects) {
            StringBuilder values = new StringBuilder("(");
            for (Field field: fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    values.append("'").append(value).append("'").append(", ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            values.deleteCharAt(values.length() - 2);
            values.deleteCharAt(values.length() - 1);
            values.append("), ");
            sql.append(values);
        }

        sql.deleteCharAt(sql.length() - 2);
        sql.deleteCharAt(sql.length() - 1);
        sql.append(";");
//        System.out.println(sql);

        try {
            initConnection();
            preparedStatement = connection.prepareStatement(String.valueOf(sql));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return 0;
    }

    // load Book object and book table mapping
    private static <T>List<Element> parserMappingXml(Class<T> c) {
        List<Element> list = new ArrayList<>();

        // Get current class, to get Element in mapping.xml class table mapping
        Pattern regexClass = Pattern.compile("[^\\S](.+)");
        Matcher matcherClass = regexClass.matcher(c.toString());
        String kindClass = null;
        while (matcherClass.find()) {
            kindClass = matcherClass.group(1);
        }

        File file = new File("mapping.xml");
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

    public <T> int addUserAndSeller(T obj , Class<T> C) {
        StringBuilder sql;
        if (C.equals(User.class)) {
            sql = new StringBuilder("INSERT INTO shop_user (");
        } else if (C.equals(Seller.class)) {
            sql = new StringBuilder("INSERT INTO shop_seller (");
        } else {
            throw new IllegalArgumentException("不支持的添加对象");
        }

        List<Element> elementList = parseBookMapping(C);
        Map<String, String> propertyToColumn = new HashMap<>();
        for (Element element : elementList) {
            propertyToColumn.put(element.getAttribute("property"),
                    element.getAttribute("column"));
        }

        Field[] fields = C.getDeclaredFields();
        for (Field field: fields) {
            sql.append(propertyToColumn.get(field.getName())).append(", ");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") VALUES (");

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                Object object = field.get(obj);
                sql.append("'").append(object).append("', ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");

        initConnection();
        try {
            preparedStatement = connection.prepareStatement(String.valueOf(sql));
            System.out.println(sql);
            return preparedStatement.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取指定xml中配置文件的表名
     * @param c
     * @param <T>
     * @return
     */
    private static <T>String getTableName(Class<T> c) {
        // Get current class, to get Element in mapping.xml class table mapping
        Pattern regexClass = Pattern.compile("[^\\S](.+)");
        Matcher matcherClass = regexClass.matcher(c.toString());
        String kindClass = null;
        while (matcherClass.find()) {
            kindClass = matcherClass.group(1);
        }

        File file = new File("D:/javawork/IdeaProjects/ShopBook/PropertyToColumn.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList nodeList = document.getElementsByTagName("beans");
            for (int  i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList list = node.getChildNodes();

                for (int j = 0; j < list.getLength(); j++) {
                    Node node1 = list.item(j);
                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node1;

                        assert kindClass != null;
                        if (Objects.equals(kindClass, element.getAttribute("type")))
                            return element.getAttribute("table");
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 关闭当先数据库的连接
     */
    private void close() throws SQLException {
        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (connection != null) connection.close();
    }

    public static void main(String[] args) throws SQLException {
        GetBookInfo getBookInfo = new GetBookInfo();
        List<BookInfo> bookInfoList = getBookInfo.getBookInfoByIsbn("9787111407010");
        for (BookInfo bookInfo: bookInfoList) {
            System.out.println("Book:");
            System.out.println(bookInfo.getBook().toString());
            System.out.println("Book author: ");
            for (Author author: bookInfo.getBookAuthor()) {
                System.out.println(author.toString());
            }
            System.out.println("Book publisher: ");
            for (Publisher publisher: bookInfo.getBookPublisher()) {
                System.out.println(publisher.toString());
            }
        }
    }
}
