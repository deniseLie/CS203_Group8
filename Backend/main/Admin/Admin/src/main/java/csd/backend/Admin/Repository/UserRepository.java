package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import csd.backend.Admin.Model.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameAndPassword(String username, String password);
}

