package com.loltournament.loginservice.service;

import com.loltournament.loginservice.model.Player;
import com.loltournament.loginservice.repository.PlayerRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    private PlayerRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void saveUser(Player player) {
        player.setAuthProvider("LOCAL");
        userRepository.save(player);
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
