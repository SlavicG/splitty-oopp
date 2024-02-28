package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class OverviewPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label from;
    @FXML
    private Label eventName;
    @FXML
    private HBox participants;
    @FXML
    private Label including;
    @FXML
    private ChoiceBox<String> box;
    @Inject
    public OverviewPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    public void startPage() {
        mainCtrl.showOverview();
    }

    private String[] users = {"name1", "name2", "name3"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        box.getItems().addAll(users);
        box.setOnAction(this::getUser);
        if (users.length > 0) {
            Button button = new Button(users[0]);
            button.setBackground(null);
            button.setOnAction((e)->{
                editParticipant(users[0]);
            });
            participants.getChildren().add(button);
        }
        for (int i = 1; i < users.length; i++){
            Button button = new Button(users[i]);
            button.setBackground(null);
            int index = i;
            button.setOnAction((e)->{
                editParticipant(users[index].toString());
            });
            participants.getChildren().add(button);
        }

        eventName.setText("Party");
    }

    public void getUser(javafx.event.ActionEvent actionEvent) {
        String user = box.getValue();
        from.setText("From "+user);
        including.setText("Including " + user);

    }
    public void invitation() {
        mainCtrl.invitationPage(eventName);
    }
    public void addParticipant() {
        mainCtrl.addParticipantPage();
    }
    public void editParticipant(String name) {
        mainCtrl.editParticipantPage();
        mainCtrl.setParticipantName(name);
    }



}
