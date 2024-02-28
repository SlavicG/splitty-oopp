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

    public CentralizedLocalDataStore(List<Quote> users, List<Quote> expenses, List<Quote> debt, List<Quote> events, List<Quote> quotes, List<Quote> persons, Configuration configuration) {
        this.users = users;
        this.expenses = expenses;
        this.debt = debt;
        this.events = events;
        this.quotes = quotes;
        this.persons = persons;
        this.configuration = configuration;
    }

    public List<Quote> getUsers() {
        ServerUtils server = new ServerUtils(configuration);
        users = server.getUser();
        return users;
    }

    public List<Quote> getExpenses() {
        ServerUtils server = new ServerUtils(configuration);
        expenses = server.getExpense();
        return expenses;
    }

    public List<Quote> getDebt() {
        ServerUtils server = new ServerUtils(configuration);
        debt = server.getDebt();
        return debt;
    }

    public List<Quote> getEvents() {
        ServerUtils server = new ServerUtils(configuration);
        events = server.getEvent();
        return events;
    }

    public List<Quote> getQuotes() {
        ServerUtils server = new ServerUtils(configuration);
        quotes = server.getQuotes();
        return quotes;
    }

    public List<Quote> getPersons() {
        ServerUtils server = new ServerUtils(configuration);
        persons = server.getPerson();
        return persons;
    }
}


