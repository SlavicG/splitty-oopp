package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class AddParticipantCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField iban;
    @FXML
    private TextField bic;
    @FXML
    private TextField debt;
    @FXML
    private Label invalid;
    @FXML
    private Label title;
    @FXML
    private Button confirm;

    private Event event;
    private User user;
    private ResourceBundle resourceBundle;

    @Inject
    public AddParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEvent(Integer eventId) {
        this.event = server.getEventById(eventId);
    }

    public void setUser(Integer userId) {
        if (userId == null) {
            user = null;
            title.setText(resourceBundle.getString("add_participant"));
            confirm.setText(resourceBundle.getString("ok"));
            return;
        }
        user = server.getUserById(event.getId(), userId);
        name.setText(user.getName());
        email.setText(user.getEmail());
        iban.setText(user.getIban());
        bic.setText(user.getBic());
        title.setText(resourceBundle.getString("edit_participant"));
        confirm.setText(resourceBundle.getString("edit"));
    }

    public void onCancel() {
        mainCtrl.eventPage(event.getId());
    }

    public void onOk() {
        if (name.getText() == null || name.getText().isEmpty()) {
            invalid.setText(resourceBundle.getString("invalid_participant_name"));
            invalid.setVisible(true);
            return;
        }
        if (user == null) {
            User newUser =
                    server.createUser(event.getId(),
                        new User(null, name.getText(), email.getText(), iban.getText(), bic.getText()));
            server.send("/app/users", newUser);
            mainCtrl.addUndoFunction(() -> server.deleteUser(event.getId(), newUser.getId()));
            user = null;
        } else {
            User oldUser = new User(user);
            User changedUser = server.updateUser(event.getId(), new User(oldUser.getId(),
                    name.getText(),
                    email.getText(), iban.getText(), bic.getText()));
            server.send("/app/users", changedUser);
            mainCtrl.addUndoFunction(() -> server.updateUser(event.getId(), oldUser));
            user = null;
        }
        mainCtrl.eventPage(event.getId());
    }

    public void clear() {
        name.setText(null);
        email.setText(null);
        iban.setText(null);
        bic.setText(null);
        invalid.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
