package iuh.fit.xstore.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpMailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Mã OTP của bạn");

        String htmlContent = "<div style='font-family: Arial, sans-serif;'>" +
                "<h2>Mã OTP của bạn</h2>" +
                "<p>Đây là mã OTP dùng một lần để xác thực:</p>" +
                "<h3 style='color: blue;'>" + otp + "</h3>" +
                "<p>Mã có hiệu lực trong 5 phút.</p>" +
                "</div>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
