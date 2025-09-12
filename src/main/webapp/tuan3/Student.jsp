<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Registration Form</title>
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
        label {
            font-weight: bold;
        }
        input, textarea, select {
            width: 100%;
            padding: 6px;
            margin: 5px 0 15px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .row {
            display: flex;
            gap: 20px;
        }
        .row > div {
            flex: 1;
        }
        .radio-group, .checkbox-group {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
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
            text-align: center;
        }
        tr {
            padding: 8px;
        }
        .actions {
            text-align: center;
            margin-top: 20px;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            margin: 0 5px;
        }
        .btn-submit {
            background: #007bff;
            color: #fff;
        }
        .btn-reset {
            background: #6c757d;
            color: #fff;
        }
        .w-80 {
            width: 88% !important;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Student Registration Form</h2>
    <form action="student" method="get">
        <div class="row">
            <div>
                <label>First Name</label>
                <input type="text" name="firstName" required>
            </div>
            <div>
                <label>Last Name</label>
                <input type="text" name="lastName" required>
            </div>
        </div>

        <label>Date of Birth</label>
        <input type="date" name="dob" required>

        <div class="row">
            <div>
                <label>Email</label>
                <input type="email" name="email" required>
            </div>
            <div>
                <label>Mobile</label>
                <input type="text" name="mobile" required>
            </div>
        </div>

        <label>Gender</label>
        <div class="radio-group">
            <label><input type="radio" name="gender" value="Male"> Male</label>
            <label><input type="radio" name="gender" value="Female"> Female</label>
        </div>

        <label>Address</label>
        <textarea name="address" rows="3"></textarea>

        <div class="row">
            <div>
                <label>City</label>
                <input type="text" name="city">
            </div>
            <div>
                <label>Pin Code</label>
                <input type="text" name="pinCode">
            </div>
        </div>

        <div class="row">
            <div>
                <label>State</label>
                <input type="text" name="state">
            </div>
            <div>
                <label>Country</label>
                <input type="text" name="country" value="India">
            </div>
        </div>

        <label>Hobbies</label>
        <div class="checkbox-group">
            <label><input type="checkbox" name="hobbies" value="Drawing"> Drawing</label>
            <label><input type="checkbox" name="hobbies" value="Singing"> Singing</label>
            <label><input type="checkbox" name="hobbies" value="Dancing"> Dancing</label>
            <label><input type="checkbox" name="hobbies" value="Sketching"> Sketching</label>
            <label><input type="checkbox" name="hobbies" value="Others"> Others</label>
        </div>

        <h3>Qualification</h3>
        <table>
            <tr>
                <th>Sl.No</th>
                <th>Examination</th>
                <th>Board</th>
                <th>Percentage</th>
                <th>Year of Passing</th>
            </tr>
            <tr>
                <td>1</td>
                <td>Class X</td>
                <td><input class="w-80" type="text" name="board10"></td>
                <td><input class="w-80" type="text" name="percent10"></td>
                <td><input class="w-80" type="text" name="year10"></td>
            </tr>
            <tr>
                <td>2</td>
                <td>Class XII</td>
                <td><input class="w-80" type="text" name="board12"></td>
                <td><input class="w-80" type="text" name="percent12"></td>
                <td><input class="w-80" type="text" name="year12"></td>
            </tr>
        </table>

        <label>Course applies for</label>
        <div class="radio-group">
            <label><input type="radio" name="course" value="BCA"> BCA</label>
            <label><input type="radio" name="course" value="B.Com"> B.Com</label>
            <label><input type="radio" name="course" value="B.Sc"> B.Sc</label>
            <label><input type="radio" name="course" value="B.A"> B.A</label>
        </div>

        <div class="actions">
            <button type="submit" class="btn btn-submit">Submit</button>
            <button type="reset" class="btn btn-reset">Reset</button>
        </div>
    </form>
</div>
</body>
</html>
