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
    void getQuotesTheHardWay() {
    }

    @Test
    void getAndAddQuotes() {
        Quote quote = new Quote(new Person("Abdullah", "Alpsoy"), "hello");
        server.addQuote(quote);
        List<Quote> quote1 = server.getQuotes();
        assertTrue(quote1.contains(quote));
    }

    @Test
    void getAndAddUsers() {
        User user = new User(5, "Abdullah", "@email.com", "504", "2323");
        server.addUsers(user);
        List<User> user1 = server.getUsers();
        assertTrue(user1.contains(user));
    }

    @Test
    void getAndAddEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Integer> userIds = new ArrayList<>();
        userIds.add(5);
        userIds.add(6);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense());
        Event event = new Event(5, "patat", userIds, expenses);
        server.addEvent(event);
        List<Event> events1 = server.getEvents();
        assertTrue(events1.contains(event));
    }
}