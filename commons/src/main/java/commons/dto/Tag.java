package commons.dto;

import java.awt.*;
import java.util.Objects;

public class Tag {
    private int id;
    private String name;
    private Color color;
    private int eventId;

    public Tag() {

    }

    public Tag(int id, String name, Color color, int eventId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.eventId = eventId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return getId() == tag.getId() &&
                Objects.equals(getName(), tag.getName()) &&
                Objects.equals(getColor(), tag.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor());
    }
}
