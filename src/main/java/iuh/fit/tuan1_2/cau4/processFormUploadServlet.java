//tuan 1 - Cau 4
package iuh.fit.tuan1_2.cau4;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException; 

@WebServlet("/tuan1/cau4/process-form-upload")
public class processFormUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fname = req.getParameter("firstname");
        String lname = req.getParameter("lastname");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fb = req.getParameter("facebook");
        String bio =  req.getParameter("bio");

        System.out.println("firstname:"+fname);
        System.out.println("lastname:"+lname);
        System.out.println("username:"+username);
        System.out.println("email:"+email);
        System.out.println("password:"+password);
        System.out.println("fb:"+fb);
        System.out.println("bio:"+bio);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/tuan1/cau4/processFormUpload.jsp").forward(req,resp);
    }
}
