package server.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    private @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String name;
    private float r, g, b;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Tag(Integer id, String name, float r, float g, float b, Event event) {
        this.id = id;
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
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

    public Event getEvent() {
        return event;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Float.compare(r, tag.r) == 0 &&
                Float.compare(g, tag.g) == 0 &&
                Float.compare(b, tag.b) == 0 &&
                Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name) &&
                Objects.equals(event, tag.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, r, g, b, event);
    }
}
