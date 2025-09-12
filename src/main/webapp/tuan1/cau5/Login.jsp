<%--
  Created by IntelliJ IDEA.
  User: trant
  Date: 8/21/2025
  Time: 10:13 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <form action="${pageContext.request.contextPath}/tuan1/cau5/login" method="post">
        <title>Đăng nhập</title>

        <div>
            <label>Tên đăng nhập:</label>
            <input type="text" name="username">
        </div>
        <div>
            <label>Mật khẩu: </label>
            <input type="password" name="password">
        </div>
        <button>Đăng nhập</button>

        <p style="color:red">
            <%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %>
        </p>
    </form>

</head>
<body>

</body>
</html>
