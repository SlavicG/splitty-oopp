package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class EditEventNameCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField name;
    @FXML
    private Label invalid;

    private Event event;
    private ResourceBundle resourceBundle;

    @Inject
    public EditEventNameCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEvent(Integer eventId) {
        this.event = server.getEventById(eventId);
        name.setText(event.getTitle());
    }

    public void onCancel() {
        mainCtrl.eventPage(event.getId());
    }

    public void onOk() {
        if (name.getText() == null || name.getText().isEmpty()) {
            invalid.setText(resourceBundle.getString("invalid_name"));
            invalid.setVisible(true);
            return;
        }
        Event oldEvent = new Event(event);
        Event changedEvent = server.updateEvent(new Event(oldEvent.getId(), name.getText(),
                oldEvent.getUserIds(),oldEvent.getExpenses(),oldEvent.getTags()));
        server.send("/app/events", changedEvent);
        mainCtrl.addUndoFunction(() -> server.updateEvent(oldEvent));
        mainCtrl.eventPage(event.getId());
    }

    public void clear() {
        name.setText(null);
        invalid.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
