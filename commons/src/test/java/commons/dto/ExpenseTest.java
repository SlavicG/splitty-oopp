package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    private User user1;
    private User user2;
    private LocalDateTime date1;
    private LocalDateTime date2;
    private Event event1;
    private Event event2;
    private Expense expense1;
    private Expense expense2;
    private Expense expense3;

    @BeforeEach
    void setup() {
        LocalDate date = LocalDate.of(2021, 10, 24);
        event1 = new Event(1, "party", null, null);
        event2 = new Event(2, "party2", null, null);
        expense1 = new Expense(1, 1.0, "description1", 3, date, new ArrayList<>(), 1);
        expense2 = new Expense(1, 1.0, "description1", 3, date, new ArrayList<>(),1);
        expense3 = new Expense(1, 1.0, "description1", 3, date, new ArrayList<>(), 1);
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
    void getPayerId() {
        assertEquals(3, expense1.getPayerId());
    }

    @Test
    void setPayerId() {
        expense1.setPayerId(5);
        assertEquals(expense1.getPayerId(), 5);
    }

    @Test
    void getDate() {
        LocalDate date = LocalDate.of(2021, 10, 24);
        assertEquals(date, expense1.getDate());
    }

    @Test
    void setDate() {
        LocalDate date = LocalDate.of(2021, 10, 25);
        expense1.setDate(date);
        assertEquals(date, expense1.getDate());
    }

    @Test
    void testEquals() {
        assertEquals(expense1, expense2);
        assertEquals(expense2, expense3);
    }

    @Test
    void testHashcode() {
        assertEquals(expense1.hashCode(), expense2.hashCode());
        assertEquals(expense2.hashCode(), expense3.hashCode());
    }
}
