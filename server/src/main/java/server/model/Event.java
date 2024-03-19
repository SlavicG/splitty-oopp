package server.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {

    private @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String title;
    private @ManyToMany
        @JoinTable(
            name = "event_user",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    ) List<User> users;
    private @OneToMany(mappedBy = "event", cascade = CascadeType.ALL) List<Expense> expenses;

    public Event() {

    }

    public Event(Integer id, String title, List<User> users, List<Expense> expenses) {
        this.id = id;
        this.title = title;
        this.users = users;
        this.expenses = expenses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(title, event.title) &&
                Objects.equals(users, event.users) &&
                Objects.equals(expenses, event.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, users, expenses);
    }

    @Override
    public String toString() {
        return "Event= {" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", users=" + users +
                ", expenses=" + expenses +
                '}';
    }
}

