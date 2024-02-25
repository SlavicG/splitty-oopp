package client;

import client.utils.ServerUtils;
import commons.dto.*;
import java.util.List;

public class CentralizedLocalDataStore {

    private List<Quote> users;
    private List<Quote> expenses;

    private List<Quote> debt;
    private List<Quote> events;
    private List<Quote> quotes;
    private List<Quote> persons;

    public CentralizedLocalDataStore(List<Quote> users, List<Quote> expenses, List<Quote> debt, List<Quote> events, List<Quote> quotes, List<Quote> persons) {
        this.users = users;
        this.expenses = expenses;
        this.debt = debt;
        this.events = events;
        this.quotes = quotes;
        this.persons = persons;
    }

    public List<Quote> getUsers() {
        ServerUtils server = new ServerUtils();
        users = server.getUser();
        return users;
    }

    public List<Quote> getExpenses() {
        ServerUtils server = new ServerUtils();
        expenses = server.getExpense();
        return expenses;
    }

    public List<Quote> getDebt() {
        ServerUtils server = new ServerUtils();
        debt = server.getDebt();
        return debt;
    }

    public List<Quote> getEvents() {
        ServerUtils server = new ServerUtils();
        events = server.getEvent();
        return events;
    }

    public List<Quote> getQuotes() {
        ServerUtils server = new ServerUtils();
        quotes = server.getQuotes();
        return quotes;
    }

    public List<Quote> getPersons() {
        ServerUtils server = new ServerUtils();
        persons = server.getPerson();
        return persons;
    }
}


