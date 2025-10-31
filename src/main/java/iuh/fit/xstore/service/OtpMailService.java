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

    public void sendOtpEmail(String to, String keyword, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Mã OTP của bạn");

        String htmlContent = "<div style='font-family: Arial, sans-serif;'>" +
                "<h2>OTP dùng để "+ keyword+"</h2>" +
                "<p>Đây là mã OTP dùng một lần được gửi từ website bán hàng <h3>XStore</h3> dùng để xác thực "+keyword+"</p>" +
                "<br>Mã OTP của bạn là: " +
                "<h3 style='color: blue;'>" + otp + "</h3>" +
                "<p>Mã có hiệu lực trong 5 phút.</p>" +
                "<span style='color: red;'> Vui lòng không cung cấp OTP cho bất kì ai kể cả nhân viên shop</span>" +
                "</div>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
