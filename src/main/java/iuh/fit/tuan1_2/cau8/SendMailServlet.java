package iuh.fit.tuan1_2.cau8;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebServlet("/sendMail")
@MultipartConfig
public class SendMailServlet extends HttpServlet {
    private final String USERNAME = "tranthanhtai1928@gmail.com";
    private final String PASSWORD = "mcip scdx zdxh lait";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String to = req.getParameter("to");
        String subject = req.getParameter("subject");
        String content = req.getParameter("content");

        Part filePart =  req.getPart("file");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }

            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, "utf-8");

            MimeBodyPart textPart =  new MimeBodyPart();
            textPart.setText(content, "utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            if (filePart != null && filePart.getSize() > 0) {
                MimeBodyPart attachPart = new MimeBodyPart();
                String fileName = filePart.getSubmittedFileName();
                InputStream fileContext = filePart.getInputStream();
                attachPart.setFileName(fileName);
                attachPart.setDataHandler(new DataHandler(new ByteArrayDataSource(fileContext, getServletContext().getMimeType(fileName))));
                multipart.addBodyPart(attachPart);

                message.setContent(multipart);


                //send mail
                Transport.send(message);
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println("<h3>Gửi mail thành công!</h3>");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
