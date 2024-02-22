package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class OverviewPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label from;
    @FXML
    private Label including;
    @FXML
    private ChoiceBox<String> box;
    @Inject
    public OverviewPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    private String[] users = {"name1", "name2", "name3"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        box.getItems().addAll(users);
        box.setOnAction(this::getUser);
    }

    private void getUser(javafx.event.ActionEvent actionEvent) {
        String user = box.getValue();
        from.setText("From "+user);
        including.setText("Including " + user);
    }

}
