package client.utils;

import commons.dto.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils3 {

    private static final String SERVER = "http://localhost:8080/";

    public List<Quote> getUser() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/User") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public List<Quote> getEvent() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Event") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public List<Quote> getExpense() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Expense") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public List<Quote> Person() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Person") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public List<Quote> getDebt() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Debt") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public List<Quote> getQuote() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Quote") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public User addUser(User user) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/User") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    public Expense addExpense(Expense expense) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Expense") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    public Debt addQuote(Debt debt) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Debt") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(debt, APPLICATION_JSON), Debt.class);
    }

    public Event addEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Debt") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    public Person addPerson(Person person) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/Debt") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(person, APPLICATION_JSON), Person.class);
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

}
