package server.service;

import commons.dto.Event;
import commons.dto.Expense;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import server.database.UserRepository;
import server.model.Tag;
import server.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private TagRepository tagRepository;

    private Function<server.model.Tag, commons.dto.Tag> mapper2 = tag -> new commons.dto.Tag(
            tag.getId(),
            tag.getName(),
            tag.getColor(),
            tag.getEvent().getId());

    private Function<server.model.Expense, Expense> mapper = expense -> new commons.dto.Expense(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            expense.getPayer().getId(),
            expense.getDate(),
            expense.getSplitBetween(),
            expense.getTag().getId());
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
                             EventRepository eventRepository,
                             TagRepository tagRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
    }

    public List<Expense> getExpenses() {
        return expenseRepository.findAll().stream().map(mapper).toList();
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id).map(mapper).orElse(null);
    }

    public Event getEventById(Integer id) {
        server.model.Event event = eventRepository.findById(id).orElse(null);
        Event returnEvent = new Event();
        returnEvent.setId(event.getId());
        returnEvent.setTitle(event.getTitle());
        returnEvent.setUsers(getUserIds(event.getUsers()));
        if(event.getExpenses()!=null){
            returnEvent.setExpenses(event.getExpenses().stream()
                    .map(mapper).toList());
        }
        if (event.getTags() != null){
            returnEvent.setTags(tagRepository.findAll().stream().
                    map(mapper2).toList());
        }
        return returnEvent;
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


    // update all users
    public void updateAllDebtsInEvent(Integer event_id) {
        Event event = getEventById(event_id);
        List<Integer> userIds = event.getUserIds();
        for (int i = 0; i < userIds.size(); i++) {
            User user = userRepository.findById(userIds.get(i)).orElse(null);
            double newDebt = getDebtOfaUser(userIds.get(i), event_id);
            user.setDebt(newDebt);
            userRepository.save(user);
        }
    }


    public Expense createExpense(Integer eventId, Expense expense) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        server.model.Expense expenseEntity = new server.model.Expense(
                null,
                expense.getAmount(),
                expense.getDescription(),
                getUserById(expense.getPayerId()),
                expense.getDate(),
                event,
                expense.getSplitBetween(),
                getTagById(expense.getTagId()));
//        event.getExpenses().add(expenseEntity);
        server.model.Expense createdEntity = expenseRepository.save(expenseEntity);
        Expense expenseCreated = new Expense(createdEntity.getId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getPayerId(),
                expense.getDate(),
                expense.getSplitBetween(),
                expense.getTagId());
        updateAllDebtsInEvent(eventId);
        return  expenseCreated;


    }

    public Expense updateExpense(Integer eventId, Expense expense) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        server.model.Expense existingExpense = expenseRepository.findById(expense.getId()).orElse(null);
        if (existingExpense.getEvent().getId() != eventId)
            throw new IllegalArgumentException("Expense doesn't belong with provided event");
        if (existingExpense != null) {
            existingExpense.setAmount(expense.getAmount());
            existingExpense.setDate(expense.getDate());
            existingExpense.setDescription(expense.getDescription());
            existingExpense.setPayer(getUserById(expense.getPayerId()));
            existingExpense.setTag(getTagById(expense.getTagId()));
            server.model.Expense savedExpense = expenseRepository.save(existingExpense);
            Expense expenseUpdated=  mapper.apply(savedExpense);
            updateAllDebtsInEvent(eventId);

            return expenseUpdated;

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
        User user = userRepository.findById(it).orElse(null);
        if (user == null) throw new IllegalArgumentException("User not found. ID: " + it);
        return user;
    }

    private Tag getTagById(Integer it) {
        Tag tag = tagRepository.findById(it).orElse(null);
        if (tag == null) throw new IllegalArgumentException("Tag not found. ID: " + it);
        return tag;
    }

    private List<server.model.Expense> getExpenses(List<commons.dto.Expense> expenses) {
        return expenses.stream().map(mapperInv).toList();
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
