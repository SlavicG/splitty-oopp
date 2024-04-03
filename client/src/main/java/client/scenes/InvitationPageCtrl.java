package client.scenes;

import client.utils.Configuration;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Mail;
import commons.dto.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mc = Executors.newVirtualThreadPerTaskExecutor();
        String s = "ABCDE";
        if(event != null) s = event.getCode();
        code.setText(s);

        Configuration configuration = new Configuration();
        server.setEmailConfig(configuration.getMailConfig());
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
    public void sendEmails() {
        String toEmails = emails_box.getText();
        if(toEmails == null || toEmails.isBlank()) {
            System.out.println("Bad email request");
            return;
        }
        String[] emails = toEmails.split(System.lineSeparator());
        for(String email : emails) {
            Mail mailRequest = new Mail(email,
                    "Invite Code to Splitty!",
                    "Here is the invite code to the Event: " + code.getText() + ".\n\n\n"
                + "Access it on the server:" + server.getServerUrl());
            mc.execute(() -> server.sendEmail(mailRequest));
            String nickName = email.split("@")[0];
            User user = new User();
            user.setName(nickName);
            System.out.println(nickName);
            user.setEmail(email);
            server.createUser(event.getId(), user);
        }
        emails_box.clear();
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
