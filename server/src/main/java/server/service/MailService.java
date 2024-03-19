package server.service;

import commons.dto.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    public ResponseEntity<Mail> sendEmail(Mail mail) {
        if (mail == null) {
            return ResponseEntity.badRequest().build();
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("splittyteam32@gmail.com");
        mailMessage.setTo(mail.getToMail());
        mailMessage.setText(mail.getBody());
        mailMessage.setSubject(mail.getSubject());
        mailSender.send(mailMessage);
        return ResponseEntity.ok().build();
    }
}
