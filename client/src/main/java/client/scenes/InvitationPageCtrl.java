package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class InvitationPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Label code;
    @FXML
    private Label eventName;
    @Inject
    public InvitationPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String s = "ABC7DE";
        code.setText(s);
    }
    public void overview() {
        mainCtrl.overviewPage();
    }
    public void setName(Label name) {
        eventName.setText(name.getText());
    }
}
