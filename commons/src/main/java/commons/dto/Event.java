package commons.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {
    private Integer id;
    private String title;
    private List<Integer> usersIds = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();

    public Event() {

    }

    public Event(Integer id, String title, List<Integer> usersIds, List<Expense> expenses) {
        this.id = id;
        this.title = title;
        this.usersIds = usersIds;
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

    public List<Integer> getUserIds() {
        return usersIds;
    }

    public void setUsers(List<Integer> usersIds) {
        this.usersIds = usersIds;
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
                Objects.equals(usersIds, event.usersIds) &&
                Objects.equals(expenses, event.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, usersIds, expenses);
    }

    // Used by the ListView on the startpage, hence the rather useless return value.
    @Override
    public String toString() {
        return title;
    }
}
