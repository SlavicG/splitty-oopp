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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import commons.dto.*;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

	private static final String SERVER = "http://localhost:8080/";

	public void getQuotesTheHardWay() throws IOException, URISyntaxException {
		var url = new URI("http://localhost:8080/api/quotes").toURL();
		var is = url.openConnection().getInputStream();
		var br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public List<Quote> getQuotes() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
	}

	public Quote addQuote(Quote quote) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
	}

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

	public List<Quote> getPerson() {
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

}