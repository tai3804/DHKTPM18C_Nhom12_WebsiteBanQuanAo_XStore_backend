<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        h2 {
            text-align: center;
        }
        table {
            margin: 0 auto;
            border-collapse: collapse;
        }
        td {
            padding: 8px 12px;
        }
        input[type="text"],
        input[type="password"],
        input[type="date"] {
            width: 220px;
            padding: 6px;
        }
        input[type="submit"] {
            display: block;
            margin: 15px auto;
            padding: 8px 16px;
            background-color: #4CAF50;
            border: none;
            color: white;
            font-size: 14px;
            cursor: pointer;
            border-radius: 4px;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<h2>User Registration Form</h2>
<form action="${pageContext.request.contextPath}/registerform" method="post">
    <table>
        <tr>
            <td>First Name:</td>
            <td><input type="text" name="firstname" required/></td>
        </tr>
        <tr>
            <td>Last Name:</td>
            <td><input type="text" name="lastname" required/></td>
        </tr>
        <tr>
            <td>Email:</td>
            <td><input type="text" name="email" required/></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" required/></td>
        </tr>
        <tr>
            <td>Birthday:</td>
            <td><input type="date" name="dob" required/></td>
        </tr>
    </table>
    <input type="submit" value="Sign Up"/>
</form>
</body>
</html>
