package client.scenes;

import client.utils.Configuration;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

import static client.Main.switchLocale;

public class StartPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    Configuration configuration;
    private final StartPageLogic logic;
    private ObservableList<Event> data;

    @FXML
    private TextField inviteCode;
    @FXML
    private TextField eventName;
    @FXML
    private ListView<Event> eventList;
    @FXML
    private Text invalidEventName;
    @FXML
    private Text invalidInviteCode;

    @Inject
    public StartPageCtrl(ServerUtils server, MainCtrl mainCtrl, Configuration configuration, StartPageLogic logic) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.configuration = configuration;
        this.logic = logic;
    }

    public void refresh() {
        var events = server.getEvents();
        data = FXCollections.observableList(events);
        eventList.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        server.registerForUpdates(q -> {
            Platform.runLater(() -> {
                data.add(q);
            });
        });
    }
    public void stop() {
        server.stop();
    }

    public void onEventSelected() {
        Event selection = eventList.getSelectionModel().getSelectedItem();
        if (selection == null) {
            return;
        }
        mainCtrl.eventPage(selection.getId());
    }

    public void onJoin() {
        boolean successful = logic.useInviteCode(inviteCode.getText());
        invalidInviteCode.setVisible(successful);
    }

    public void onCreateEvent() {
        boolean successful = logic.createNewEvent(eventName.getText());
        invalidEventName.setVisible(successful);
        eventName.setText(null);
    }

    public void changeLangEn() throws BackingStoreException {
        configuration.setLangConfig("en");
        switchLocale("en");

    }

    public void changeLangNl() throws BackingStoreException {
        configuration.setLangConfig("nl");
        switchLocale("nl");
    }

    public void changeLangRo() throws BackingStoreException {
        configuration.setLangConfig("ro");
        switchLocale("ro");

    }

    public void addNewLang() {
        try {
            String saveDir = logic.createLanguageTemplate();
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Created new language template");
            alert.setHeaderText("Created new language template");
            alert.setContentText("Your new language template can be found at \"" + saveDir + "\".");
            alert.show();
        }
        catch (IOException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed creating new language template");
            alert.setHeaderText("Failed creating new language template");
            alert.setContentText("Something went wrong while creating a new language template:\n" + e);
            alert.show();
        }
    }

    public void onAdminButton(){
        mainCtrl.loginPage();
    }
}
