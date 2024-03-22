package server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void getId() {
        Event event = new Event(1, "party", null, null, null);
        assertEquals(1, event.getId());
    }

    @Test
    void setId() {
        Event event = new Event(1, "party", null, null, null);
        event.setId(2);
        assertEquals(2, event.getId());
    }

    @Test
    void getTitle() {
        Event event = new Event(1, "party", null, null, null);
        assertEquals("party", event.getTitle());
    }

    @Test
    void setTitle() {
        Event event = new Event(1, "party", null, null, null);
        event.setTitle("partyZ");
        assertEquals("partyZ", event.getTitle());
    }

    @Test
    void testEquals() {
        Event event = new Event(1, "party", null, null, null);
        Event event2 = new Event(1, "party", null, null, null);
        assertTrue(event.equals(event2));
    }


}