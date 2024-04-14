package server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import server.model.Event;
import server.model.Expense;
import server.model.Tag;
import server.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private EventService eventService;

    private commons.dto.User user2;
    private LocalDateTime date1;
    private LocalDateTime date2;
    private server.model.Event event1;
    private server.model.Event event2;
    private server.model.User user1;
    private Expense expense1;
    private Expense expense2;
    private Tag tag;
    private commons.dto.Expense expense3;

    @BeforeEach
    void setup()
    {
        LocalDate date = LocalDate.of(2024, 5,10);
        user1 = new User(1, "david",
                "davidgogoana@gmail.com", "1234","5678");
        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        ArrayList<Expense> expenses = new ArrayList<>();

        expense1 = new Expense(1, 1.0, "description1",
                new User(),
                date, new server.model.Event(), new ArrayList<>(), new Tag());
        expense2 = new Expense(1, 1.0, "description1",
                new User(),
                date,new server.model.Event(), new ArrayList<>(),new Tag());
        expenses.add(expense1);
        expenses.add(expense2);
        tag = new Tag(1,"tickets",1,1,1,event1);
        ArrayList<Tag> tags1 = new ArrayList<>();
        tags1.add(tag);
        tag = new Tag(1,"tickets",1,1,1,event2);
        ArrayList<Tag> tags2 = new ArrayList<>();
        tags2.add(tag);
        event1 = new server.model.Event(1,
                "bowling", users, expenses, tags1);
        event2 = new server.model.Event(2,
                "bowling", users, expenses, tags2 );



    }
    @Test
    void createEvent() {

    }

    @Test
    void getEventById() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void getAllEvents() {
        List<Event> mockEvents = Arrays.asList(event1);
        when(eventRepository.findAll()).thenReturn(mockEvents);
        List<commons.dto.Event> resultEvents = eventService.getAllEvents();
        commons.dto.Event event = resultEvents.get(0);
        assertEquals(event1.getId(), event.getId());
        assertEquals(event1.getUsers().get(0).getId(),event.getUserIds().get(0));
        assertEquals(event1.getTitle(),event.getTitle());
    }

    @Test
    void deleteEvent() {
    }

    @Test
    void getDebtOfaUser() {
    }

    @Test
    void getAllDebtsInEvent() {
    }

    @Test
    void expensesUserPaid() {
    }

    @Test
    void expenseIncludeUser() {
    }

    @Test
    void allExpenses() {
        List<Event> mockEvents = Arrays.asList(event1);
        when(eventRepository.findAll()).thenReturn(mockEvents);
        List<commons.dto.Event> resultEvents = eventService.getAllEvents();
        commons.dto.Event event = resultEvents.get(0);
        assertEquals(event1.getExpenses(),event1.getExpenses());
    }

    @Test
    void addUser() {
    }

    @Test
    void removeUser() {
    }


    @Test
    void getUsers() {
        List<Event> mockEvents = Arrays.asList(event1);
        when(eventRepository.findAll()).thenReturn(mockEvents);
        List<commons.dto.Event> resultEvents = eventService.getAllEvents();
        commons.dto.Event event = resultEvents.get(0);
        assertEquals(event1.getUsers(),event1.getUsers());
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUpdates() {
    }

    @Test
    void settleAllDebtsEvent() {
    }

    @Test
    void updateAllDebtsInEvent() {
    }

    @Test
    void settleDebtUser() {
    }

    @Test
    void settleExpense() {
    }

    @Test
    void settleAB() {
    }

    @Test
    void oweMoney() {
    }

    @Test
    void getAllDebtsInEventUser() {
    }
}