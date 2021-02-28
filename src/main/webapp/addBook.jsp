<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/20
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>添加新的商品</title>
    <meta charset="utf-8"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-migrate-3.3.0.js"></script>
    <link rel="stylesheet" type="text/css" href="CSS/addBook.css"/>
</head>
<body>
<div class="formArea">
    <form id="addBookForm" method="post" action="/addBook" enctype="multipart/form-data">
        <input type="hidden" id="shopId" name="shopId" value="<%=request.getParameter("shopId")%>" />
        <input type="hidden" id="isbn"   name="isbn" value="<%=request.getParameter("isbn")%>" />
        <div class="inputLayout">
            ISBN&emsp;&emsp;:
            <input type="number" maxlength="15" id="bookIsbn" name="bookIsbn"
                   class="inputStyle" required />
        </div>
        <div class="inputLayout">
            书名&emsp;&emsp;:
            <input type="text" id="bookName" name="bookName" class="inputStyle" required />
        </div>
        <div class="inputLayout">
            作者名&emsp;:
            <input type="text" id="authorName" name="authorName" placeholder="多个作者请以'#'号分隔"
                   class="inputStyle" required />
        </div>
        <div class="inputLayout">
            出版社&emsp;:
            <input type="text" id="publisherName" name="publisherName" class="inputStyle" required />
        </div>
        <div class="inputLayout">
            出版日期:
            <input type="date" id="publisherDate" name="publisherDate" class="inputStyle" required />
        </div>
        <div class="inputLayout">
            价格&emsp;&emsp;:
            <input type="text" id="bookPrice" name="bookPrice" class="inputStyle" required />
        </div>
        <div class="textArea">
            <div class="inputLayout">对于书的描述 :</div>
            <div>
                <textarea cols="65" rows="15" id="bookDescribe" name="bookDescribe">
                </textarea>
            </div>
        </div>
        <div style="height: 20px; width: 100%;"></div>
        <div id="uploadFile">
            <div class="imageUpload">
                上传图片&emsp;:&emsp;<input type="file" id="bookImage_0" name="bookImage_0" class="fileUploadInput"/>
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
            const element = $('<div class="imageUpload">上传图片&emsp;:&emsp;<input type="file" ' +
                'class="fileUploadInput" id="bookImage_"' + i + ' name="book_image_"' + i + '/></div>');
            element.appendTo($("#uploadFile"));
            i++;
        });
        $('#uploadButton').click(function () {
            const bookIsbn      = $('#bookIsbn').val();
            const bookName      = $('#bookName').val();
            const bookPrice     = $('#bookPrice').val();
            const bookDescribe  = $('#bookDescribe').val();
            const uploadImage   = $('#bookImage_0').val();
            if (bookIsbn.trim().length === 0) {
                alert("请输入书的ISBN");
            }else if (bookName.trim().length === 0) {
                alert("请输入书名。");
            } else if (bookPrice.trim().length === 0) {
                alert("请输入书的价格。");
            } else if (uploadImage.trim().length === 0) {
                alert("请选择上传书籍的图像。");
            } else if (bookDescribe.trim().length === 0) {
                const con = confirm("你还没有输入对于书的描述， 你确定要提交吗?");
                if (con === true) {
                    $("#addBookForm").submit();
                }
            } else {
                $("#addBookForm").submit();
            }
        });
    </script>
</div>
</body>
</html>