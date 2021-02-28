<%@ page import="com.database.entity.ShopBook" %>
<%@ page import="com.database.connect.DataBaseAccess" %>
<%@ page import="com.database.entity.Author" %>
<%@ page import="com.database.entity.Publisher" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/21
  Time: 16:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍信息</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-migrate-3.3.0.js"></script>
    <link rel="stylesheet" type="text/css" href="CSS/BookInfo.css"/>
</head>
<body>
<div class="book">
    <div class="bookUnit">
        <%
            DataBaseAccess dataBaseAccess = new DataBaseAccess();
            String shopId = request.getParameter("shopId");
            String isbn = request.getParameter("isbn");
            ShopBook shopBook = dataBaseAccess.getShopBookByShopIdAndIsbn(shopId, isbn);
            String imageAddress = dataBaseAccess.getImageAddressByShopIdAndIsbn(shopId, isbn);
            if (shopBook != null) {
        %>
        <div>
            <img src="Images/<%=shopId%>/<%=imageAddress%>" alt="Image"/>
        </div>
        <div class="bookName">
            <%=shopBook.getBookInfo().getBook().getBookName()%>
        </div>
        <div class="bookPrice">
            ￥：<%=shopBook.getPrice()%>
        </div>
        <div class="bookAuthor">
            <%
                for (Author author : shopBook.getBookInfo().getBookAuthor()) {
            %>
            <div>
                作者:&emsp;<%=author.getAuthorName()%>
            </div>
            <%
                }
            %>
        </div>
        <div class="shopReputation">
            <%
                for (Publisher publisher : shopBook.getBookInfo().getBookPublisher()) {
            %>
            <div style="width: 100%;">
                出版社&emsp;:&emsp;<%=publisher.getPublisherName()%>
            </div>
            <div>
                出版日期:&emsp;<%=publisher.getPublisherDate()%>
            </div>
            <%
                }
            %>
        </div>
        <div>
            <button class="shopButton" id="addCart_<%=isbn%>"
                    name="<%=shopId%>">添加到购物车
            </button>
            <button class="shopButton" id="buyer_<%=isbn%>"
                    name="<%=shopId%>"
                    value="<%=shopBook.getPrice()%>">购买
            </button>
        </div>
    </div>
    <div class="describe">
        <%=shopBook.getDescribe()%>
    </div>
    <script type="text/javascript">
        let click = $('button').click(function () {
            const str = this.id;
            const typeStr = str.match(/[a-z|A-Z]+/g);
            if (String("addCart").valueOf() === String(typeStr).valueOf()) {
                <%
                    Object object = session.getAttribute("login");
                    if (object == null) {
                %>
                const con = confirm("你还没有登录呢， 要去登录吗？");
                if (con === true) {
                    location.href = "/login.jsp";
                }
                <%
                    } else {
                        Object user    =  session.getAttribute("userId");
                        Object seller  =  session.getAttribute("sellerId");
                        String userId  =  user != null ? (String) user: (String)seller;
                %>
                const isbn = str.match(/\d+/g);
                $.ajax({
                    method: "Get",
                    url: "/addBookToCart",
                    data: {
                        userId: <%=userId%>, shopId: String(this.name).valueOf(),
                        isbn: String(isbn).valueOf()
                    },
                    success: function (responseText) {
                        const result = parseInt(responseText);
                        if (result > 0) {
                            alert("添加成功！");
                        } else {
                            alert("添加失败！");
                        }
                    }
                });
                <%
                    }
                %>
            } else if (String("buyer").valueOf() === String(typeStr).valueOf()) {
                <%
                   object = session.getAttribute("login");
                   if (object == null) {
               %>
                const con = confirm("你还没有登录呢， 要去登录吗？");
                if (con === true) {
                    location.href = "/login.jsp";
                }
                <%
                    } else {
                        Object user    =  session.getAttribute("userId");
                        Object seller  =  session.getAttribute("sellerId");
                        String userId  =  user != null ? (String) user: (String)seller;
                %>
                const isbn = str.match(/\d+/g);
                const date = new Date();
                const year = date.getFullYear(); //获取当前年份
                const mon = date.getMonth() + 1; //获取当前月份
                const da = date.getDate(); //获取当前日
                const day = date.getDay(); //获取当前星期几
                const h = date.getHours(); //获取小时
                const m = date.getMinutes(); //获取分钟
                const s = date.getSeconds(); //获取秒
                const transactionDate = year + '年' + mon + '月' + da + '日'
                    + '星期' + day + ' ' + h + ':' + m + ':' + s;
                $.ajax({
                    method: "Get",
                    url: "/purchaseBook",
                    data: {
                        userId: <%=userId %>, shopId: String(this.name).valueOf(), price: this.value,
                        detail: "交易书籍的ISBN: " + String(isbn).valueOf() + "\t交易时间: " + transactionDate
                    },
                    success: function (responseText) {
                        const result = parseInt(responseText);
                        if (result > 0) {
                            alert("购买成功！");
                        } else {
                            alert("购买失败！");
                        }
                    }
                });
                <%
                }
                %>
            }
        });
    </script>
    <%
        }
    %>
</div>
</body>
</html>
