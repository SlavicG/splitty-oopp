package server.api;

import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.User;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.service.EventService;
import server.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rest/events")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
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

    // show all expenses
    @GetMapping("/{event_id}/allexpenses")
    @ResponseBody
    public List<Expense> getExpenses(@PathVariable Integer event_id){
        return eventService.allExpenses( event_id);
    }



    @GetMapping("/{event_id}/users")
    @ResponseBody
    public List<User> getAllUsers(@PathVariable Integer event_id) {
        Event event = eventService.getEventById(event_id);
        return event.getUserIds().stream().map(a -> userService.getUserById(a)).toList();
    }

    @GetMapping("/{event_id}/debts")
    @ResponseBody
    public Map<Integer,Double> getAllDebts(@PathVariable Integer event_id){
        Map<Integer,Double> mapa = eventService.getAllDebtsInEvent( event_id);
        return mapa;
    }
    @GetMapping("/{event_id}/users/{user_id}/debt")
    @ResponseBody
    public Double getDebtUser(@PathVariable Integer user_id, @PathVariable Integer event_id){
        return eventService.getDebtOfaUser(user_id, event_id);
    }

    // expenses for which user_id paid
    @GetMapping("/{event_id}/paid/user/{user_id}")
    @ResponseBody
    public List<Expense> getExpensesUserPaid(@PathVariable Integer user_id, @PathVariable Integer event_id){
        return eventService.expensesUserPaid(user_id, event_id);
    }

    // expenses that include user_id
    @GetMapping("/{event_id}/include/user/{user_id}")
    @ResponseBody
    public List<Expense> getExpenseIncludeUser(@PathVariable Integer user_id, @PathVariable Integer event_id){
        return eventService.expenseIncludeUser(user_id, event_id);
    }

    @PostMapping("/{event_id}/add_user/{user_id}")
    @ResponseBody
    public Event addUser(@PathVariable(name = "event_id") Integer event_id,
                         @PathVariable(name = "user_id") Integer user_id) {
        return eventService.addUser(event_id, user_id);
    }

    @DeleteMapping("/{event_id}/add_user/{user_id}")
    @ResponseBody
    public Event removeUser(@PathVariable(name = "event_id") Integer event_id,
                         @PathVariable(name = "user_id") Integer user_id) {
        return eventService.removeUser(event_id, user_id);
    }
}
