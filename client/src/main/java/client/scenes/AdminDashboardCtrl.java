package client.scenes;

import client.utils.ServerUtils;
import com.google.gson.*;
import com.google.inject.Inject;
import commons.dto.*;
import commons.dto.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import commons.dto.Tag;

import java.time.LocalDate;

public class AdminDashboardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private ListView<Event> eventListAdmin;
    private static final Gson GSON = new GsonBuilder()
            // Custom serializer for LocalDate (if still needed)
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new
                    JsonPrimitive(src.toString()))
//            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
//                    LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))

            .create();
//    Gson GSON = new GsonBuilder()
//            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
//                @Override
//                public LocalDateTime deserialize(JsonElement json, Type typeOfT,
//                                                 JsonDeserializationContext context) throws JsonParseException {
//                    return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//                }
//            })
//            .create();

    @Inject
    public AdminDashboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void refresh() {
        eventListAdmin.getItems().clear();
        for (Event event : server.getEvents()) {
            eventListAdmin.getItems().add(event);
        }
    }

    public void refresh(Comparator<Event> comparator) {
        List<Event> sortedEvents = server.getEvents().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        eventListAdmin.setItems(FXCollections.observableArrayList(sortedEvents));
    }

    public void onTitleClick() {

        refresh(Comparator.comparing(Event::getTitle));
    }

    public void onCreationClick() {

        refresh(Comparator.comparing(Event::getTitle));
    }

    public void onLastDateClick() {

        refresh(Comparator.comparing(Event::getTitle));
    }

    public void backToStart() {
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
        EventSnapshot selectedEventJson = new EventSnapshot(selectedEvent.getId(), selectedEvent.getTitle(),
                selectedEvent.getUserIds(), selectedEvent.getExpenses(), selectedEvent.getTags());
        selectedEventJson.setUserList(server.getUserByEvent(selectedEvent.getId()));

        if (selectedEventJson == null) {
            showAlert("No Selection", "No event selected",
                    "Please select an event to download its backup.", AlertType.WARNING);
            return;
        }

        String eventJson = convertEventToJson(selectedEventJson);
        if (eventJson != null) {
            saveEventJsonToFile(eventJson);
        }
    }

    private String convertEventToJson(EventSnapshot event) {
        try {
            return GSON.toJson(event);
        } catch (Exception e) {
            showAlert("Conversion Error", "Error converting event to JSON",
                    "Could not convert the selected event to JSON: " + e.getMessage(), AlertType.ERROR);
            return null;
        }
    }
//    public static String convertEventToJson(Event event) {
//        return event.toJSON();
//    }

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

    @FXML
    private void selectAndUploadBackupAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Event Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(null); // Replace null with your stage reference
        if (file != null) {
            uploadEventSnapshotFromFile(file);
        }
    }

    private String readJsonFromFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            showAlert("File Error", "Error reading file",
                    "Could not read the file: " + e.getMessage(), AlertType.ERROR);
            return null;
        }
    }

    private EventSnapshot deserializeJsonToSnapshot(String json) {
        try {
            Gson gson = new Gson();
            return GSON.fromJson(json, EventSnapshot.class);
        } catch (Exception e) {
            showAlert("Conversion Error", "Error converting JSON to EventSnapshot",
                    "Could not convert the JSON to an EventSnapshot: " + e.getMessage(), AlertType.ERROR);
            return null;
        }
    }

    private void uploadEventSnapshotFromFile(File file) {
        String json = readJsonFromFile(file);

        if (json != null) {
            EventSnapshot snapshot = deserializeJsonToSnapshot(json);
            if (snapshot != null) {
                // Process the Event part of the snapshot
                // Assuming you have a method in ServerUtils to add or update an event
                Event createdevent = snapshot.getEvent();
                createdevent.setId(null);
                createdevent.setTags(new ArrayList<>());
                Event newEvent = server.addEvent(createdevent);


                // Process each User in the snapshot
                // Assuming you have a method in ServerUtils to add or update users
                Map<Integer, Integer> userMap = new HashMap<>();
                snapshot.getUserList().forEach(user ->
                {
                    User createdUser = server.createUser(newEvent.getId(),
                            new User(null, user.getName(),
                                    user.getEmail(), user.getIban(), user.getBic(), user.getDebt()));
                    userMap.put(user.getId(), createdUser.getId());
                });
                Map<Integer, Integer> tagMap = new HashMap<>();
                snapshot.getTags().forEach(
                        tag ->
                        {
                            Tag newTag = server.addTag(new Tag(null,
                                    tag.getName(), tag.getR(), tag.getG(), tag.getB(),
                                    newEvent.getId()), newEvent.getId());
                            tagMap.put(tag.getId(), newTag.getId());
                        });
                List<Expense> expenses = snapshot.getExpenses();
                snapshot.getExpenses().forEach(expense ->
                {
                    Expense expense1 = server.addExpense(new Expense(null, expense.getAmount(),
                            expense.getDescription(), userMap.get(expense.getPayerId()), expense.getDate(),
                            expense.getSplitBetween(), tagMap.get(expense.getTagId())), newEvent.getId());
                });

                // Refresh the UI or perform other actions as needed after successful upload
                refresh(); // Assuming refresh updates your UI with the latest data

                showAlert("Success", "Backup Uploaded",
                        "The event and associated users have been successfully restored from the backup.",
                        AlertType.INFORMATION);
            }
        }
    }


}
