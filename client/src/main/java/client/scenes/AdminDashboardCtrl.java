package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import com.google.gson.Gson;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    public void refresh(Comparator<Event> comparator)
    {
        List<Event> sortedEvents = server.getEvents().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        eventListAdmin.setItems(FXCollections.observableArrayList(sortedEvents));
    }
    public void onTitleClick()
    {

        refresh(Comparator.comparing(Event :: getTitle));
    }
    public void onCreationClick()
    {

        refresh(Comparator.comparing(Event :: getTitle));
    }
    public void onLastDateClick()
    {

        refresh(Comparator.comparing(Event :: getTitle));
    }
    public void backToStart()
    {
        mainCtrl.startPage();
    }
//    public void onEventSelected() {
//        Event selection = eventListAdmin.getSelectionModel().getSelectedItem();
//        if (selection == null) {
//            return;
//        }
//        mainCtrl.eventPage(selection.getId());
//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }
    public void deleteSelectedEvent()
    {
        Event selectedEvent = eventListAdmin.getSelectionModel().getSelectedItem();
        if(selectedEvent!=null)
        {
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
        if (selectedEvent == null) {
            showAlert("No Selection", "No event selected",
                    "Please select an event to download its backup.", AlertType.WARNING);
            return;
        }

        String eventJson = convertEventToJson(selectedEvent);
        if (eventJson != null) {
            saveEventJsonToFile(eventJson);
        }
    }

    private String convertEventToJson(Event event) {
        try {
            Gson gson = new Gson();
            return gson.toJson(event);
        } catch (Exception e) {
            showAlert("Conversion Error", "Error converting event to JSON",
                    "Could not convert the selected event to JSON: " + e.getMessage(), AlertType.ERROR);
            return null;
        }
    }

    private void saveEventJsonToFile(String json) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Event Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (Writer writer = new FileWriter(file)) {
                writer.write(json);
                showAlert("Success", "Backup Saved",
                        "Event backup saved successfully!", AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("File Error", "Error saving file",
                        "Could not save the file: " + e.getMessage(), AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
