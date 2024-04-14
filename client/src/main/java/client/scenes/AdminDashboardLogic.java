package client.scenes;

import client.utils.ServerUtils;
import com.google.gson.*;
import com.google.inject.Inject;
import commons.dto.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AdminDashboardLogic {
    private final ServerUtils server;

    private static final Gson GSON = new GsonBuilder()
        .serializeNulls()
        // Custom serializer for LocalDate (if still needed)
        .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new
            JsonPrimitive(src.toString()))
        .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
            LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .create();

    @Inject
    public AdminDashboardLogic(ServerUtils server) {
        this.server = server;
    }

    public void downloadBackupAction(Event selectedEvent, File file) throws IOException {
        EventSnapshot selectedEventJson = new EventSnapshot(selectedEvent.getId(), selectedEvent.getTitle(),
            selectedEvent.getUserIds(), selectedEvent.getExpenses(), selectedEvent.getTags());
        selectedEventJson.setUserList(server.getUserByEvent(selectedEvent.getId()));

        String eventJson = GSON.toJson(selectedEventJson);
        if (eventJson != null) {
            try (Writer writer = new FileWriter(file)) {
                writer.write(eventJson);
            }
        }
    }

    public void uploadEventSnapshotFromFile(File file) throws IOException {
        String json = Files.readString(file.toPath());

        if (json != null) {
            EventSnapshot snapshot = GSON.fromJson(json, EventSnapshot.class);
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
                snapshot.getExpenses().forEach(expense ->
                {
                    server.addExpense(new Expense(null, expense.getAmount(),
                        expense.getDescription(), userMap.get(expense.getPayerId()), expense.getDate(),
                        expense.getSplitBetween(), tagMap.get(expense.getTagId())), newEvent.getId());
                });
            }
        }
    }
}
