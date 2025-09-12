<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Registration Result</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f7f7f7;
        }
        .container {
            width: 800px;
            margin: 30px auto;
            background: #fff;
            padding: 25px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
        }
        table, th, td {
            border: 1px solid #999;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        .section-title {
            margin-top: 20px;
            font-weight: bold;
            color: #007bff;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Student Registration Result</h2>
    <p class="section-title">Personal Details</p>
    <table>
        <tr>
            <th>First Name</th>
            <td>${student.getFirstName()}</td>
        </tr>
        <tr>
            <th>Last Name</th>
            <td>${student.getLastName()}
            </td>
        </tr>
        <tr>
            <th>Date of Birth</th>
            <td>${student.getDob()}</td>
        </tr>
        <tr>
            <th>Email</th>
            <td>${student.getEmail()}</td>
        </tr>
        <tr>
            <th>Mobile</th>
            <td>${student.getMobile() }</td>
        </tr>
        <tr>
            <th>Gender</th>
            <td>${ student.getGender()}</td>
        </tr>
        <tr>
            <th>Address</th>
            <td>${ student.getAddress() }, ${ student.getCity() }, ${ student.getState() }, ${ student.getCountry() } - ${ student.getPinCode() }</td>
        </tr>
        <tr>
            <th>Hobbies</th>
            <td>${ student.getHobbies() }</td>
        </tr>
    </table>

    <p class="section-title">Qualifications</p>
    <table>
        <tr>
            <th>Sl.No</th>
            <th>Examination</th>
            <th>Board</th>
            <th>Percentage</th>
            <th>Year of Passing</th>
        </tr>
        <c:forEach var="q" items="${student.qualifications}">

        <tr>
            <td>${ q.getId() }</td>
            <td>${ q.getExamination() }</td>
            <td>${ q.getBorad() }</td>
            <td>${ q.getPercentage() }</td>
            <td>${ q.getYear() }</td>
        </tr>
        </c:forEach>
    </table>

    <p class="section-title">Course Applied</p>
    <p>${ student.getCourse() }</p>
</div>
</body>
</html>
