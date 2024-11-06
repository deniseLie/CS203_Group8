package csd.backend.Admin;

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
}
