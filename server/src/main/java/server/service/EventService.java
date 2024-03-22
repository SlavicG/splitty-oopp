package server.service;


import commons.dto.Event;
import commons.dto.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import server.database.UserRepository;
import server.model.Tag;
import server.model.User;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private Map<Object, Consumer<Event>> listeners = new HashMap<>();

    private Function<server.model.Expense, Expense> mapper = expense -> new commons.dto.Expense(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            expense.getPayer().getId(),
            expense.getDate(),
            expense.getSplitBetween(),
            0);

    public EventService(EventRepository eventRepository, ExpenseRepository expenseRepository,
                        UserRepository userRepository, TagRepository tagRepository) {
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    public ResponseEntity<Event> createEvent(Event event) {
        server.model.Event newEvent = new server.model.Event();
        newEvent.setTitle(event.getTitle());
        newEvent.setUsers(getUsers(event.getUserIds()));

        server.model.Tag tag1 = new server.model.Tag();
        tag1.setName("Food");
        tag1.setColor(new Color(0,128,0));
        tagRepository.save(tag1);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        newEvent.setTags(tags);
        server.model.Event createdEvent = eventRepository.save(newEvent);
        Event returnEvent = getEvent(createdEvent);
        listeners.forEach((k, l) -> {
            l.accept(returnEvent);
        });
        return ResponseEntity.ok(returnEvent);
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

        return new Event(it.getId(), it.getTitle(), getUserIds(it.getUsers()), new ArrayList<>(), new ArrayList<>());
    }

    public List<User> getUsers(List<Integer> userIds) {
        return userIds.stream().map(it -> getUserById(it)).toList();
    }


    private User getUserById(Integer it) {
        User user = userRepository.findById(it).orElse(null);
        if (user == null) throw new IllegalArgumentException("User not found. ID: " + it);
        return user;
    }

//    //private static List<Tag> getTags(List<Tag> tags) {
//        return tags.stream().map(it -> it.getId()).toList();
//    }

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
        List<Integer> userIds = event.getUserIds();
        Map<Integer, Double> mapa = new HashMap<>();
        for (int i = 0; i < userIds.size(); i++) {
            mapa.put(userIds.get(i), getDebtOfaUser(userIds.get(i), event_id));
        }
        return mapa;
    }

    //Expenses for which some person paid in an event
    public List<Expense> expensesUserPaid(Integer id, Integer event_id){
        Event event = getEventById(event_id);
        List<Expense> listOfExpenses = event.getExpenses().stream().
                filter(expense -> expense.getPayerId().equals(id)).collect(Collectors.toList());
        return listOfExpenses;
    }

    //Expenses that include a person in an event
    public List<Expense> expenseIncludeUser(Integer id, Integer event_id){
        Event event = getEventById(event_id);
        List<Expense> listOfExpenses = event.getExpenses().stream().
                filter(expense -> expense.getSplitBetween().contains(id)).collect(Collectors.toList());
        return listOfExpenses;
    }

    // All expenses in an event
    public List<Expense> allExpenses(Integer event_id){
        Event event = getEventById(event_id);
        List<Expense> listOfExpenses = event.getExpenses();
        return listOfExpenses;
    }

    public Event addUser(Integer event_id, Integer user_id) {
        server.model.Event newEvent = eventRepository.findById(event_id).orElse(null);
        boolean performed = false;
        boolean have = false;
        for(server.model.User user: newEvent.getUsers()) {
            if (user.getId().equals(user_id)) {
                have = true;
                break;
            }
        }
        if(have == false) {
            newEvent.getUsers().add(getUserById(user_id));
            server.model.Event updatedEvent = eventRepository.save(newEvent);
            Event returnEvent = getEvent(updatedEvent);
            User user = getUserById(user_id);
            user.getEvents().add(newEvent);
            userRepository.save(user);
            return returnEvent;
        } else {
            throw new IllegalArgumentException("User already exists in event");
        }

    }
    public Event removeUser(Integer event_id, Integer user_id) {
        server.model.Event newEvent = eventRepository.findById(event_id).orElse(null);
        if(newEvent == null) {
            throw new IllegalArgumentException("Event with given Id does not exist!");
        }
        boolean performed = false;
        if(newEvent.getUsers().contains(getUserById(user_id))) {
            newEvent.getUsers().remove(getUserById(user_id));
            performed = true;
        }
        server.model.Event updatedEvent = eventRepository.save(newEvent);
        Event returnEvent = getEvent(updatedEvent);
        if(performed) {
            User user = getUserById(user_id);
            user.getEvents().remove(newEvent);
            userRepository.save(user);
        }
        return returnEvent;
    }

    public DeferredResult<ResponseEntity<Event>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L, noContent);
        var key = new Object();
        listeners.put(key, q -> {
            res.setResult(ResponseEntity.ok(q));
        });
        res.onCompletion(() -> {
            listeners.remove(key);
        });
        return res;
    }
}


