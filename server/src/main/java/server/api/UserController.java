package server.api;

import commons.dto.User;
import org.apache.coyote.BadRequestException;

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
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User updateUser (@PathVariable Integer id, @RequestBody User user) throws BadRequestException{
        if(user.getId() != id) throw new BadRequestException();
        return userService.updateUser(user);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public User getUser (@PathVariable Integer id) {
        return userService.getUserById(id);
    }
    @GetMapping
    @ResponseBody
    public List<User> getUsers () {
        return userService.getUsers();
    }

    @GetMapping("/debts/{id}/events/{event_id}")
    @ResponseBody
    public Double getDebtUser(@PathVariable Integer id, @PathVariable Integer even_id){
        return userService.getDebtOfaUser(id, even_id);
    }


}
