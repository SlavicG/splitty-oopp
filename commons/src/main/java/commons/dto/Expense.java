package commons.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Expense {
    private Integer id;
    private Double amount;
    private String description;
    private User payer;
    private LocalDateTime date;
    private Event event;

    public Expense(Integer id, Double amount, String description, User payer, LocalDateTime date, Event event) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.payer = payer;
        this.date = date;
        this.event = event;
    }

    public Expense() {

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id) && Objects.equals(amount, expense.amount) && Objects.equals(description, expense.description) && Objects.equals(payer, expense.payer) && Objects.equals(date, expense.date) && Objects.equals(event, expense.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, description, payer, date, event);
    }
}
