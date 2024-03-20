package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class StartPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField inviteCode;
    @FXML
    private TextField eventName;
    @FXML
    private ListView<Event> eventList;
    @FXML
    private Text invalidEventName;

    @Inject
    public StartPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void onJoin() {
        // todo: Get the event corresponding to this invite code.
        inviteCode.setText(null);
    }

    public void refresh() {
        eventList.getItems().clear();
        for (Event event : server.getEvents()) {
            eventList.getItems().add(event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    public void onEventSelected() {
        Event selection = eventList.getSelectionModel().getSelectedItem();
        if (selection == null) {
            return;
        }
        mainCtrl.eventPage(selection.getId());
    }

    public void onCreateEvent() {
        // Make sure the event name isn't blank.
        // This is probably better checked server-side, but this will do for now.
        if (eventName.getText().isEmpty()) {
            invalidEventName.setVisible(true);
            return;
        }
        invalidEventName.setVisible(false);
        Event event = new Event();
        event.setTitle(eventName.getText());
        Event finalEvent = server.addEvent(event);
        mainCtrl.eventPage(finalEvent.getId());
        eventName.setText(null);
    }
    public void onAdminButton(){
        mainCtrl.loginPage();
    }
}
