package server.service;

import commons.dto.Event;
import commons.dto.User;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.UserRepository;



import java.util.Collections;
import java.util.List;
import java.util.function.Function;



@Service
public class UserService {
    private EventRepository eventRepository;

    private UserRepository userRepository;
    private Function<server.model.User, User> mapper = user -> new User(user.getId(), user.getName(), user.getEmail(), user.getIban(), user.getBic());
    protected UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getUsers() {
        return userRepository.findAll().stream().map(mapper).toList();
    }
    public User getUserById(Integer id) {
        return userRepository.findById(id).map(mapper).orElse(null);
    }
    public User createUser(User user) {
        server.model.User userEntity = new server.model.User(null, user.getName(), user.getEmail(), user.getIban(), user.getBic(), Collections.emptyList());
        server.model.User createdUser = userRepository.save(userEntity);
        return new User(createdUser.getId(), createdUser.getName(), createdUser.getEmail(), user.getIban(), user.getBic());
    }
    public User updateUser(User user) {
        server.model.User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            server.model.User updatedUser = new server.model.User(existingUser.getId(), user.getName(), user.getEmail(), user.getIban(), user.getBic(), existingUser.getEvents());
            server.model.User savedUser = userRepository.save(updatedUser);
            return mapper.apply(savedUser);
        }
        return null;
    }
    public static User getUser(server.model.User user) {
        return new User(user.getId(), user.getName(), user.getEmail(), user.getIban(), user.getBic());
    }


    //calculate a debt of a give user
    public Double getDebtOfaUser(Integer id,Integer event_id){
        double fullAmount = 0;
        Event event;
        try {
            event = (Event) eventRepository.findAll().stream().
                    filter(eventThis -> eventThis.getId().equals(event_id));
            fullAmount = event.getExpenses().stream()
                    .mapToDouble(expense -> expense.getAmount())
                    .sum();
            fullAmount = fullAmount/event.getUserIds().size();

        } catch (Exception e) {
            throw new ServiceException("error" + id,e);
        }
        double amountPayed = 0;
        //Amount of money spend on expenses in all the
        try {
            amountPayed = event.getExpenses().stream().
                    filter(expense -> expense.getId().equals(id))
                    .mapToDouble(expense -> expense.getAmount())
                    .sum();

        } catch (Exception e) {
            throw new ServiceException("error" + id,e);
        }
        double debt = fullAmount - amountPayed;
        return debt;
    }



    //calculate all debts between all users

}
