//em không tạo được kết nối :((
package iuh.fit.tuan1_2.cau7;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
@WebServlet("/tuan1/cau7/uploaddatabase")
@MultipartConfig
public class UploadDB extends HttpServlet {
    private String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=UploadFileServletDB;encrypt=false;trustServerCertificate=true;";
    private String dbUser = "sa";
    private String dbPass = "sapassword";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");

        System.out.println(firstName + lastName);

        InputStream inputStream = null;
        Part filePart = req.getPart("photo");
        if (filePart != null) {
            inputStream = filePart.getInputStream();
        }

        Connection conn = null;
        String message = null;

        System.out.println(conn);

        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

            System.out.println(conn);
            String sql = "INSERT INTO contacts (first_name, last_name, photo) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, firstName);
            statement.setString(2, lastName);

            if (inputStream != null) {
                statement.setBinaryStream(3, inputStream, (int) filePart.getSize());
            } else {
                statement.setNull(3, Types.BLOB);
            }

            int row = statement.executeUpdate();
            if (row > 0) {
                message = "Upload and save into database successfully!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Error: " + e.getMessage();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
        }

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(message);
    }
}
