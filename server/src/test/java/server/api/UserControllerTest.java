package server.api;

import commons.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private TestUserRepository testUserRepository;
    private TestEventRepository testEventRepository;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void setup() {
        testUserRepository = new TestUserRepository();
        testEventRepository = new TestEventRepository();
        userService = new UserService(testEventRepository, testUserRepository);
        userController = new UserController(userService);
    }

    @Test
    void createUser() {
        User user = new User();
        user.setName("David");
        server.model.User userEntity = new server.model.User(null,
                user.getName(), user.getEmail(), user.getIban(), user.getBic(), Collections.emptyList());
        assertEquals(userController.createUser(user).toString(),
                "User{id=1, name='David', email='null', iban='null', bic='null'}");
    }

    //    @Test
//    void updateUser() {
//    }
//
    @Test
    void getUser() {
        User user1 = new User();
        user1.setName("David");
        userController.createUser(user1);
        User user = new User();
        user.setName("Slavic");
        userController.createUser(user);
        User user2 = new User();
        user2.setName("DavidGogoana");
        userController.createUser(user2);
        User user3 = new User();
        user3.setName("DavidDavid");
        userController.createUser(user3);
        User user4 = new User();
        user4.setName("Slavicc");
        userController.createUser(user4);
        User user5 = new User();
        user5.setName("DavidGogoanaa");
        userController.createUser(user5);
        assertEquals(userController.getUser(1).toString(),
                "User{id=1, name='David', email='null', iban='null', bic='null'}");
    }

    @Test
    void getUsers() {
        User user1 = new User();
        user1.setName("David");
        userController.createUser(user1);
        User user = new User();
        user.setName("SlavicG");
        userController.createUser(user);
        User user2 = new User();
        user2.setName("DavidGogoana");
        userController.createUser(user2);
        User user3 = new User();
        user3.setName("DavidDavid");
        userController.createUser(user3);
        User user4 = new User();
        user4.setName("Slavicc");
        userController.createUser(user4);
        User user5 = new User();
        user5.setName("DavidGogoanaa");
        userController.createUser(user5);
        assertEquals(userController.getUsers().toString(),
                "[User{id=1, name='David', email='null', iban='null', bic='null'}, " +
                        "User{id=2, name='SlavicG', email='null', iban='null', bic='null'}, " +
                        "User{id=3, name='DavidGogoana', email='null', iban='null', bic='null'}, " +
                        "User{id=4, name='DavidDavid', email='null', iban='null', bic='null'}, " +
                        "User{id=5, name='Slavicc', email='null', iban='null', bic='null'}, " +
                        "User{id=6, name='DavidGogoanaa', email='null', iban='null', bic='null'}]");
    }
}