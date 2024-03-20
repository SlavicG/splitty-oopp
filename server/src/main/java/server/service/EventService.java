package server.service;


import org.springframework.stereotype.Service;
import server.database.EventRepository;
import commons.dto.Event;
import commons.dto.Expense;

import server.database.ExpenseRepository;
import server.database.UserRepository;

import server.model.User;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;


    private Function<server.model.Expense, Expense> mapper = expense -> new commons.dto.Expense(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            expense.getPayer().getId(),
            expense.getDate(),
            expense.getSplitBetween());

    public EventService(EventRepository eventRepository, ExpenseRepository expenseRepository,
                        UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(Event event) {
        server.model.Event newEvent = new server.model.Event();
        newEvent.setTitle(event.getTitle());
        newEvent.setUsers(getUsers(event.getUserIds()));
        server.model.Event createdEvent = eventRepository.save(newEvent);
        Event returnEvent = getEvent(createdEvent);
        return returnEvent;
    }


    public Event getEventById(Integer id) {
        server.model.Event event = eventRepository.getById(id);
        Event returnEvent = new Event();
        returnEvent.setId(event.getId());
        returnEvent.setTitle(event.getTitle());
        returnEvent.setUsers(getUserIds(event.getUsers()));
        if(event.getExpenses()!=null){
            returnEvent.setExpenses(expenseRepository.findAll().stream().
                    map(mapper).toList());
        }
        return returnEvent;
    }


    public Event updateEvent(Event event) {
        server.model.Event newEvent = eventRepository.getById(event.getId());
        newEvent.setTitle(event.getTitle());
        newEvent.getUsers().clear();
        newEvent.getUsers().addAll(getUsers(event.getUserIds()));
        server.model.Event updatedEvent = eventRepository.save(newEvent);

        Event returnEvent = getEvent(updatedEvent);
        return returnEvent;
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll().stream().map(it -> getEvent(it)).toList();
    }


    public Event deleteEvent(Integer id) {
        Optional<server.model.Event> eventToDelete = eventRepository.findById(id);
        return eventToDelete.map(x -> {
            eventRepository.delete(x);
            return getEvent(x);
        }).orElse(null);
    }

    private Event getEvent(server.model.Event it) {
        return new Event(it.getId(), it.getTitle(), getUserIds(it.getUsers()), List.of());
    }

    public List<User> getUsers(List<Integer> userIds) {
        return userIds.stream().map(it -> getUserById(it)).toList();
    }


    private User getUserById(Integer it) {
        User user = userRepository.getById(it);
        if (user == null) throw new IllegalArgumentException("User not found. ID: " + it);
        return user;
    }

    private static List<Integer> getUserIds(List<User> users) {
        return users.stream().map(it -> it.getId()).toList();
    }


    //calculate a debt of a give user
    public Double getDebtOfaUser(Integer id,Integer event_id){
        Event event = getEventById(event_id);
        double fullAmount = event.getExpenses().stream()
                .filter(expense -> expense.getSplitBetween().contains(id))
                .mapToDouble(expense -> expense.getAmount()/expense.getSplitBetween().size())
                .sum();

        //Amount of money spend on expenses in all the
        double amountPayed = event.getExpenses().stream().
                filter(expense -> expense.getPayerId().equals(id))
                .mapToDouble(expense -> expense.getAmount())
                .sum();

        double debt = fullAmount - amountPayed;
        return debt;
    }

    //calculate all debts between all users
    public Map<Integer, Double> getAllDebtsInEvent(Integer event_id) {
        Event event = getEventById(event_id);
        server.model.Event eventForUsers = eventRepository.getById(event_id);
//        List<User> users = eventForUsers.getUsers();
        List<Integer> userIds = event.getUserIds();
        Map<Integer, Double> mapa = new HashMap<>();
        for (int i = 0; i < userIds.size(); i++) {
            mapa.put(userIds.get(i), getDebtOfaUser(userIds.get(i), event_id));
        }
        return mapa;
    }

    //Expenses for which some person paid in an event
    public List<Expense> showAllExpensesPersonPaid(Integer id, Integer event_id){
        Event event = getEventById(event_id);
        List<Expense> listOfExpenses = event.getExpenses().stream().
                filter(expense -> expense.getPayerId().equals(id)).collect(Collectors.toList());
        return listOfExpenses;


    }

}


