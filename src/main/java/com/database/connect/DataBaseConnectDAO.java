package com.database.connect;

import com.database.entity.*;

import java.util.List;

public interface DataBaseConnectDAO {
    List<Book> findTop6Book();
    int addUser(List<User> userList);
    String queryUserIdByUserId(String userId);
    String querySellerIdBySellerId(String sellerId);
    User queryUserByIdAndPass(String userId, String userPassword);
    <T>int addUserAndSeller(T obj, Class<T> C);
    Seller querySellerByIdAndPass(String userId, String userPassword);
    List<Shop> queryAllShopBySellerId(String sellerId);
    List<BookInfo> queryBookInfoByIsbn(String isbn);
    List<ShopBook> getAllShopBookByShopIdAndSellerId(String shopId, String sellerId);
    ShopBook getShopBookByShopIdAndIsbn(String shopId, String isbn);
    int insertBookInfo(List<BookInfo> bookInfoList);
    String getImageAddressByShopIdAndIsbn(String shopId, String isbn);
    List<BookShow> getAllBookShowByShopIdAndSellerId(String shopId, String sellerId);
    int deleteShopByShopIdAndIsbn(String shopId, String isbn);
    int deleteBookImageByShopIdAndIsbn(String shopId, String isbn);
    int insertIntoBookImageAddr(String shopId, String isbn, String fileName);
    int insertIntoShopBook(String shopId, String isbn, String bookPrice, String bookDescribe);
    boolean insertBook(String bookIsbn, String bookName,
                       String[] bookAuthor, String bookPublisher, String publisherDate);
    void takeNoteUploadInfo(String sellerId, String isbn,
                            String shopId, String uploadDate);
    List<String> getAllShopImageByShopId(String shopId);
    int addShop(String shopId, String sellerId, String imageAddress ,String shopName,
                String shopCreateDate, Double reputation);
    List<MainShopInfo> getMainShopInfo();
    int addBookToCart(String userId, String shopId, String isbn);
    int addPurchaseRecord(String userId, String shopId, String price, String detail);
    CartShop getCartShop(String shopId, String isbn);
    List<ShopIdAndISBN> getShopAndISBN(String userId);
    int deleteBookOnCart(String userId, String shopId, String isbn);
    List<SearchResult> getSearchResultByInput(String searchInput);
}
