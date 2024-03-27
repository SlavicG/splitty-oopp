package server.admin;

import commons.dto.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.EventService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    public AdminController(EventService eventService)
    {

    }

    @GetMapping("/dashboard")
    public List<User> getUser()
    {
        // Replacement of placeholder that used a removed method
        return new ArrayList<>();
    }


}
