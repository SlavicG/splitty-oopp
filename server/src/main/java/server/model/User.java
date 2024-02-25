package server.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String name;
    private String email;
    private @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY) List<Event> events;
    private String iban;
    private String bic;

    public User() {

    }

    public User(Integer id, String name, String email, String iban, String bic, List<Event> events) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.events = events;
        this.iban = iban;
        this.bic = bic;
    }

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(events, user.events) && Objects.equals(iban, user.iban) && Objects.equals(bic, user.bic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, events, iban, bic);
    }
}
