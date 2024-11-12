package com.loltournament.loginservice.service;

import com.loltournament.loginservice.model.Player;
import com.loltournament.loginservice.repository.PlayerRepository;
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

    @Transactional
    public void processOAuthLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        Player player = userRepository.findByEmail(email).orElse(null);

        if (player == null) {
            player = new Player();
            player.setEmail(email);
            player.setUsername(oAuth2User.getAttribute("name"));
            player.setAuthProvider("GOOGLE");
            userRepository.save(player);
        }
    }
}
