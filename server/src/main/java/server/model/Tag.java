package server.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import commons.dto.ColorDeserializer;

import java.awt.*;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    private @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String name;
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color color;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Tag(Integer id, String name, Color color, Event event) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.event = event;
    }

    public Tag() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Event getEvent() {
        return event;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(getId(), tag.getId()) &&
                Objects.equals(getName(), tag.getName()) &&
                Objects.equals(getColor(), tag.getColor()) &&
                Objects.equals(getEvent(), tag.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getEvent());
    }
}
