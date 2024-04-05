package server.service;

import commons.dto.Mail;
import commons.dto.MailConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import server.Config;

@Service
public class MailService {
    public ResponseEntity<Mail> sendEmail(Mail mail) {
        if (mail == null) {
            return ResponseEntity.badRequest().build();
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(Config.getEmailUsername());
        mailMessage.setCc(Config.getEmailUsername());
        mailMessage.setTo(mail.getToMail());
        mailMessage.setText(mail.getBody());
        mailMessage.setSubject(mail.getSubject());
        Config.getJavaMailSender().send(mailMessage);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<MailConfig> configEmail(@RequestBody MailConfig mailConfig) {
        Config.changeMailSenderConfig(mailConfig);
        return ResponseEntity.ok().build();
    }
}
