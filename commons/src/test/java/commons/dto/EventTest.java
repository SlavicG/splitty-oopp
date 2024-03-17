package commons.dto;

import org.junit.jupiter.api.Test;

import java.security.spec.ECField;
import java.time.LocalDate;
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
        Event event1 = new Event();
        event1.setId(1);
        assertEquals(2, event.getId());
        assertEquals(1,event1.getId());
    }

    @Test
    void getTitle() {
        Event event = new Event(1, "party", null, null);
        Event event1 = new Event();
        event1.setId(1);
        assertEquals(1,event1.getId());
        assertEquals("party", event.getTitle());
    }

    @Test
    void setTitle() {
        Event event = new Event(1, "party", null, null);
        event.setTitle("partyZ");
        Event event1 = new Event();
        event1.setTitle("snackbar visit");
        assertEquals("partyZ", event.getTitle());
        assertEquals(event1.getTitle(), "snackbar visit");
    }

    @Test
    void getUsersIds() {
        Event event = new Event(1, "party", null, null);
        assertEquals(null, event.getUserIds());
    }

    @Test
    void setUserIds() {
        Event event = new Event(1, "party", null, null);
        List<Integer> userIds = new ArrayList<>();
        userIds.add(5);
        event.setUsers(userIds);
        assertEquals(userIds, event.getUserIds());
    }

    @Test
    void getExpenses() {
        Event event = new Event(1, "party", null, null);
        assertEquals(null, event.getExpenses());
    }

    @Test
    void setExpenses() {
        Event event = new Event(1, "party", null, null);
        List<Expense> expenses = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 3, 3);
        Expense expense1 = new Expense(1, 20.0, "SnackBar", 3, date);
        expenses.add(expense1);
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
        Event event2 = new Event(1, "party", null, null);
        assertEquals(event, event2);
    }


}