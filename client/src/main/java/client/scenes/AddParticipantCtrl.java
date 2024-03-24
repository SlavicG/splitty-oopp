package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import commons.dto.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label invalid;

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
            return;
        }
        user = server.getUserById(userId);
        name.setText(user.getName());
        email.setText(user.getEmail());
        iban.setText(user.getIban());
        bic.setText(user.getBic());
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
                    server.addUsers(new User(null, name.getText(), email.getText(), iban.getText(), bic.getText()));
            server.addUserToEvent(event, event.getId(), newUser.getId());
            server.send("/app/users", newUser);
            user = null;
        } else {
            User changedUser = server.updateUser(new User(user.getId(),
                    name.getText(),
                    email.getText(), iban.getText(), bic.getText()), user.getId());
            server.send("/app/users", changedUser);
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
