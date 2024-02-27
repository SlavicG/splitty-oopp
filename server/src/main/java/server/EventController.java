package server;


import commons.dto.Event;
import commons.dto.User;
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

    @PostMapping
    @ResponseBody
    public Event createEvent(@RequestBody Event event) {
        System.out.println("Received event: " + event);
        return eventService.createEvent(event);
    }
    @GetMapping("/{id}/users/{user_id}")
    @ResponseBody
    public User getUserFromEvent(@PathVariable String id, @PathVariable Integer user_id){
        User user = eventService.getUserFromEventById(id,user_id);
        return user;
    }


    @GetMapping("/{id}")
    @ResponseBody
    public Event getEvent (@PathVariable String id) {
        Event event = eventService.getEventById(id);
        if(event == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return event;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Event updateEvent (@PathVariable String id, @RequestBody Event event) throws BadRequestException {

        if(!event.getId().equals( id)) throw new BadRequestException();
        return eventService.updateEvent(event);
    }



    @GetMapping
    @ResponseBody
    public List<Event> getEvent() {
        return eventService.getEvent();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteEvent(@PathVariable String id){
        Event event = eventService.getEventById(id);
        if(event == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        eventService.deleteEvent(id);

        return "Deleted event: " +eventService.getEvent()+
                "successfully!";
    }




}
