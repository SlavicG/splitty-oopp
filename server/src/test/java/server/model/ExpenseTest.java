package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    private User user1;
    private User user2;
    private LocalDate date1;
    private LocalDate date2;
    private Event event1;
    private Event event2;
    private Expense expense1;
    private Expense expense2;
    private Expense expense3;

    @BeforeEach
    void setup() {
        user1 = new User(1, "Miruna", "mcoroi@tudelft.nl", "123", "567");
        user2 = new User(1, "Slavic", "slavic@tudelft.nl", "123", "567");
        date1 = LocalDate.of(2021, 10, 24);
        date2 = LocalDate.of(2022, 10, 24);
        event1 = new Event(1, "party", null, null, null);
        event2 = new Event(2, "party2", null, null, null);
        expense1 = new Expense(1, 1.0, "description1", user1, date1, event1, new ArrayList<>(), null);
        expense2 = new Expense(1, 1.0, "description1", user1, date1, event1, new ArrayList<>(), null);
        expense3 = new Expense(1, 1.0, "description1", user2, date2, event2, new ArrayList<>(), null);
    }

    @Test
    void getId() {
        assertEquals(1, expense1.getId());
    }

    @Test
    void setId() {
        expense1.setId(2);
        assertEquals(2, expense1.getId());
    }

    @Test
    void getAmount() {
        assertEquals(1.0, expense1.getAmount());
    }

    @Test
    void setAmount() {
        expense1.setAmount(1.5);
        assertEquals(1.5, expense1.getAmount());
    }

    @Test
    void getDescription() {
        assertEquals("description1", expense1.getDescription());
    }

    @Test
    void setDescription() {
        expense1.setDescription("description2");
        assertEquals("description2", expense1.getDescription());
    }

    @Test
    void getPayer() {
        assertEquals(user1, expense1.getPayer());
    }

    @Test
    void setPayer() {
        expense1.setPayer(user2);
        assertEquals(user2, expense1.getPayer());
    }

    @Test
    void getDate() {
        assertEquals(date1, expense1.getDate());
    }

    @Test
    void setDate() {
        expense1.setDate(date2);
        assertEquals(date2, expense1.getDate());
    }

    @Test
    void getEvent() {
        assertEquals(event1, expense1.getEvent());
    }

    @Test
    void setEvent() {
        expense1.setEvent(event2);
        assertEquals(event2, expense1.getEvent());
    }

    @Test
    void testEquals() {
        assertEquals(expense1, expense2);
    }

    @Test
    void testHashCode() {
        assertNotEquals(expense1.hashCode(), expense3.hashCode());
    }
}