package csd.backend.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public String authenticateUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .map(user -> "user authorized")
                .orElse("user unauthorized");
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public String createUser(String username, String password) {

        if (userRepository.existsById(username)) {
            return "User already exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);  // to-do: hash the password
        userRepository.save(user);
        
        return "User created successfully";
    }
    public String deleteUser(String username) {
        if (userRepository.existsById(username)) {
            userRepository.deleteById(username);
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
}
