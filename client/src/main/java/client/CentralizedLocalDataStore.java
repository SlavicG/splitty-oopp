package client;

import client.utils.Configuration;
import client.utils.ServerUtils;
import commons.dto.Quote;

import java.util.List;

public class CentralizedLocalDataStore {

    private List<Quote> users;
    private List<Quote> expenses;
    private List<Quote> debt;
    private List<Quote> events;
    private List<Quote> quotes;
    private List<Quote> persons;
    private final client.utils.Configuration configuration;

    public CentralizedLocalDataStore(Configuration configuration) {
        ServerUtils server = new ServerUtils(configuration);
        this.users = server.getUser();
        this.expenses = server.getExpense();
        this.debt = server.getDebt();
        this.events = server.getEvent();
        this.quotes = server.getQuotes();
        this.persons = server.getPerson();
        this.configuration = configuration;
    }

    public List<Quote> getUsers() {
        return users;
    }

    public List<Quote> getExpenses() {
        return expenses;
    }

    public List<Quote> getDebt() {
        return debt;
    }

    public List<Quote> getEvents() {
        return events;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public List<Quote> getPersons() {
        return persons;
    }
}


