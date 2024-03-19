package server.service;

import org.springframework.stereotype.Service;
import server.database.EventRepository;
import commons.dto.Event;
import server.database.UserRepository;
import server.model.User;

import commons.dto.Expense;
import server.database.ExpenseRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
            expense.getDate());

    public EventService(EventRepository eventRepository, ExpenseRepository expenseRepository, UserRepository userRepository) {
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
            returnEvent.setExpenses(expenseRepository.findAll().stream().map(mapper).toList());
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
        if(user == null) throw new IllegalArgumentException("User not found. ID: " + it);
        return user;
    }

    private static List<Integer> getUserIds(List<User> users) {
        return users.stream().map(it -> it.getId()).toList();
    }




    //calculate all debts between all users
    public Double getAllDebtsInEvent(Integer event_id){
        Event event = getEventById(event_id);
        double sum = event.getExpenses().stream()
                .mapToDouble(expense -> expense.getAmount())
                .sum();
        sum = sum/event.getUserIds().size();
        return sum;
    }
    //calculate a debt of a give user
    public Double getDebtOfaUser(Integer id,Integer event_id){
        Event event = getEventById(event_id);
        double fullAmount = event.getExpenses().stream()
                .mapToDouble(expense -> expense.getAmount())
                .sum();
        fullAmount = fullAmount/event.getUserIds().size();



        //Amount of money spend on expenses in all the
        double amountPayed = event.getExpenses().stream().
                filter(expense -> expense.getPayerId().equals(id))
                .mapToDouble(expense -> expense.getAmount())
                .sum();

        double debt = fullAmount - amountPayed;
        return debt;
    }


}


