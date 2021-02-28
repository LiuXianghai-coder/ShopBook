package com.database.connect;

import com.database.entity.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class DataBaseAccess implements DataBaseConnectDAO {
    private GetBookInfo getBookInfo = new GetBookInfo();
    private DataBaseShop dataBaseShop = new DataBaseShop();

    @Override
    public List<Book> findTop6Book() {
        String sql       = "SELECT * FROM book LIMIT 6";
        return getBookInfo.getBooks(sql, null, Book.class);
    }

    @Override
    public int addUser(List<User> userList) {
        return getBookInfo.insertQuery("shop_user", userList, User.class);
    }

    @Override
    public String queryUserIdByUserId(String userId) {
        return getBookInfo.queryUserByUserId(userId);
    }

    @Override
    public String querySellerIdBySellerId(String sellerId) {
        return getBookInfo.querySellerBySellerId(sellerId);
    }

    @Override
    public User queryUserByIdAndPass(String userId, String userPassword){
        return getBookInfo.queryUserByIdAndPass(userId, userPassword);
    }

    @Override
    public Seller querySellerByIdAndPass(String userId, String userPassword){
        return getBookInfo.querySellerByIdAndPass(userId, userPassword);
    }

    @Override
    public List<Shop> queryAllShopBySellerId(String sellerId) {
        return dataBaseShop.getAllShopBySellerId(sellerId);
    }

    public List<BookInfo> queryBookInfoByIsbn(String isbn) {
        try {
            return getBookInfo.getBookInfoByIsbn(isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ShopBook> getAllShopBookByShopIdAndSellerId(String shopId, String sellerId) {
        return dataBaseShop.getAllShopByShopIdAndSellerId(shopId, sellerId);
    }

    @Override
    public ShopBook getShopBookByShopIdAndIsbn(String shopId, String isbn) {
        return dataBaseShop.getShopBookByShopIdAndIsbn(shopId, isbn);
    }

    @Override
    public int insertBookInfo(List<BookInfo> bookInfoList) {
        return getBookInfo.insertBookInfo(bookInfoList);
    }

    @Override
    public String getImageAddressByShopIdAndIsbn(String shopId, String isbn) {
        return dataBaseShop.getImageAddressByShopIdAndIsbn(shopId, isbn);
    }

    @Override
    public List<BookShow> getAllBookShowByShopIdAndSellerId(String shopId, String sellerId) {
        if (shopId == null || shopId.trim().length() == 0) {
            throw new IllegalArgumentException("shopId parameter error");
        }
        if (sellerId == null || sellerId.trim().length() == 0) {
            throw new IllegalArgumentException("sellerId parameter error");
        }

        String sql = "SELECT  Distinct bookInfo.isbn, price, book_name, publisher_name, publisher_date," +
                " publisher_id, image_address FROM\n" +
                "(SELECT shop_id, price, book_name, shopBook.isbn, publisher_name,\n" +
                "       publisher_date, publisher_id FROM (\n" +
                "SELECT book.isbn, price, book_name, shop_id FROM\n" +
                "(SELECT foo.shop_name, foo.shop_id, foo.shop_create_date,\n" +
                "        foo.shop_reputation, shop_book.isbn, shop_book.price\n" +
                "FROM (SELECT shop.shop_id,shop.shop_name,shop.shop_create_date,\n" +
                "            shop.shop_reputation FROM shop, shop_seller WHERE\n" +
                "            shop.seller_id=?)\n" +
                "AS foo, shop_book WHERE foo.shop_id=?) AS shopInfo,\n" +
                "book WHERE book.isbn=shopInfo.isbn) AS shopBook,\n" +
                "(SELECT isbn,publisher_name,publisher_date,\n" +
                "        publisher.publisher_id FROM book_publisher, publisher\n" +
                "WHERE book_publisher.publisher_id=publisher.publisher_id) AS\n" +
                "    publisherBook WHERE publisherBook.isbn=shopBook.isbn) AS bookInfo, book_image_address\n" +
                "WHERE bookInfo.shop_id=book_image_address.shop_id AND bookInfo.isbn=book_image_address.isbn;";
        Object[] objects = {sellerId, shopId};

        return dataBaseShop.getAllBookByShopIdAndSellerId(sql, objects);
    }

    @Override
    public int deleteShopByShopIdAndIsbn(String shopId, String isbn) {
        try {
            return dataBaseShop.deleteShopByShopIdAndIsbn(shopId, isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deleteBookImageByShopIdAndIsbn(String shopId, String isbn) {
        try {
            return dataBaseShop.deleteBookImageByShopIdAndIsbn(shopId, isbn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int insertIntoBookImageAddr(String shopId, String isbn, String fileName) {
       checkParameter(shopId);
       checkParameter(isbn);
       checkParameter(fileName);

       String sql = "INSERT INTO book_image_address VALUES (?, ?, ?) " +
               "ON CONFLICT(shop_id, isbn, image_address) DO NOTHING";
       Object[] objects = {shopId, Long.parseLong(isbn), fileName};
       return dataBaseShop.operatingDataBase(sql, objects);
    }

    @Override
    public int insertIntoShopBook(String shopId, String isbn,
                                  String bookPrice, String bookDescribe) {
        checkParameter(shopId);
        checkParameter(isbn);
        checkParameter(bookPrice);

        if (bookDescribe == null) {
            bookDescribe = "";
        }

        String sql = "INSERT INTO shop_book(shop_id, isbn, price, describe) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT (shop_id, isbn) DO UPDATE SET price=?, describe=?;";
        Object[] objects = {shopId, Long.parseLong(isbn), Double.parseDouble(bookPrice),
                bookDescribe, Double.parseDouble(bookPrice), bookDescribe};

        return dataBaseShop.operatingDataBase(sql, objects);
    }

    @Override
    public boolean insertBook(String bookIsbn, String bookName, String[] bookAuthor,
                              String bookPublisher, String publisherDate) {
        checkParameter(bookIsbn);
        checkParameter(bookName);
        checkParameter(bookPublisher);
        checkParameter(publisherDate);
        for (String s: bookAuthor) {
            checkParameter(s);
        }

        try {
            return dataBaseShop.insertBook(bookIsbn, bookName,
                    bookAuthor, bookPublisher, publisherDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void takeNoteUploadInfo(String sellerId, String isbn,
                                   String shopId, String uploadDate) {
        try {
            dataBaseShop.takeNoteUploadInfo(sellerId, isbn, shopId, uploadDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllShopImageByShopId(String shopId) {
        return dataBaseShop.getAllShopImageByShopId(shopId);
    }

    @Override
    public int addShop(String shopId, String sellerId, String imageAddress,
                       String shopName, String shopCreateDate, Double reputation) {
        int countShop   = 0;
        int countImage  = 0;
        String insertIntoShop      = "INSERT INTO shop(shop_id, shop_name, shop_create_date, " +
                "seller_id, shop_reputation) VALUES (?, ?, ?, ?, ?) ON CONFLICT(shop_id) DO NOTHING ";
        Object[] objects = new Object[]{shopId, shopName, Date.valueOf(shopCreateDate), sellerId, reputation};
        if ((countShop = dataBaseShop.controlDataBase(insertIntoShop, objects)) > 0) {
            System.out.println("count size: " + countShop);
            String insertIntoShopImage = "INSERT INTO shop_image(shop_id, image) VALUES (?, ?) " +
                    "ON CONFLICT(shop_id, image) DO NOTHING";
            objects = new Object[]{shopId, imageAddress};

            countImage += dataBaseShop.operatingDataBase(insertIntoShopImage, objects);
        }

        return countImage >0 ?1:0;
    }

    @Override
    public List<MainShopInfo> getMainShopInfo() {
        return dataBaseShop.getMainShopInfo();
    }

    @Override
    public int addBookToCart(String userId, String shopId, String isbn) {
        checkParameter(userId);
        checkParameter(shopId);
        checkParameter(isbn);

        String sql = "INSERT INTO add_shop_cart(user_id, shop_id, isbn) VALUES (?, ?, ?)\n" +
                "ON CONFLICT (user_id, shop_id, isbn) DO NOTHING";
        Object[] objects = new Object[]{userId, shopId, Long.parseLong(isbn)};

        return dataBaseShop.operatingDataBase(sql, objects);
    }

    @Override
    public int addPurchaseRecord(String userId, String shopId, String price, String detail) {
        checkParameter(userId);
        checkParameter(shopId);
        checkParameter(price);
        checkParameter(detail);

        String  sql    =    "INSERT INTO purchase(user_id, shop_id, purchase_price, " +
                "transaction_detail) VALUES (?, ?, ?, ?)";
        Object[] objects    =   new Object[]{userId, shopId, Double.parseDouble(price), detail};
        return dataBaseShop.operatingDataBase(sql, objects);
    }

    @Override
    public CartShop getCartShop(String shopId, String isbn) {
        return dataBaseShop.getCartShop(shopId, isbn);
    }

    @Override
    public List<ShopIdAndISBN> getShopAndISBN(String userId) {
        return dataBaseShop.getShopIdAndISBN(userId);
    }

    @Override
    public int deleteBookOnCart(String userId, String shopId, String isbn) {
        checkParameter(userId);
        checkParameter(shopId);
        checkParameter(isbn);

//        System.out.println(userId + "\t" + shopId + "\t" + isbn);

        String sql = "DELETE FROM add_shop_cart WHERE user_id=? AND shop_id=? AND isbn=?";
        Object[] objects = new Object[]{userId, shopId, Long.parseLong(isbn)};
        return dataBaseShop.operatingDataBase(sql, objects);
    }

    @Override
    public List<SearchResult> getSearchResultByInput(String searchInput) {
        checkParameter(searchInput);
        if (isIsbn(searchInput)) {
            return dataBaseShop.getSearchResult(Long.parseLong(searchInput));
        } else {
            return dataBaseShop.getSearchResult(searchInput);
        }
    }

    private boolean isIsbn(String searchInput) {
        Pattern pattern = Pattern.compile("\\d+");
        return pattern.matcher(searchInput).matches();
    }

    @Override
    public <T>int addUserAndSeller(T obj, Class<T> C) {
        return getBookInfo.addUserAndSeller(obj, C);
    }

    public void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("addOrUpdate bookImageAddr parameter error.");
        }
    }

    public static void main(String[] args) {
        DataBaseAccess dataBaseAccess = new DataBaseAccess();
        List<BookShow> bookShowList = dataBaseAccess.
                getAllBookShowByShopIdAndSellerId("abc_123456", "123456");
        for (BookShow bookShow: bookShowList) {
            System.out.println(bookShow.getBook().toString());
            System.out.println(bookShow.getPublisher().toString());
            System.out.println("Book Price: " + bookShow.getPrice());
            System.out.println("Book ImageAddress: " + bookShow.getImageAddress());
        }
    }
}
