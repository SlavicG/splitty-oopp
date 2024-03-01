package client;

import client.utils.Configuration;
import client.utils.ServerUtils;
import commons.dto.*;
import java.util.List;

public class CentralizedLocalDataStore {

    private List<User> users;
    private List<Expense> expenses;
    private List<Debt> debt;
    private List<Event> events;
    private List<Quote> quotes;
    private List<Person> persons;
    private final client.utils.Configuration configuration;

    public CentralizedLocalDataStore(Configuration configuration) {
        ServerUtils server = new ServerUtils(configuration);
        this.users = server.getUsers();
        this.expenses = server.getExpenses();
        this.debt = server.getDebt();
        this.events = server.getEvents();
        this.quotes = server.getQuotes();
        this.persons = server.getPersons();
        this.configuration = configuration;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Debt> getDebt() {
        return debt;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public List<Person> getPersons() {
        return persons;
    }
}


