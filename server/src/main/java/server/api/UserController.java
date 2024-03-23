package server.api;

import commons.dto.User;
import org.apache.coyote.BadRequestException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/rest/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping
    @ResponseBody
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        if(user.equals(null) || user.getName()==null)
//            return ResponseEntity.badRequest().build();
//
//        return ResponseEntity.ok().build();
//    }
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User updateUser(@PathVariable Integer id, @RequestBody User user) throws BadRequestException {
        if (user.getId() != id) throw new BadRequestException();
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @ResponseBody
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @MessageMapping("/users")
    @SendTo("/topic/users")
    public User addMessage(User user) {
        User retUser = createUser(user);
        return retUser;
    }
}
