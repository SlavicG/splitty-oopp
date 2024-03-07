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
import org.glassfish.jersey.client.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;


public class ServerUtils {
	private final Configuration configuration;

	@Inject
	public ServerUtils(Configuration configuration) {
		this.configuration = configuration;
	}

	public void getQuotesTheHardWay() throws IOException, URISyntaxException {
		var url = new URI(configuration.getServerURL() + "/api/quotes").toURL();
		var is = url.openConnection().getInputStream();
		var br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public List<Quote> getQuotes() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Quote>>() {});
	}

	public Quote addQuote(Quote quote) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
	}

	public List<User> getUsers() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("rest/users") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<User>>() {
				});
	}

	public List<Event> getEvents() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Event") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Event>>() {
				});
	}

	public List<Expense> getExpenses() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Expense") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Expense>>() {
				});
	}

	public List<Person> getPersons() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Person") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Person>>() {
				});
	}

	public List<Debt> getDebt() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Debt") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.get(new GenericType<List<Debt>>() {
				});
	}

	public User addUsers(User user) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/User") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(user, APPLICATION_JSON), User.class);
	}

	public Expense addExpense(Expense expense) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Expense") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}

	public Debt addDebt(Debt debt) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Debt") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(debt, APPLICATION_JSON), Debt.class);
	}

	public Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Debt") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	public Person addPerson(Person person) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(configuration.getServerURL()).path("api/Debt") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(person, APPLICATION_JSON), Person.class);
	}


}
