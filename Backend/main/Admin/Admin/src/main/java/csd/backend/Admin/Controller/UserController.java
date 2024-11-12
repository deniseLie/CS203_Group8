package csd.backend.Admin.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import csd.backend.Admin.Model.User.User;
import csd.backend.Admin.Service.*;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    //PLAYER ADMIN ACTIONS
    //to do: add jwt
    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        return userService.authenticateUser(username, password);
    }
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/createUser")
    public String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        return userService.createUser(username, password, role);
    }
    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        return userService.deleteUser(userId);
    }
}

