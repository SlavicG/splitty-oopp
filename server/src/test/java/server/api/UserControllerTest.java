package server.api;

import commons.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Assertions;

import java.sql.SQLOutput;
import java.util.Random;
public class UserControllerTest {
    public int nextInt;
    protected TestUserRepository repo;
    protected UserController controller;
    protected UserService service;


    @BeforeEach
    public void setup(){
        repo = new TestUserRepository();
        service = new UserService(repo);
        controller = new UserController(service);
    }

    @Test
    void cannotAddNullPerson(){
        User user = new User();
        var actual = controller.createUser(user);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
    @Test
    void addedPersonStatus(){
        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345");
        var actual = controller.createUser(user);
        assertEquals(OK, actual.getStatusCode());
    }

//    @Test
//    void updateUser() {
//
//
//    }
//
//    @Test
//    void getUser() {
//        User user = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345");
//        controller.createUser(user);
//        System.out.println(controller.getUser(1));
//    }
////
//    @Test
//    void getUsers() {
//        User user1 = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345");
//        User user2 = new User(1, "David", "dgogoana@tudelft.nl", "54321", "12345");
//        User user3 = new User(1, "Gogo", "dgogoana@tudelft.nl", "54321", "12345");
//        System.out.println(controller.getUsers());
//
//    }
}
