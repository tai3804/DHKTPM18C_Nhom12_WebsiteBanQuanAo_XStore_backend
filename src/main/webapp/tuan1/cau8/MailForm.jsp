<%--
  Created by IntelliJ IDEA.
  User: trant
  Date: 8/25/2025
  Time: 8:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/sendMail"
          method="post" enctype="multipart/form-data">
        <label>Người nhận</label>
        <input type="email" name="to" required> <br>

        <label>Tiêu đề</label>
        <input type="text" name="subject" required> <br>

        <label>Nội dung</label>
        <textarea name="content" rows="5" cols="40"></textarea> <br>

        <label>Tệp đính kèm</label>
        <input type="file" name="file"> <br><br>

        <input type="submit" value="Send">
    </form>
</body>
</html>
