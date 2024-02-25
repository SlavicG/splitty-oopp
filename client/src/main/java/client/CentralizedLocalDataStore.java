package client;

import commons.dto.*;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CentralizedLocalDataStore {

    private List<User> users;
    private List<Expense> expenses;

    private List<Debt> debt;
    private List<Event> events;
    private List<Quote> quotes;
    private List<Person> persons;

    public CentralizedLocalDataStore(List<User> users, List<Expense> expenses, List<Debt> debt, List<Event> events, List<Quote> quotes, List<Person> persons) {
        this.users = users;
        this.expenses = expenses;
        this.debt = debt;
        this.events = events;
        this.quotes = quotes;
        this.persons = persons;
    }
}


