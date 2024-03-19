package server.api;

import commons.dto.Mail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.service.MailService;

@Controller
@RequestMapping("/rest")
public class MailController {
    private MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/mail")
    public ResponseEntity<Mail> sendMail(@RequestBody Mail mail) {
        return mailService.sendEmail((mail));
    }
}
