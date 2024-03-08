package server.model;

import jakarta.persistence.*;

import java.awt.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String name;
    private Color color;
    @OneToMany @JoinColumn(name ="expense_id") private List<Expense> expenses;
    @ManyToOne @JoinColumn(name ="event_id") private Event event;

    public Tag(Integer id, String name, Color color, List<Expense> expenses, Event event) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.expenses = expenses;
        this.event = event;
    }

    protected Tag() {

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

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(getId(), tag.getId()) && Objects.equals(getName(), tag.getName()) && Objects.equals(getColor(), tag.getColor()) && Objects.equals(getExpenses(), tag.getExpenses()) && Objects.equals(getEvent(), tag.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getExpenses(), getEvent());
    }
}
