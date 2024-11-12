package com.loltournament.loginservice.service;

import com.loltournament.loginservice.model.Player;
import com.loltournament.loginservice.repository.PlayerRepository;
import com.loltournament.loginservice.security.SecurityConfig;
import com.loltournament.loginservice.exception.UserNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    private PlayerRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDetails loadUserById(Long userId) throws UserNotFoundException {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public Long saveUser(Player player) {
        player.setAuthProvider("LOCAL");
        Player savedPlayer = userRepository.save(player);
        return savedPlayer.getUserId();
    }

    public Player updateUser(Long userId, String email, String password, String playerName, String username) throws UserNotFoundException {
        return userRepository.findById(userId)
            .map(existingPlayer -> {
                // Update fields as needed
                existingPlayer.setEmail(email);
                existingPlayer.setUsername(username);
                existingPlayer.setPlayername(playerName);
                
                // Update password if provided
                if (password != null && !password.isEmpty()) {
                    existingPlayer.setPassword(new SecurityConfig().passwordEncoder().encode(password)); // Ensure password encoding
                }
    
                // Save and return the updated player
                return userRepository.save(existingPlayer);
            })
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
