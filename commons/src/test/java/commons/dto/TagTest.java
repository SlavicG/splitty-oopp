package commons.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {
    private Tag tag1;
    private Tag tag2;
    private Color color1;
    private Color color2;

    @BeforeEach
    void setup() {
        color1 = new Color(255);
        color2 = new Color(0);
        tag1 = new Tag(1, "food", color1);
        tag2 = new Tag(2, "travel", color2);
    }

    @Test
    void getId() {
        assertEquals(1,tag1.getId());
    }

    @Test
    void getName() {
        assertEquals("food", tag1.getName());
    }

    @Test
    void getColor() {
        assertEquals(color1,tag1.getColor());
    }

    @Test
    void setId() {
        tag1.setId(3);
        assertEquals(3, tag1.getId());
    }

    @Test
    void setName() {
        tag1.setName("food2");
        assertEquals("food2", tag1.getName());
    }

    @Test
    void setColor() {
        Color color3 = new Color(2);
        tag1.setColor(color3);
        assertEquals(color3, tag1.getColor());
    }

    @Test
    void testEquals() {
        assertFalse(tag1.equals(tag2));
    }

    @Test
    void testHashCode() {
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
}