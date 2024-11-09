package com.loltournament.loginservice.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.loltournament.loginservice.model.Player;
import com.loltournament.loginservice.repository.PlayerRepository;

@RestController
public class OAuth2Controller {
    private final OAuth2AuthorizedClientService clientService;

    @Autowired
    public OAuth2Controller(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }
    
    @Autowired
    private PlayerRepository playerRepository;


    @GetMapping("/login/oauth2/code/{provider}")
    public RedirectView loginSuccess(@PathVariable String provider, OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName());

        OAuth2User oauth2User = authenticationToken.getPrincipal();
        String userEmail = (String) oauth2User.getAttributes().get("email");
        String userName = (String) oauth2User.getAttributes().get("name");

        Optional<Player> existingPlayer = playerRepository.findByEmail(userEmail);
        if (!existingPlayer.isPresent()) {
            Player player = new Player();
            player.setEmail(userEmail);
            player.setUsername(userName);
            player.setPlayername(userName);
            player.setAuthProvider("GOOGLE");
        }

        // Handle storing the user information and token, then redirect to a successful
        // login page
        // For example, store user details in your database and generate a JWT token for
        // further authentication.

        return new RedirectView("/login-success");
    }
}
