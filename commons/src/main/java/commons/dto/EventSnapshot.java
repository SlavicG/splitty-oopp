package commons.dto;
import java.util.*;
public class EventSnapshot extends Event{
    public List<User> userList;
    public EventSnapshot(Integer id, String title, List<Integer> usersIds, List<Expense> expenses, List<Tag> tags) {
        super(id, title, usersIds, expenses, tags);
        userList = new ArrayList<>();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    public Event getEvent()
    {
        return new Event(getId(), getTitle(), getUserIds(), getExpenses(), getTags());
    }

}
