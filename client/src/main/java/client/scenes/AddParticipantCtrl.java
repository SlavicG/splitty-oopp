package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddParticipantCtrl  {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField name;
    private String initialName;

    @Inject
    public AddParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void setName(String name) {
        this.name.setText(name);
        initialName = name;
    }
    public void abort() {
        this.name.setText(initialName);
    }
    public void overview() {
        mainCtrl.overviewPage();
    }
}
