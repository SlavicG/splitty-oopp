package commons.dto;

import java.util.Objects;

public class Tag {
    private Integer id;
    private String name;
    private float r, g, b;
    private int eventId;

    public Tag() {

    }

    public Tag(Integer id, String name, float r, float g, float b, int eventId) {
        this.id = id;
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.eventId = eventId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
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
        return id == tag.id && Float.compare(r, tag.r) == 0 &&
                Float.compare(g, tag.g) == 0 &&
                Float.compare(b, tag.b) == 0 &&
                eventId == tag.eventId &&
                Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, r, g, b, eventId);
    }
}
