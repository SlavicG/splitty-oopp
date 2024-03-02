package server.api;

import commons.dto.Event;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.service.EventService;

import java.util.List;

@Controller
@RequestMapping("/rest/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @ResponseBody
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping
    @ResponseBody
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Event getEvent(@PathVariable Integer id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return event;
    }


    @PutMapping("/{id}")
    @ResponseBody
    public Event updateEvent(@PathVariable Integer id, @RequestBody Event event) throws BadRequestException {
        if (!event.getId().equals(id)) throw new BadRequestException();
        return eventService.updateEvent(event);
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public Event deleteEvent(@PathVariable Integer id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
       return eventService.deleteEvent(id);
    }


}
