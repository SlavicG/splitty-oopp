package server.service;

import server.model.Event;
import server.model.Expense;
import server.model.User;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventService {
    private EventRepository eventRepository;

    private Function<server.model.User, User> userM = user -> new User(user.getId(), user.getName(), user.getEmail(), user.getIban(), user.getBic());
    private Function<server.model.Expense, Expense> expenseM =
            expense -> new Expense(expense.getId(),  expense.getAmount(),expense.getDescription(),
                    userM.apply(expense.getPayer()),expense.getDate(), null);
    private final Function<server.model.Event, Event> eventM = event ->
            new Event(
                    event.getId(),
                    event.getTitle(),
                    event.getUsers().stream().map(userM).collect(Collectors.toList()),
                    event.getExpenses().stream().map(expenseM).collect(Collectors.toList())
            );

    protected EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    public List<Event> getEvent() {
        return eventRepository.findAll().stream().map(eventM).toList();
    }

    public Event getEventById(String id) {

        return eventRepository.findById(id).map(eventM).orElse(null);
    }

public Event createEvent(Event event) {
    List<User> users = event.getUsers();
    if (users == null) {
        users = new ArrayList<>();
    }
    server.model.Event eventObj = new server.model.Event(
            null,
            event.getTitle(),
            event.getUsers(),
            event.getExpenses()
    );
    server.model.Event newEvent = eventRepository.save(eventObj);



    return newEvent;
}
    public Event deleteEvent(String  id){
        Optional<server.model.Event> eventToDelete  = eventRepository.findById(id);
        if(eventToDelete.isPresent()){
            server.model.Event eventEntity= eventToDelete.get();
            eventRepository.delete(eventEntity);
            return eventEntity;
        }
        return null;
    }

    public Event updateEvent(Event event){
        Optional<server.model.Event> eventToUpdate = eventRepository.findById(String.valueOf(event.getId()));
        if (eventToUpdate.isPresent()) {
            server.model.Event eventEntity = eventToUpdate.get();
            eventEntity.setTitle(event.getTitle());
            server.model.Event savedEvent = eventRepository.save(eventEntity);
            return eventM.apply(savedEvent);
        }
        return null;
    }


    public User getUserFromEventById(String eventId, Integer userId){
        Event eventById =  eventRepository.findById(eventId).map(eventM).orElse(null);
        if(eventById == null){
            return null;
        }
        return eventById.getUsers().stream().filter(user ->user.getId().equals(userId)).findFirst().orElse(null);

    }

}


