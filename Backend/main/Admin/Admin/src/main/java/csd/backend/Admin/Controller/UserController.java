package csd.backend.Admin.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import csd.backend.Admin.Model.RequestBody.*;
import csd.backend.Admin.Model.User.User;
import csd.backend.Admin.Service.*;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    //PLAYER ADMIN ACTIONS
    //to do: add jwt
    @PostMapping("/login")
    public String authenticate(@RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
    }

    // Get All users
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Create user
    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest) {
        String responseMessage = userService.createUser(
            createUserRequest.getUsername(),
            createUserRequest.getPassword(),
            createUserRequest.getRole(),
            createUserRequest.getPlayerName(),
            createUserRequest.getProfilePicture()
        );
        if (responseMessage.equals("User created successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

}

