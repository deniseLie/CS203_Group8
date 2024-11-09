package csd.backend.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //to do: add jwt
    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        return adminService.authenticateUser(username, password);
    }
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
    @PostMapping("/createUser")
    public String createUser(@RequestParam String username, @RequestParam String password) {
        return adminService.createUser(username, password);
    }
    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String username) {
        return adminService.deleteUser(username);
    }
}

