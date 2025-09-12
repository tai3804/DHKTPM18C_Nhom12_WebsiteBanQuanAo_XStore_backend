<%--
  Created by IntelliJ IDEA.
  User: trant
  Date: 8/21/2025
  Time: 10:29 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h1>Home Page</h1>
    <h2>Xin chào, ${sessionScope.user}</h2>
    <a href="${pageContext.request.contextPath}/tuan1/cau5/secure/Secret.jsp">Trang bảo mật</a> <br>
    <a href="${pageContext.request.contextPath}/tuan1/cau5//tuan1/cau5/secure/logout"></a>
</body>
</html>
