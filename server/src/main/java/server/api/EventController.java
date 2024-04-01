package server.api;

import commons.dto.Event;
import commons.dto.Expense;
import commons.dto.User;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import server.service.EventService;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
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

    @PostMapping("/{event_id}/users")
    @ResponseBody
    public User addUser(@PathVariable(name = "event_id") Integer event_id, @RequestBody User user) {
        return eventService.addUser(event_id, user);
    }

    @DeleteMapping("/{event_id}/users/{user_id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable(name = "event_id") Integer eventId,
                         @PathVariable(name = "user_id") Integer userId) {
        eventService.removeUser(eventId, userId);
    }

    @GetMapping("/{event_id}/users/{user_id}")
    @ResponseBody
    public User getUser(@PathVariable(name = "event_id") Integer eventId,
                        @PathVariable(name = "user_id") Integer userId) {
        return eventService.getUser(eventId, userId);
    }

    @GetMapping("/{event_id}/users")
    @ResponseBody
    public List<User> getUsers(@PathVariable(name = "event_id") Integer eventId) {
        return eventService.getUsers(eventId);
    }

    @PutMapping("/{event_id}/users/{user_id}")
    @ResponseBody
    public User updateUser(@PathVariable(name = "event_id") Integer eventId,
                           @PathVariable(name = "user_id") Integer userId,
                           @RequestBody User user) {
        return eventService.updateUser(eventId, userId, user);
    }

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<Event>> getUpdates() {
        return eventService.getUpdates();
    }

    @GetMapping("/{event_id}/settle_debts")
    @ResponseBody
    public List<User> settleAllUsers(@PathVariable(name = "event_id") Integer eventId) {
        return eventService.settleAllDebtsEvent(eventId);
    }
    @GetMapping("/{event_id}/settle_debts/users/{user_id}")
    @ResponseBody
    public User settleUser(@PathVariable(name = "event_id") Integer eventId,
                                     @PathVariable(name = "user_id") Integer userId) {
        return eventService.settleDebtUser(eventId, userId);
    }
    @GetMapping("/{event_id}/settle_debts/expenses/{expense_id}")
    @ResponseBody
    public List<User> settleExpense(@PathVariable(name = "event_id") Integer eventId,
                                    @PathVariable(name = "expense_id") Integer userId) {
        return eventService.settleExpense(eventId, userId);
    }

}
