package iuh.fit.tuan3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2025 by trant.
 * All rights reserved.
 * This file is part of tranthanhtai_22673121.
 */

@WebServlet("/tuan3/student")
public class StudentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest rep, HttpServletResponse res) throws ServletException, IOException {
        Student student = new Student();

        String fName = rep.getParameter("firstName");
        String lName = rep.getParameter("lastName");
        String dobStr = rep.getParameter("dob");
        LocalDate dob = LocalDate.parse(dobStr);
        String email = rep.getParameter("email");
        String mobile = rep.getParameter("mobile");
        String gender = rep.getParameter("gender");
        String address = rep.getParameter("address");
        String city = rep.getParameter("city");
        String pinCode = rep.getParameter("pinCode");
        String state = rep.getParameter("state");
        String country = rep.getParameter("country");
        String hobby = rep.getParameter("hobbies");
        String board10 = rep.getParameter("board10");
        Float per10 = Float.parseFloat(rep.getParameter("percent10"));
        int year10 = Integer.parseInt(rep.getParameter("year10"));
        String board12 = rep.getParameter("board12");
        Float per12 = Float.parseFloat(rep.getParameter("percent12"));
        int year12 = Integer.parseInt(rep.getParameter("year12"));
        String course = rep.getParameter("course");

        student.setId("student1");
        student.setFirstName(fName);
        student.setLastName(lName);
        student.setDob(dob);
        student.setEmail(email);
        student.setMobile(mobile);
        student.setGender(gender);
        student.setAddress(address);
        student.setCity(city);
        student.setPinCode(pinCode);
        student.setState(state);
        student.setCountry(country);
        student.setHobbies(hobby);

        Qualification qua10 = new Qualification("1", "Class 10", board10, per10, year10);
        Qualification qua12= new Qualification("2", "Class 12", board12, per12, year12);

        List<Qualification> quas =  new ArrayList<Qualification>();
        quas.add(qua10);
        quas.add(qua12);

        student.setQualifications(quas);
        student.setCourse(course);

        rep.setAttribute("student", student);
        rep.getRequestDispatcher("StudentResult.jsp").forward(rep, res);
    }
}
