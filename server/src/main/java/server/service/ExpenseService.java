package server.service;

import commons.dto.Event;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import commons.dto.Expense;
import server.database.UserRepository;
import server.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private Function<server.model.Expense, Expense> mapper = expense -> new commons.dto.Expense(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            expense.getPayer().getId(),
            expense.getDate());
    private Function<Expense, server.model.Expense> mapperInv = expense -> new server.model.Expense(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            getUserById(expense.getPayerId()),
            expense.getDate());

    private server.model.Expense mapperInv(Expense expense) {
        return new server.model.Expense(
                expense.getId(),
                expense.getAmount(),
                expense.getDescription(),
                getUserById(expense.getPayerId()),
                expense.getDate());
    }

    protected ExpenseService(ExpenseRepository expenseRepository,
                             UserRepository userRepository,
                             EventRepository eventRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<Expense> getExpenses() {
        return expenseRepository.findAll().stream().map(mapper).toList();
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id).map(mapper).orElse(null);
    }

    @Transactional
    public Expense createExpense(Integer eventId, Expense expense) {
        server.model.Event event = eventRepository.getById(eventId);
        server.model.Expense expenseEntity = new server.model.Expense(
                null,
                expense.getAmount(),
                expense.getDescription(),
                getUserById(expense.getPayerId()),
                expense.getDate(),
                event);
//        event.getExpenses().add(expenseEntity);
        List<server.model.Expense> listExpensesPrev = event.getExpenses();
        listExpensesPrev.add(expenseEntity);
        event.setExpenses(listExpensesPrev);

        eventRepository.save(event);
        server.model.Expense createdEntity = expenseRepository.save(expenseEntity);
        return new Expense(createdEntity.getId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getPayerId(),
                expense.getDate());
    }

    public Expense updateExpense(Integer eventId, Expense expense) {
        server.model.Event event = eventRepository.getById(eventId);
        server.model.Expense existingExpense = expenseRepository.findById(expense.getId()).orElse(null);
        if (existingExpense.getEvent().getId() != eventId)
            throw new IllegalArgumentException("Expense doesn't belong with provided event");
        if (existingExpense != null) {
            existingExpense.setAmount(expense.getAmount());
            existingExpense.setDate(expense.getDate());
            existingExpense.setDescription(expense.getDescription());
            existingExpense.setPayer(getUserById(expense.getPayerId()));
            server.model.Expense savedExpense = expenseRepository.save(existingExpense);
            return mapper.apply(savedExpense);
        }
        return null;
    }

    public List<Expense> getExpenses(Integer eventId) {
        return expenseRepository.findAllByEventId(eventId).stream().map(mapper).toList();
    }

    public Expense deleteExpense(Integer eventId, Integer expenseId) {
        Optional<server.model.Expense> expense = expenseRepository.findById(expenseId);
        return expense.map(value -> {
            if (eventId != value.getEvent().getId()) {
                throw new IllegalArgumentException("Expense doesn't belong to given event");
            }
            expenseRepository.deleteById(expenseId);
            return mapper.apply(value);
        }).orElseThrow(() -> new IllegalArgumentException("Expense to be deleted not found"));
    }

    private User getUserById(Integer it) {
        User user = userRepository.getById(it);
        if (user == null) throw new IllegalArgumentException("User not found. ID: " + it);
        return user;
    }

    private List<server.model.Expense> getExpenses(List<commons.dto.Expense> expenses) {
        return expenses.stream().map(mapperInv).toList();
    }

    private Event getEvent(server.model.Event it) {
        return new Event(it.getId(), it.getTitle(), getUserIds(it.getUsers()), List.of());
    }

    private server.model.Event getModelEvent(Event event) {
        server.model.Event newEvent = new server.model.Event();
        newEvent.setId(event.getId());
        newEvent.setTitle(event.getTitle());
        newEvent.setUsers(getUsers(event.getUserIds()));
        return newEvent;
    }

    private List<Integer> getUserIds(List<User> users) {
        return users.stream().map(it -> it.getId()).toList();
    }

    private List<User> getUsers(List<Integer> userIds) {
        return userIds.stream().map(it -> getUserById(it)).toList();
    }
}
