package client.utils;

import commons.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServerUtilsTest {
    Configuration configuration = new Configuration();
    ServerUtils server = new ServerUtils(configuration);

    @Test
    void constructorTest() {
        ServerUtils server1 = new ServerUtils(configuration);
    }

    @Test
    void getAndAddUsers() {
        User user = new User(5, "Abdullah", "@email.com", "504", "2323");
        //server.addUsers(user);
        //List<User> user1 = server.getUsers();
        // assertFalse(user1.contains(user));
    }

    @Test
    void getAndAddEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Integer> userIds = new ArrayList<>();
        userIds.add(5);
        userIds.add(6);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        Event event = new Event(5, "patat", userIds, expenses, null);
        //server.addEvent(event);
        //List<Event> events1 = server.getEvents();
        //assertTrue(events1.contains(event));
    }

    @Test
    void getAndAddExpenses() {
        LocalDateTime now = LocalDateTime.now();
        List<Integer> users = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        Event event = new Event(5, "patat", users, expenses, null);
        Expense expense = new Expense();
        expenses.add(expense);
        //server.addEvent(event);
        //List<Expense> expense1 = server.getExpenses();
        //assertTrue(expense1.contains(expense));
    }

    @Test
    void getAndAddPersons() {
        Person person = new Person("Abdullah", "Alpsoy");
        //server.addPerson(person);
        //List<Person> person1 = server.getPersons();
        //assertTrue(person1.contains(person));
    }

    @Test
    void getAndAddDebt() {
        User user = new User(7, "Abdullah", "@email.com", "504", "2323");
        User user1 = new User(6, "Ilker", "@email.com", "5046", "2324");
        Debt debt = new Debt(5, user1, user, 50.0);
        //server.addDebt(debt);
        //List<Quote> quote1 = server.getQuotes();
        //assertTrue(quote1.contains(debt));
    }
}