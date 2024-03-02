package server.service;

import org.springframework.stereotype.Service;
import server.database.EventRepository;
import commons.dto.Event;
import server.database.UserRepository;
import server.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
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

    private List<User> getUsers(List<Integer> userIds) {
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
    private static Event getEvent(server.model.Event it) {
        return new Event(it.getId(), it.getTitle(), getUserIds(it.getUsers()), List.of());
    }
}


