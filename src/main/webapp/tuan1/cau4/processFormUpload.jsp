<%--Tuan1 - Cau 4--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #2f3542; /* nền xám đậm */
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        form {
            background-color: #3d3d3d; /* nền form */
            padding: 30px;
            border-radius: 8px;
            width: 400px;
            box-shadow: 0px 4px 10px rgba(0,0,0,0.3);
            color: white;
        }

        h2 {
            text-align: left;
            margin-bottom: 20px;
            color: #fff;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: bold;
        }

        /* --- chỉnh Name thành 2 ô --- */
        .name-group {
            display: flex;
            gap: 10px; /* khoảng cách giữa 2 ô */
            margin-bottom: 15px;
        }

        .name-group input {
            flex: 1; /* mỗi ô chiếm 50% */
            padding: 10px;
            border: none;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="url"],
        textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: none;
            border-radius: 4px;
            box-sizing: border-box;
        }

        textarea {
            resize: none;
        }

        button {
            width: 100%;
            background-color: #1e90ff;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0d6efd;
        }

        div {
            display: flex;
            justify-content: space-between;
        }
        #firstname,
        #lastname{
            width: 49%;
        }
    </style>
</head>
<body>
<form action="${pageContext.request.contextPath}/tuan1/cau4/process-form-upload" method="post">
    <h2>Register</h2>

    <label>Name *</label>
    <div>
        <input type="text" name="firstname" id="firstname" required>
        <input type="text" name="lastname" id="lastname" required>
    </div>


    <label>Username *</label>
    <input type="text" name="username" required>

    <label>E-mail *</label>
    <input type="email" name="email" required>

    <label>Password *</label>
    <input type="password" name="password" required>

    <label>Facebook</label>
    <input type="url" name="facebook">

    <label>Short Bio</label>
    <textarea name="bio" rows="3"></textarea>

    <button type="submit">Submit</button>
</form>
</body>
</html>
