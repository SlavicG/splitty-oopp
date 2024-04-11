package client.scenes;

import client.utils.ServerUtils;
import com.google.gson.JsonParser;
import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.Tag;
import commons.dto.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class AdminDashboardTest {
    private final String testJson = """
                    {
                        "userList": [
                            {
                                "id": 1,
                                "name": "mik",
                                "email": null,
                                "iban": null,
                                "bic": null,
                                "debt": 0
                            },
                            {
                                "id": 2,
                                "name": "superbos",
                                "email": null,
                                "iban": null,
                                "bic": null,
                                "debt": 0
                            }
                        ],
                        "id": 1,
                        "title": "test",
                        "usersIds": [
                            1,
                            2
                        ],
                        "expenses": [
                            {
                                "id": 1,
                                "amount": 50,
                                "description": "test expense",
                                "payerId": 1,
                                "tagId": 1,
                                "splitBetween": [
                                    1
                                ],
                                "date": "2024-04-11"
                            },
                            {
                                "id": 2,
                                "amount": 6,
                                "description": "test expense 2",
                                "payerId": 2,
                                "tagId": 2,
                                "splitBetween": [
                                    1,
                                    2
                                ],
                                "date": "2024-04-10"
                            }
                        ],
                        "tags": [
                            {
                                "id": 1,
                                "name": "food",
                                "r": 255,
                                "g": 255,
                                "b": 0,
                                "eventId": 1
                            },
                            {
                                "id": 2,
                                "name": "entrance fees",
                                "r": 255,
                                "g": 0,
                                "b": 255,
                                "eventId": 1
                            },
                            {
                                "id": 3,
                                "name": "travel",
                                "r": 0,
                                "g": 255,
                                "b": 255,
                                "eventId": 1
                            }
                        ]
                    }""";

    @Test
    void downloadBackupAction(@TempDir Path path) {
        final int eventId = 1;

        List<User> users = new ArrayList<>();
        users.add(new User(1, "mik", null, null, null, 0.0));
        users.add(new User(2, "superbos", null, null, null, 0.0));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1, 50.0, "test expense", 1,
            LocalDate.of(2024, 4, 11), List.of(1), 1));
        expenses.add(new Expense(2, 6.0, "test expense 2", 2,
            LocalDate.of(2024, 4, 10), Arrays.asList(1, 2), 2));

        List<Tag> tags = new ArrayList<>();
        tags.add(new commons.dto.Tag(1, "food",
            255, 255, 0, eventId));
        tags.add(new commons.dto.Tag(2, "entrance fees",
            255, 0, 255, eventId));
        tags.add(new commons.dto.Tag(3, "travel",
            0, 255, 255, eventId));

        Event event = new Event(eventId, "test", Arrays.asList(1, 2), expenses, tags);

        ServerUtils server = mock(ServerUtils.class);
        when(server.getUserByEvent(event.getId())).thenReturn(users);

        File file = path.resolve("export.json").toFile();

        AdminDashboardLogic logic = new AdminDashboardLogic(server);
        try {
            logic.downloadBackupAction(event, file);
        } catch (IOException e) {
            fail();
        }

        try (FileReader reader = new FileReader(file)) {
            assertEquals(JsonParser.parseString(testJson), JsonParser.parseReader(reader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void uploadEventSnapshotFromFile(@TempDir Path path) {
        // Initialize test data.
        final int eventId = 1;

        List<User> users = new ArrayList<>();
        users.add(new User(1, "mik", null, null, null, 0.0));
        users.add(new User(2, "superbos", null, null, null, 0.0));

        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1, 50.0, "test expense", 1,
            LocalDate.of(2024, 4, 11), List.of(1), 1));
        expenses.add(new Expense(2, 6.0, "test expense 2", 2,
            LocalDate.of(2024, 4, 10), Arrays.asList(1, 2), 2));

        List<Tag> tags = new ArrayList<>();
        tags.add(new commons.dto.Tag(1, "food",
            255, 255, 0, eventId));
        tags.add(new commons.dto.Tag(2, "entrance fees",
            255, 0, 255, eventId));
        tags.add(new commons.dto.Tag(3, "travel",
            0, 255, 255, eventId));

        Event event = new Event(eventId, "test", Arrays.asList(1, 2), expenses, tags);

        // Create temporary file.
        Path filePath = path.resolve("import.json");
        try {
            Files.writeString(filePath, testJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // ServerUtils mock, featuring auto-incrementing IDs.
        ServerUtils server = mock(ServerUtils.class);
        when(server.addEvent(any())).thenAnswer(a -> {
            Event addedEvent = a.getArgument(0);
            addedEvent.setId(eventId);
            return addedEvent;
        });

        AtomicInteger userCount = new AtomicInteger(1);
        when(server.createUser(anyInt(), any())).thenAnswer(a -> {
            User user = a.getArgument(1);
            user.setId(userCount.get());
            userCount.addAndGet(1);
            return user;
        });

        AtomicInteger tagCount = new AtomicInteger(1);
        when(server.addTag(any(), anyInt())).thenAnswer(a -> {
            Tag argument = a.getArgument(0);
            argument.setId(tagCount.get());
            tagCount.addAndGet(1);
            return argument;
        });

        AtomicInteger expenseCount = new AtomicInteger(1);
        when(server.addExpense(any(), anyInt())).thenAnswer(a -> {
            Expense expense = a.getArgument(0);
            expense.setId(expenseCount.get());
            expenseCount.addAndGet(1);
            return expense;
        });

        // Now perform the actual method.
        AdminDashboardLogic logic = new AdminDashboardLogic(server);
        try {
            logic.uploadEventSnapshotFromFile(filePath.toFile());
        } catch (IOException e) {
            fail();
        }

        // Finally, verify all the test data.
        verify(server).addEvent(event);
        verify(server).createUser(event.getId(), new User(users.getFirst()));
        verify(server).createUser(event.getId(), users.get(1));
        verify(server).addExpense(expenses.getFirst(), event.getId());
        verify(server).addExpense(expenses.get(1), event.getId());
        verify(server).addTag(tags.getFirst(), event.getId());
        verify(server).addTag(tags.get(1), event.getId());
        verify(server).addTag(tags.get(2), event.getId());
    }
}