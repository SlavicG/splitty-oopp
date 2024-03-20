package server.admin;

import commons.dto.Event;
import commons.dto.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.EventService;
import server.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final UserService userService;

    public AdminController(EventService eventService, UserService userService)
    {
        this.eventService=eventService;
        this.userService=userService;
    }
    @GetMapping("/dashboard")
    public List<User> getUser()
    {
        return userService.getUsers();
    }


}
