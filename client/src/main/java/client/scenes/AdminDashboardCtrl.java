package client.scenes;

import client.utils.Configuration;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.stream.Collectors;

import static client.Main.switchLocale;

public class AdminDashboardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final AdminDashboardLogic logic;
    @FXML
    private ListView<Event> eventListAdmin;
    @FXML
    private ImageView currentLanguage;
    Configuration configuration;

    @Inject
    public AdminDashboardCtrl(ServerUtils server, MainCtrl mainCtrl, AdminDashboardLogic logic,
                              Configuration configuration) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.logic = logic;
        this.configuration = configuration;
    }

    public void refresh() {
        eventListAdmin.getItems().clear();
        for (Event event : server.getEvents()) {
            eventListAdmin.getItems().add(event);
        }
        Configuration configuration = new Configuration();
        String l = configuration.getLangConfig();
        String s = System.getProperty("user.dir");
        File file = new File(s + "\\client\\src\\main\\resources\\client\\images\\" + l + ".png");
        Image image = new Image(file.toURI().toString());
        currentLanguage.setImage(image);
    }

    public void refresh(Comparator<Event> comparator) {
        List<Event> sortedEvents = server.getEvents().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        eventListAdmin.setItems(FXCollections.observableArrayList(sortedEvents));
    }

    public void changeLangEn() throws BackingStoreException {
        configuration.setLangConfig("en");
        switchLocale("en");
        mainCtrl.restart(MainCtrl::adminPage);
    }

    public void changeLangNl() throws BackingStoreException {
        configuration.setLangConfig("nl");
        switchLocale("nl");
        mainCtrl.restart(MainCtrl::adminPage);
    }

    public void changeLangRo() throws BackingStoreException {
        configuration.setLangConfig("ro");
        switchLocale("ro");
        mainCtrl.restart(MainCtrl::adminPage);
    }

    public void addNewLang() {
        try {
            String saveDir = createLanguageTemplate();
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

    public String createLanguageTemplate() throws IOException {
        String path = String.format("%s/Downloads/template.properties", System.getProperty("user.home"));
        createLanguageTemplate(new FileWriter(path));
        return path;
    }

    public void createLanguageTemplate(Writer writer) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("client.language", Locale.forLanguageTag("en"));
        writer.write("# Add the name of your new language to the first line of this file as a comment!\n"+
                "# Send the final version to splittyteam32@gmail.com\n");
        for (String key : bundle.keySet()) {
            writer.write(String.format("%s = %s\n", key, bundle.getString(key)));
        }
        writer.flush();
    }

    public void onTitleClick() {

        refresh(Comparator.comparing(Event::getTitle));
    }

    public void onCreationClick() {

        refresh(Comparator.comparing(Event::findEarliestDate));
    }

    public void onLastDateClick() {

        refresh(Comparator.comparing(Event::findLatestDate));
    }

    public void backToStart() {
        mainCtrl.startPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    public void deleteSelectedEvent() {
        Event selectedEvent = eventListAdmin.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this event?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // Assuming server.deleteEvent() exists and deletes the event on the server
                    server.deleteEvent(selectedEvent.getId());
                    eventListAdmin.getItems().remove(selectedEvent);
                }
            });
        }
    }

    @FXML
    private void downloadBackupAction() {
        Event selectedEvent = eventListAdmin.getSelectionModel().getSelectedItem();
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Event Backup");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                logic.downloadBackupAction(selectedEvent, file);
            }
            else {
                showAlert("File Error", "Error saving file",
                    "Could not access this file path.", AlertType.ERROR);
            }
        } catch (IOException e) {
            showAlert("File Error", "Error saving file",
                "Could not save the file: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void selectAndUploadBackupAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Event Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null); // Replace null with your stage reference
        if (file != null) {
            try {
                logic.uploadEventSnapshotFromFile(file);

                // Refresh the UI or perform other actions as needed after successful upload
                refresh(); // Assuming refresh updates your UI with the latest data

                showAlert("Success", "Backup Uploaded",
                    "The event and associated users have been successfully restored from the backup.",
                    AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Read Error", "Error reading JSON file",
                    "Could not read the JSON file: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }
}
