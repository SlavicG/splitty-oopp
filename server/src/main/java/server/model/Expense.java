package server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "expenses")
public class Expense {
    private @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private Double amount;
    private String description;
    private @ManyToOne User payer;
    private LocalDate date;
    @JsonProperty("split-between")
    private List<Integer> splitBetween;

    private @ManyToOne
        @JoinColumn(name = "event_id") Event event;

    public Expense() {

    }

    public Expense(Integer id, Double amount, String description, User payer,
                   LocalDate date, Event event, List<Integer> splitBetween) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.payer = payer;
        this.date = date;
        this.event = event;
        this.splitBetween = splitBetween;
    }

    public void setSplitBetween(List<Integer> splitBetween) {
        this.splitBetween = splitBetween;
    }

    public List<Integer> getSplitBetween() {
        return splitBetween;
    }

    public Expense(Object o, Double amount, String description, User payer, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.payer = payer;
        this.date = date;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", payer=" + payer +
                ", date=" + date +
                ", event=" + event +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id) &&
                Objects.equals(amount, expense.amount) &&
                Objects.equals(description, expense.description) &&
                Objects.equals(payer, expense.payer) &&
                Objects.equals(date, expense.date) &&
                Objects.equals(event, expense.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, description, payer, date, event);
    }


}
