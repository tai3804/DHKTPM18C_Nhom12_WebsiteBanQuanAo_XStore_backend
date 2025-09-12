<%@ page import="iuh.fit.tuan3.bai2.Account" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 9/8/2025
  Time: 8:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Account List</title>
</head>
<body>
<h2>Account List</h2>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Firstname</th>
    <th>Lastname</th>
    <th>Email</th>
    <th>Date of Birth</th>
  </tr>
  <%
    List<Account> accounts = (List<Account>) request.getAttribute("accounts");
    for(Account acc : accounts){
  %>
  <tr>
    <td><%= acc.getId() %></td>
    <td><%= acc.getFirstName() %></td>
    <td><%= acc.getLastName() %></td>
    <td><%= acc.getEmail() %></td>
    <td><%= acc.getDateOfBirth() %></td>
  </tr>
  <% } %>
</table>
</body>
</html>
