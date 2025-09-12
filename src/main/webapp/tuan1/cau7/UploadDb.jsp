<%--
  Created by IntelliJ IDEA.
  User: trant
  Date: 8/23/2025
  Time: 3:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Up load DB</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/tuan1/cau7/uploaddatabase"
          method="post" enctype="multipart/form-data">
        First name: <input type="text" name="firstname"><br>
        Last name: <input type="text" name="lastname"><br>
        Portrait PhotoL <input type="file" name="photo"><br>
        <input type="submit" value="save">

    </form>

</body>
</html>
