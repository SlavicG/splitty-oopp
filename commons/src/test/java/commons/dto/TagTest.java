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

    }

    @Test
    void getName() {
    }

    @Test
    void getColor() {
    }

    @Test
    void setId() {
    }

    @Test
    void setName() {
    }

    @Test
    void setColor() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}