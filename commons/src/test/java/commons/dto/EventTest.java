package commons.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void getId() {
        Event event = new Event(1, "party", null, null);
        assertEquals(1, event.getId());
    }

    @Test
    void setId() {
        Event event = new Event(1, "party", null, null);
        event.setId(2);
        assertEquals(2, event.getId());
    }

    @Test
    void getTitle() {
        Event event = new Event(1, "party", null, null);
        assertEquals("party", event.getTitle());
    }

    @Test
    void setTitle() {
        Event event = new Event(1, "party", null, null);
        event.setTitle("partyZ");
        assertEquals("partyZ", event.getTitle());
    }

    @Test
    void getUsers() {
        Event event = new Event(1, "party", null, null);
        assertEquals(null,event.getUsers());
    }

    @Test
    void setUsers() {
        Event event = new Event(1, "party", null, null);
        List<User> users = new ArrayList<>();
        users.add(new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345"));
        event.setUsers(users);
        assertEquals(users,event.getUsers());
    }

    @Test
    void getExpenses() {
        Event event = new Event(1, "party", null, null);
        assertEquals(null,event.getExpenses());
    }

    @Test
    void setExpenses() {
        Event event = new Event(1, "party", null, null);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1, 25.0, "taxi", null, null, null));
        event.setExpenses(expenses);
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void testEquals() {
        Event event = new Event(1, "party", null, null);
        Event event2 = new Event(1, "party", null, null);
        assertTrue(event.equals(event2));
    }

    @Test
    void testHashCode() {
        Event event = new Event(1, "party", null, null);
        assertEquals(-790538474, event.hashCode());
    }
}