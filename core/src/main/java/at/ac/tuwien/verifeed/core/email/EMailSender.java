package at.ac.tuwien.verifeed.core.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EMailSender {

    @Value("${mail.confirmation.subject}")
    private String subject;

    @Value("${mail.confirmation.sender}")
    private String sender;

    @Autowired
    private final JavaMailSender mailSender;

    public EMailSender(JavaMailSender sender) {
        this.mailSender = sender;
    }

    public void send(String recipient, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(content, true);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setFrom(sender);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }
}
