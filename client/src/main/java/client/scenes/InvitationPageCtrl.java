package client.scenes;

import client.utils.Configuration;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Mail;
import commons.dto.MailConfig;
import commons.dto.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Label code;
    @FXML
    private Label eventName;
    @FXML
    private TextArea emails_box;
    @FXML
    private Event event;
    @FXML
    Button send_emails;
    private Executor mc;
    @Inject
    public InvitationPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    private static JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    public void setMailConfig(MailConfig mailConfig) {
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setPassword(mailConfig.getPassword());
        mailSender.setUsername(mailConfig.getUsername());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailConfig.getProps().getProperty("mail.transport.protocol"));
        props.put("mail.smtp.auth", mailConfig.getProps().getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", mailConfig.getProps().getProperty("mail.smtp.starttls.enable"));
        props.put("mail.debug", mailConfig.getProps().getProperty("mail.debug"));
    }
    public void sendEmail(Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSender.getUsername());
        mailMessage.setCc(mailSender.getUsername());
        mailMessage.setTo(mail.getToMail());
        mailMessage.setText(mail.getBody());
        mailMessage.setSubject(mail.getSubject());
        mailSender.send(mailMessage);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mc = Executors.newVirtualThreadPerTaskExecutor();
        String s = "ABCDE";
        if(event != null) s = event.getCode();
        code.setText(s);

        Configuration configuration = new Configuration();;
        if(configuration.getMailConfig() == null) return;

        setMailConfig(configuration.getMailConfig());
        Mail mailRequest = new Mail(configuration.getMailConfig().getUsername(),
                "Testing E-mail Configuration",
                "Test to check if E-mails are being sent.");
        mc.execute(() -> sendEmail(mailRequest));
    }
    public void overview() {
        mainCtrl.overviewPage();
    }
    public void setName(Label name) {
        eventName.setText(name.getText());
    }
    public void setEvent(Integer eventid) {
        this.event = server.getEventById(eventid);
    }

    boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void sendEmails() throws InterruptedException {
        Configuration configuration = new Configuration();
        if(configuration.getMailConfig() == null) return;
        String toEmails = emails_box.getText();
        if(toEmails == null || toEmails.isBlank()) {
            System.out.println("Bad email request");
            return;
        }
        String[] emails = toEmails.split(System.lineSeparator());
        var validEmails = new ArrayList<>();
        var invalidEmails = new ArrayList<>();
        for(String email : emails) {
            if(isValidEmail(email)) {
                validEmails.add(email);
                Mail mailRequest = new Mail(email,
                        "Invite Code to Splitty!",
                        "Here is the invite code to the Event: " + code.getText() + ".\n\n\n"
                                + "Access it on the server:" + server.getServerUrl());
                mc.execute(() -> sendEmail(mailRequest));
                String nickName = email.split("@")[0];
                User user = new User();
                user.setName(nickName);
                System.out.println(nickName);
                user.setEmail(email);
                server.createUser(event.getId(), user);
            } else {
                invalidEmails.add(email);
            }
        }
        emails_box.clear();
        String res = "Emails succesfully sent to the valid Emails: \n";
        for(var email: validEmails) {
            res += email + "\n";
        }
        res += "\n\n\nEmails not sent to:\n";
        for(var email: invalidEmails) {
            res += email + "\n";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Email Sending Result");
        alert.setHeaderText(null);
        alert.setContentText(res);
        alert.showAndWait();

        mainCtrl.overviewPage();
    }
    public void clear() {
        eventName.setText(null);
    }
    public void refresh() {
        eventName.setText(eventName.getText());
        String s = "ABCDE";
        if(event != null) s = event.getCode();
        code.setText(s);
    }
}
