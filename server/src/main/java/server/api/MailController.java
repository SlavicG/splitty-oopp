package server.api;

import commons.dto.Mail;
import commons.dto.MailConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @ResponseBody
    @PostMapping("/mail/config")
    public ResponseEntity<MailConfig> configEmail(@RequestBody MailConfig mailConfig) {
        return mailService.configEmail(mailConfig);
    }
}
