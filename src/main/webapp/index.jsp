<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.database.connect.DataBaseAccess" %>
<%@ page import="com.database.entity.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="com.database.entity.MainShopInfo" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Spring 在线书店</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-migrate-3.3.0.js"></script>
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.css">
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">

    <script src="https://unpkg.com/swiper/swiper-bundle.js"></script>
    <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
    <link rel="stylesheet" type="text/css" href="CSS/Main.css"/>
</head>
<body>
<div id="top">
    <div style="width: 10%"></div>
    <%
        Object obj = session.getAttribute("userLogin");
        Object obj2 = session.getAttribute("sellerLogin");
        if (obj == null && obj2 == null) {
    %>
    <div class="register">
        <a href="register.jsp">注冊</a>
    </div>
    <div class="login">
        <a href="login.jsp">登录</a>
    </div>
    <%
    } else if (obj != null) {
    %>
    <div class="shopCart">
        <a href="ShopCart.jsp">购物车</a>
    </div>
    <div class="logOut">
        <a href="/logout">退出</a>
    </div>
    <%
    } else {
    %>
    <div class="manageShop">
        <a href="manageShop.jsp">管理店铺</a>
    </div>
    <div class="logOut">
        <a href="/logout">退出</a>
    </div>
    <%
        }
    %>
</div>
<div id="search">
    <form id="inputSearchForm" method="Get" action="/search">
        <nobr>
            <label style="width: 20%">
                <input type="text" size="20" class="searchInput" id="searchInput" name="searchInput"/>
            </label>
            <button type="button" id="searchButton" class="searchButton">搜索</button>
        </nobr>
    </form>
    <script type="text/javascript">
        const button = document.getElementById("searchButton");
        const inputText = document.getElementById("searchInput");
        const form = document.getElementById("inputSearchForm");
        button.addEventListener("click", function () {
            if (inputText.value.trim().length !== 0) {
                form.submit();
            }
        })
    </script>
</div>
<div style="width: 100%; height: 10px">
</div>
<div id="menu" class="menu">
    <div class="content">
        <div class="bookKindRight">
            <div id="bookKind" class="bookKind">
                <%
                    DataBaseAccess dataBaseAccess = new DataBaseAccess();
                    for (Book book : dataBaseAccess.findTop6Book()) {
                %>
                <a href="/bookInfo?isbn=<%=book.getIsbn()%>&shopId=abc_123456">
                    <%=book.getBookName()%>
                </a>
                <div style="height: 15px"></div>
                <% } %>
            </div>
        </div>
        <div class="bookImage">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <a href="/bookInfo?isbn=9787115516756&shopId=abc_123456">
                            <img src="Images/abc_123456/unix_advance.jpg" alt="Image"/>
                        </a>
                    </div>
                    <div class="swiper-slide">
                        <a href="/bookInfo?isbn=9787115521637&shopId=abc_123456">
                            <img src="Images/abc_123456/C%20Primer%20Plus.jpg" alt="Image"/>
                        </a>
                    </div>
                    <div class="swiper-slide">
                        <a href="/bookInfo?isbn=9787115508652&shopId=abc_123456">
                            <img src="Images/abc_123456/refactor.jpg" alt="Image"/>
                        </a>
                    </div>
                </div>
                <div class="swiper-pagination"></div>
                <div class="swiper-button-prev"></div>
                <div class="swiper-button-next"></div>
                <div class="swiper-scrollbar"></div>
            </div>
        </div>
        <script type="text/javascript">
            var myswp = new Swiper('.swiper-container', {
                loop: true,
                autoplay: {
                    disableOnInteraction: false,
                    delay: 2500
                },
                pagination: {
                    el: '.swiper-pagination',
                    clickable: true
                },
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev'
                }
            });
        </script>
        <div class="news">
            <h1>Hello World!</h1>
        </div>
    </div>
</div>
<div style="width: 100%; height: 30px">
</div>
<div class="book">
    <%
        List<MainShopInfo> shopInfoList = dataBaseAccess.getMainShopInfo();
        for (MainShopInfo shopInfo : shopInfoList) {
    %>
    <div class="bookUnit">
        <div style="width: 250px">
            <a href="/bookInfo?isbn=<%=shopInfo.getIsbn() %>&shopId=<%=shopInfo.getShopId()%>">
                <img src="Images/<%=shopInfo.getShopId()%>/<%=shopInfo.getImageAddress()%>" alt="Image"/>
            </a>
        </div>
        <div class="bookName">
            <%=shopInfo.getBookName()%>
        </div>
        <div class="bookPrice">
            ￥：<%=shopInfo.getPrice()%>
        </div>
        <div class="shopName">
            店铺：<%=shopInfo.getShopName()%>
        </div>
        <div class="shopReputation">
            信誉: <%=shopInfo.getShopReputation()%>
        </div>
        <div>
            <button class="shopButton" id="addCart_<%=shopInfo.getIsbn()%>"
                    name="<%=shopInfo.getShopId()%>">添加到购物车
            </button>
            <button class="shopButton" id="buyer_<%=shopInfo.getIsbn()%>"
                    name="<%=shopInfo.getShopId()%>"
                    value="<%=shopInfo.getPrice()%>">购买
            </button>
        </div>
    </div>
    <%
        }
    %>
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
    Object registerStatus = session.getAttribute("registerStatus");
    if (registerStatus != null) {
%>
<script type="text/javascript">
    alert("注册成功!");
</script>
<% session.removeAttribute("registerStatus");
}
%>
<%
    Object obj1 = session.getAttribute("login");
    if (obj1 != null) {
%>
<script type="text/javascript">
    alert("登录成功!");
</script>
<%
    }
%>
</body>
</html>