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
import server.model.User;

import java.util.*;
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
            expense.getTag().getId());
    private Function<server.model.Tag, commons.dto.Tag> mapper2 = tag -> new commons.dto.Tag(
            tag.getId(),
            tag.getName(),
            tag.getR(),
            tag.getG(),
            tag.getB(),
            tag.getEvent().getId());

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
        newEvent.setUsers(new ArrayList<>());

        server.model.Event createdEvent = eventRepository.save(newEvent);
//
//        TagService tagService = new TagService(tagRepository, eventRepository, userRepository);
//        tagService.createTag(createdEvent.getId(), new commons.dto.Tag(1, "food",
//                255, 255, 0, createdEvent.getId()));
//        tagService.createTag(createdEvent.getId(), new commons.dto.Tag(2, "entrance fees",
//                255, 0, 255, createdEvent.getId()));
//        tagService.createTag(createdEvent.getId(), new commons.dto.Tag(3, "travel",
//                0, 255, 255, createdEvent.getId()));
        Event returnEvent = getEvent(createdEvent);

        listeners.forEach((k, l) -> {
            l.accept(returnEvent);
        });
        return ResponseEntity.ok(returnEvent);
    }


    public Event getEventById(Integer id) {
        server.model.Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with given ID does not exist.");
        }
        Event returnEvent = new Event();
        returnEvent.setId(event.getId());
        returnEvent.setTitle(event.getTitle());
        returnEvent.setUsers(event.getUsers().stream().map(User::getId).toList());
        if(event.getExpenses()!=null){
            returnEvent.setExpenses(event.getExpenses().stream()
                    .map(mapper).toList());
        }
        if (event.getTags() != null){
            returnEvent.setTags(tagRepository.findAll().stream().
                    filter(x -> Objects.equals(x.getEvent().getId(), id)).map(mapper2).toList());
        }
        return returnEvent;
    }


    public Event updateEvent(Event event) {
        server.model.Event newEvent = eventRepository.findById(event.getId()).orElse(null);
        if (newEvent == null) {
            throw new IllegalArgumentException("Event with given ID does not exist.");
        }
        newEvent.setTitle(event.getTitle());
        server.model.Event updatedEvent = eventRepository.save(newEvent);

        return getEvent(updatedEvent);
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

        return new Event(it.getId(), it.getTitle(), it.getUsers().stream().map(User::getId).toList(),
                it.getExpenses().stream().map(mapperExpense).toList(),
                tagRepository.findAll().stream().filter(x -> x.getEvent().getId() == it.getId()).map(mapper2).toList());
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

    public commons.dto.User addUser(Integer eventId, commons.dto.User user) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with provided ID does not exist.");
        }

        User dbUser = new User(user, event);
        dbUser.setDebt(0.0);
        user.setDebt(0.0);
        event.getUsers().add(dbUser);
        eventRepository.save(event);
        user.setId(dbUser.getId());
        return user;
    }

    public void removeUser(Integer eventId, Integer userId) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with provided ID does not exist.");
        }
        userRepository.deleteById(userId);
    }

    public commons.dto.User getUser(Integer eventId, Integer userId) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with provided ID does not exist.");
        }
        var user = event.getUsers().stream().filter(u -> u.getId().equals(userId)).findAny().orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User with provided ID does not exist or is not part of this event.");
        }
        return new commons.dto.User(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getIban(),
            user.getBic(),
            user.getDebt()
        );
    }

    public List<commons.dto.User> getUsers(Integer eventId) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with provided ID does not exist.");
        }
        return event.getUsers().stream().map(User::toCommonUser).toList();
    }

    public commons.dto.User updateUser(Integer eventId, Integer userId, commons.dto.User user) {
        server.model.Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with provided ID does not exist.");
        }
        var oldUser = event.getUsers().stream().filter(u -> u.getId().equals(userId)).findAny().orElse(null);
        if (oldUser == null) {
            throw new IllegalArgumentException("User with provided ID does not exist or is not part of this event.");
        }
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setIban(user.getIban());
        oldUser.setBic(user.getBic());
        userRepository.save(oldUser);
        return oldUser.toCommonUser();
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
    private Function<server.model.Expense, Expense> mapperExpense = expense -> new commons.dto.Expense(
            expense.getId(),
            expense.getAmount(),
            expense.getDescription(),
            expense.getPayer().getId(),
            expense.getDate(),
            expense.getSplitBetween(),
            expense.getTag().getId());

    //settle all debts in an event
    public List<commons.dto.User> settleAllDebtsEvent(Integer eventId){
        List<commons.dto.User> userList = getUsers(eventId);
        userList.forEach(user -> user.setDebt(0.0));
        List<User> usersDatabase = new ArrayList<>();
        for(int i=0;i<userList.size();i++){
            User user = userRepository.findById(userList.get(i).getId()).orElse(null);
            assert user != null;
            user.setDebt(0.0);
            usersDatabase.add(user);

        }
        userRepository.saveAll(usersDatabase);
        return userList;
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

    //settle all debts for a given user in an event
    public commons.dto.User settleDebtUser(Integer eventId, Integer userId){

        commons.dto.User userC = getUser(eventId,userId);

        Event event = getEventById(eventId);
        List<Expense> listOfExpenses = event.getExpenses().stream().
                filter(expense -> expense.getSplitBetween().contains(userId)).
                filter(expense -> !expense.getPayerId().equals(userId)).
                collect(Collectors.toList());

        for(Expense e: listOfExpenses){
            Double debtInExpense = e.getAmount()/e.getSplitBetween().size();
            commons.dto.User userToUpdateDto = getUser(eventId, e.getPayerId());
            userToUpdateDto.setDebt(userToUpdateDto.getDebt() + debtInExpense);
            User userToUpdate = userRepository.findById(e.getPayerId()).orElse(null);
            userToUpdate.setDebt(userToUpdate.getDebt() + debtInExpense);
            userRepository.save(userToUpdate);

        }
        Double debtRn = userC.getDebt()>0?0.0:userC.getDebt();
        userC.setDebt(debtRn);
        User userToUpdate = userRepository.findById(userId).orElse(null);
        userToUpdate.setDebt(debtRn);
        userRepository.save(userToUpdate);
        return userC;


    }
    // Settle debts for one expense in an event
    public List<commons.dto.User> settleExpense(Integer eventId, Integer expense_id){
        server.model.Expense expense = expenseRepository.findById(expense_id).orElse(null);
        Event event = getEventById(eventId);
        Expense expenseC = expenseRepository.findById(expense_id).map(mapper).orElse(null);

        Double debtPPerson = expense.getAmount()/(expenseC.getSplitBetween().size());

        for(Integer u: expense.getSplitBetween()){
            if(!expense.getPayer().getId().equals(u)) {


                commons.dto.User userToUpdateDto = getUser(eventId, u);
                userToUpdateDto.setDebt(userToUpdateDto.getDebt() - debtPPerson);

                User user = userRepository.findById(u).orElse(null);
                user.setDebt(user.getDebt() - debtPPerson);
                userRepository.save(user);

                commons.dto.User payerToUpdateDto = getUser(eventId,expense.getPayer().getId());
                payerToUpdateDto.setDebt(payerToUpdateDto.getDebt() + debtPPerson);

                User payer = userRepository.findById(expense.getPayer().getId()).orElse(null);
                payer.setDebt(payer.getDebt() + debtPPerson);
                userRepository.save(payer);
            }
        }
        return getUsers(eventId);

    }

    // settle debt between person A - paid and B - to settle
    public List<commons.dto.User> settleAB(Integer eventId, Integer user1_id, Integer user2_id){
        List<Expense> expensesA = expensesUserPaid(user1_id, eventId);
        List<Expense> expensesB = expenseIncludeUser(user2_id, eventId);
        List<Expense> expensesAB = expensesA.stream()
                .filter(expense -> expensesB.contains(expense))
                .toList();

        if(!expensesAB.isEmpty()){
            double debtUserB = expensesAB.stream()
                    .mapToDouble(expense -> expense.getAmount()/expense.getSplitBetween().size())
                    .sum();


            commons.dto.User userToUpdateDto = getUser(eventId, user2_id);
            userToUpdateDto.setDebt(userToUpdateDto.getDebt() - debtUserB);

            User user = userRepository.findById(user2_id).orElse(null);
            user.setDebt(user.getDebt() - debtUserB);
            userRepository.save(user);

            commons.dto.User payerToUpdateDto = getUser(eventId,user1_id);
            payerToUpdateDto.setDebt(payerToUpdateDto.getDebt() + debtUserB);

            User payer = userRepository.findById(user1_id).orElse(null);
            payer.setDebt(payer.getDebt() + debtUserB);
            userRepository.save(payer);


        }
        List<commons.dto.User> userAB = new ArrayList<>();
        userAB.add(getUser(eventId,user1_id));
        userAB.add(getUser(eventId,user2_id));


        return userAB;

    }

    // show users who still owe money
    public List<commons.dto.User> oweMoney(Integer eventId){

        Event event = getEventById(eventId);

        List<commons.dto.User> listUser = new ArrayList<>();

        for(Integer u: event.getUserIds()){
            if(getUser(eventId,u).getDebt()>0.0){
                listUser.add(getUser(eventId,u));
            }
        }

        return listUser;
    }

    public Map<commons.dto.User, Double> getAllDebtsInEventUser(Integer event_id) {
        Event event = getEventById(event_id);
        server.model.Event eventForUsers = eventRepository.getById(event_id);
        List<Integer> userIds = event.getUserIds();
        List<commons.dto.User> users = userIds.stream().map(id -> getUser(event_id, id)).toList();
        Map<commons.dto.User, Double> mapa = new HashMap<>();
        for (int i = 0; i < userIds.size(); i++) {
            mapa.put(users.get(i), getDebtOfaUser(userIds.get(i), event_id));
        }
        return mapa;
    }

}


