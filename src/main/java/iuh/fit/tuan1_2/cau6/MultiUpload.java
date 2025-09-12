package iuh.fit.tuan1_2.cau6;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/tuan1/cau6/multi-upload")
@MultipartConfig
public class MultiUpload extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/tuan1/cau6/MultiUpload.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        Collection<Part> parts = req.getParts();
        parts.forEach(part -> {
            try {
                String fileName = part.getSubmittedFileName();
                part.write(uploadPath + File.separator + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        resp.getWriter().println("File uploaded successfully, File was saved at: " + uploadPath + File.separator);
    }
}
