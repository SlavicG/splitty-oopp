package commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import java.util.List;
import java.util.Objects;

public class Expense {
    private Integer id;
    private Double amount;
    private String description;
    private Integer payerId;
    private Integer tagId;

    @JsonProperty("split-between")
    private List<Integer> splitBetween;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public Expense(Integer id, Double amount, String description,
                   Integer payerId, LocalDate date, List<Integer> splitBetween, Integer tagId ) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.payerId = payerId;
        this.date = date;
        this.splitBetween = splitBetween;
        this.tagId = tagId;
    }


//    public Expense(Integer id, Double amount, String description,
//                   Integer payerId, LocalDate date, List<Integer> splitBetween ) {
//        this.id = id;
//        this.amount = amount;
//        this.description = description;
//        this.payerId = payerId;
//        this.date = date;
//        this.splitBetween = splitBetween;
//    }

    public Expense(Expense expense) {
        this.id = expense.getId();
        this.amount = expense.getAmount();
        this.description = expense.getDescription();
        this.payerId = expense.getPayerId();
        this.date = expense.getDate();
        this.splitBetween = expense.getSplitBetween();
        this.tagId = expense.getTagId();
    }

    public Expense() {

    }




    public Integer getId() {
        return id;
    }

    public List<Integer> getSplitBetween() {
        return splitBetween;
    }

    public void setSplitBetween(List<Integer> splitBetween) {
        this.splitBetween = splitBetween;
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

    public Integer getPayerId() {
        return payerId;
    }

    public void setPayerId(Integer payerId) {
        this.payerId = payerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTagId() {
        return this.tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id)
                && Objects.equals(amount, expense.amount)
                && Objects.equals(description, expense.description)
                && Objects.equals(payerId, expense.payerId)
                && Objects.equals(splitBetween, expense.splitBetween)
                && Objects.equals(date, expense.date)
                && Objects.equals(tagId, expense.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, description, payerId, date, splitBetween, tagId);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", payerId=" + payerId +
                ", tagId=" + tagId +
                ", splitBetween=" + splitBetween +
                ", date=" + date +
                '}';
    }
}
