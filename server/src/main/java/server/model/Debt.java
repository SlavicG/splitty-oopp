package server.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "debts")
public class Debt {
    private @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private @OneToOne User indebted;
    private Double amount;

    public Debt() {

    }

    public Debt(Integer id, User indebted, Double amount) {
        this.id = id;
        this.indebted = indebted;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getIndebted() {
        return indebted;
    }

    public void setIndebted(User indebted) {
        this.indebted = indebted;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Objects.equals(id, debt.id) &&
                Objects.equals(indebted, debt.indebted) &&
                Objects.equals(amount, debt.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indebted, amount);
    }
}
