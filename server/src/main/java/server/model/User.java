package server.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    private @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String name;
    private String email;
    private @ManyToOne(fetch = FetchType.EAGER) Event event;
    private String iban;
    private String bic;

    public User() {

    }

    public User(commons.dto.User user, Event event) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.event = event;
        this.iban = user.getIban();
        this.bic = user.getBic();
    }

    public User(Integer id, String name, String email, String iban, String bic, Event event) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.event = event;
        this.iban = iban;
        this.bic = bic;
    }

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(Integer id, String name, String email, String iban, String bic) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
    }

    public commons.dto.User toCommonUser() {
        return new commons.dto.User(
            getId(),
            getName(),
            getEmail(),
            getIban(),
            getBic()
        );
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

    public Event getEvent() {
        return event;
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

    public void setEvent(Event event) {
        this.event = event;
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
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, event, iban, bic);
    }
}
