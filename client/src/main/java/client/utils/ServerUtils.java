/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import com.google.inject.Inject;
import commons.dto.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class ServerUtils {
    private final Configuration configuration;
    private final StompSession session;

    private static final String SERVER_URL = "http://localhost:8080";
    public String getServerUrl() {
        return configuration.getHttpServerUrl();
    }
    @Inject
    public ServerUtils(Configuration configuration) {
        this.configuration = configuration;
        this.session = connect(configuration.getWebsocketServerUrl());
    }

    public void getQuotesTheHardWay() throws IOException, URISyntaxException {
        var url = new URI(configuration.getHttpServerUrl() + "/api/quotes").toURL();
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("api/quotes")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<List<Quote>>() {
                });
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("api/quotes")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public User getUserById(int eventId, int userId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/users/" + userId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    public User createUser(int eventId, User user) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/users")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    public User updateUser(int eventId, User user) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/users/" + user.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .put(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    public List<User> getUserByEvent(int eventId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/users")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    public Response deleteUser(int eventId, int userId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/users/" + userId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

    public List<Event> getEvents() {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<List<Event>>() {
                });
    }

    public Event getEventById(int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId)
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<>() {
                });
    }

    public Event addEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    public Event updateEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig()) 
				.target(configuration.getHttpServerUrl()).path("/rest/events/" + event.getId())
				.request(APPLICATION_JSON) 
				.accept(APPLICATION_JSON) 
				.put(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    public Response deleteEvent(int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId)
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .delete();
    }

    public List<Expense> getExpenses(int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/expenses")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<List<Expense>>() {
                });
    }

    public Expense getExpenseById(int eventId, int expenseId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/expenses/" + expenseId)
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<Expense>() {
                });
    }

    public Expense addExpense(Expense expense, int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/expenses")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    public Expense updateExpense(Expense expense, int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path(
                    "/rest/events/" + eventId + "/expenses/" + expense.getId()) 
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    public Response deleteExpense(Expense expense, int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path(
                    "/rest/events/" + eventId + "/expenses/" + expense.getId()) 
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .delete();
    }

    public List<Debt> getDebts(int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/debts")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<List<Debt>>() {
                });
    }

    public Debt getDebt(int eventId, int userId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/users/" + userId + "/debt")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<Debt>() {
                });
    }

    public Debt addDebt(Debt debt) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/api/Debt")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .post(Entity.entity(debt, APPLICATION_JSON), Debt.class);
    }

    public Person addPerson(Person person) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("api/Person")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .post(Entity.entity(person, APPLICATION_JSON), Person.class);
    }

    public List<Person> getPersons() {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("api/Person")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<List<Person>>() {
                });
    }
    public Mail sendEmail(Mail mailRequest) {
        return ClientBuilder.newClient(new ClientConfig()).
                target(configuration.getHttpServerUrl())
                .path("rest/mail").request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(mailRequest, APPLICATION_JSON), Mail.class);
    }


    public boolean login(String password) {
        try {
            String auth = "user" + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authHeader = "Basic " + encodedAuth;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(configuration.getHttpServerUrl() + "admin/dashboard"))
                    .header("Authorization", authHeader)
                    .GET() // Or POST, if you're testing a specific action
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Success if we get a response code of 200 OK, indicating that the credentials are correct
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();
    public void registerForUpdates(Consumer<Event> consumer) {
        EXEC.submit(() -> {
            while(!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) 
                        .target(configuration.getHttpServerUrl()).path("/rest/events/updates")
                        .request(APPLICATION_JSON) 
                        .accept(APPLICATION_JSON) 
                        .get(Response.class);
                if(res.getStatus() == 204) {
                    continue;
                }
                var e = res.readEntity(Event.class);
                consumer.accept(e);
            }
        });
    }
    public void stop() {
        EXEC.shutdown();
    }

    public List<Tag> getTags(int eventId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/tags")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public Tag getTagById(int eventId, int tagId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/tags/" + tagId)
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .get(new GenericType<Tag>() {
                });
    }

    public Tag addTag(Tag tag, int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/tags")
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    public Tag updateTag(Tag tag, int eventId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/tags/" + tag.getId())
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }

    public Response deleteTag(int eventId, int tagId) {
        return ClientBuilder.newClient(new ClientConfig()) 
                .target(configuration.getHttpServerUrl()).path("/rest/events/" + eventId + "/tags/" + tagId)
                .request(APPLICATION_JSON) 
                .accept(APPLICATION_JSON) 
                .delete();
    }
    public MailConfig setEmailConfig(MailConfig mailConfig) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(configuration.getHttpServerUrl()).path("/rest/mail/config")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(mailConfig, APPLICATION_JSON), MailConfig.class);
    }
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }
    
    public void send(String dest, Object o) {
        session.send(dest, o);
    }
}
