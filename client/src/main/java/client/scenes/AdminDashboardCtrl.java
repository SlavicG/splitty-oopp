package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private ListView<Event> eventListAdmin;
    @Inject
    public AdminDashboardCtrl(ServerUtils server, MainCtrl mainCtrl)
    {
        this.server=server;
        this.mainCtrl=mainCtrl;
    }
    public void refresh() {
        eventListAdmin.getItems().clear();
        for (Event event : server.getEvents()) {
            eventListAdmin.getItems().add(event);
        }
    }
    public void backToStart()
    {
        mainCtrl.startPage();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }
}
