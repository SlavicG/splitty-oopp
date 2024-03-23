package commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {
    private Integer id;
    private String title;
    private List<Integer> usersIds = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public Event() {

    }

    public Event(Integer id, String title, List<Integer> usersIds, List<Expense> expenses, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.usersIds = usersIds;
        this.expenses = expenses;
        this.tags = tags;
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
        return this.expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    @JsonIgnore
    public String getCode() {
        int x = this.getId();
        String s = "";
        for(int i = 0; i < 10; ++i) {
            Character c = (char)('A' + x % 10);
            x /= 10;
            s = s + c.toString();
        }
        return s;
    }
}
