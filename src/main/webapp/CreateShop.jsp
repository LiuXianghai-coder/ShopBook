<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/20
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>创建新的店铺</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-migrate-3.3.0.js"></script>
    <link rel="stylesheet" type="text/css" href="CSS/CreateShop.css">
</head>
<body>
<div class="formArea">
    <form id="createShopForm" action="createShop"
          method="post" enctype="multipart/form-data">
        <div class="inputLayout">
            店铺名字:
            <input type="text" id="shopName" name="shopName"
                   class="inputStyle" required/>
        </div>
        <div style="height: 20px; width: 100%;"></div>
        <div id="uploadFile">
            <div>
                上传图片:<input type="file" id="bookImage_0"
                            name="bookImage_0" class="fileUploadInput"/>
            </div>
        </div>
        <div style="height: 20px; width: 100%;"></div>
        <div class="buttonArea">
            <button type="reset" id="reset">重置</button>
            <button type="button" id="uploadButton">提交</button>
            <button type="button" id="addFileUpload">增加</button>
        </div>
    </form>
    <script type="text/javascript">
        let i = 1;
        let click = $("#addFileUpload").click(function () {
            const element = $('<div>上传图片:<input type="file" ' +
                'class="fileUploadInput" id="bookImage_"' + i + ' name="book_image_"' + i + '/></div>');
            element.appendTo($("#uploadFile"));
            i++;
        });
        $('#uploadButton').click(function () {
            $('#createShopForm').submit();
        });
    </script>
</div>
</body>
</html>
