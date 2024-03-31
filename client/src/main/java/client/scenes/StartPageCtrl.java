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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

import static client.Main.switchLocale;

public class StartPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    Configuration configuration;
    private ObservableList<Event> data;

    @FXML
    private TextField inviteCode;
    @FXML
    private TextField eventName;
    @FXML
    private ListView<Event> eventList;
    @FXML
    private Text invalidEventName;

    @Inject
    public StartPageCtrl(ServerUtils server, MainCtrl mainCtrl, Configuration configuration) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.configuration = configuration;
    }

    public void onJoin() {
        // todo: Get the event corresponding to this invite code.
        inviteCode.setText(null);
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
    public void changeLangEn() throws BackingStoreException {
        configuration.setLangConfig("en");
        switchLocale("en");

    }
    public void changeLangNl() throws BackingStoreException {
        configuration.setLangConfig("nl");
        switchLocale("nl");
    }
    public void addNewLang() throws IOException {
        Properties newLang = new Properties();
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("client/src/main/resources/client/misc/langTemplate.txt"))) {
            newLang.load(reader);
        } catch (IOException e) {
            throw e;
        }

        String path;
        try (OutputStream output = new FileOutputStream("client/src/main/resources/client/misc/newLang.properties")) {
            newLang.store(output, "Add the name of your new language to the first line of this file as a comment!\n"+
                    "Send the final version to splittyteam32@gmail.com");
            path = "client/src/main/resources/client/misc/newLang.properties";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File(path);
        String saveDir = System.getProperty("user.home") + "/Downloads/" + file.getName();
        try {
            Files.move(file.toPath(), Paths.get(saveDir), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File sucesfully downloaded to: " + saveDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAdminButton(){
        mainCtrl.loginPage();
    }
}
