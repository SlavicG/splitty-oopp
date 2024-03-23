package server.service;


import commons.dto.User;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Service
public class UserService {
    private EventRepository eventRepository;

    private UserRepository userRepository;

    public UserService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    private Function<server.model.User, User> mapper = user ->
            new User(user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getIban(),
                    user.getBic());

    public List<User> getUsers() {
        return userRepository.findAll().stream().map(mapper).toList();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).map(mapper).orElse(null);
    }

    public User createUser(User user) {
        server.model.User userEntity = new server.model.User(null,
                user.getName(),
                user.getEmail(),
                user.getIban(),
                user.getBic(),
                new ArrayList<>());
        server.model.User createdUser = userRepository.save(userEntity);
        return new User(createdUser.getId(),
                createdUser.getName(),
                createdUser.getEmail(),
                user.getIban(),
                user.getBic());
    }

    public User updateUser(User user) {
        server.model.User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            server.model.User updatedUser = new server.model.User(existingUser.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getIban(),
                    user.getBic(),
                    existingUser.getEvents());
            server.model.User savedUser = userRepository.save(updatedUser);
            return mapper.apply(savedUser);
        }
        return null;
    }

    public static User getUser(server.model.User user) {
        return new User(user.getId(), user.getName(), user.getEmail(), user.getIban(), user.getBic());
    }


}
