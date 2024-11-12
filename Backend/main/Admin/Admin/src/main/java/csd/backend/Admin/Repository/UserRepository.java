package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Admin.Model.User.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username); 
    Optional<User> findById(Long id);
    
    boolean existsByUserId(Long userId);
    boolean existsByUsername(String username);
    
}

