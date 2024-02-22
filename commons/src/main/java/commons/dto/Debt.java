package commons.dto;

import java.util.Objects;

public class Debt {
    private Integer id;
    private User owed;
    private User indebted;
    private Double amount;

    public Debt(Integer id, User owed, User indebted, Double amount) {
        this.id = id;
        this.owed = owed;
        this.indebted = indebted;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOwed() {
        return owed;
    }

    public void setOwed(User owed) {
        this.owed = owed;
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
        return Objects.equals(id, debt.id) && Objects.equals(owed, debt.owed) && Objects.equals(indebted, debt.indebted) && Objects.equals(amount, debt.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owed, indebted, amount);
    }
}
