package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.Mail;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

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
    @Inject
    public InvitationPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String s = "ABCDE";
        if(event != null) s = event.getCode();
        code.setText(s);
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
        String toEmail = emails_box.getText();
        if(toEmail == null || toEmail.isBlank()) {
            System.out.println("Bad email request");
            return;
        }
        Mail mailRequest = new Mail(toEmail,
                "Invite Code to Splitty!",
                "Here is the invite code to the Event: " + code.getText());
        server.sendEmail(mailRequest);
        emails_box.clear();
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
