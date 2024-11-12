package com.loltournament.loginservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import com.loltournament.loginservice.repository.PlayerRepository;
import com.loltournament.loginservice.service.GoogleAttributes.GoogleUserInfo;
import com.loltournament.loginservice.util.JwtUtil;
import com.loltournament.loginservice.model.Player;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private PlayerRepository userRepository;

    @Autowired
    private SqsService sqsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oidcUser.getAttributes());

        // see what other data from userRequest or oidcUser you need

        Optional<Player> userOptional = userRepository.findByEmail(googleUserInfo.getEmail());
        if (!userOptional.isPresent()) {
            Player user = new Player();
            user.setEmail(googleUserInfo.getEmail());
            user.setUsername(googleUserInfo.getName());
            user.setPlayername(googleUserInfo.getName());
            user.setAuthProvider("GOOGLE");

            Player player = userRepository.save(user);
            long playerId = player.getId();
            String playerEmail = player.getEmail();

            String messageBody = "{\"playerId\": \"" + playerId + "\", \"email\": \"" + playerEmail + "\"}";
            String messageGroupId = "player-" + playerId;
            String actionType = "addPlayer";
            sqsService.sendMessageToQueue(sqsService.accountQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.matchmakingQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.penaltyQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.adminQueueUrl, messageBody, messageGroupId, actionType);
        }

         // Generate a JWT for the authenticated user
        String jwtToken = jwtUtil.generateToken(googleUserInfo.getName(), userOptional.get().getUserId());

        // Create a new attributes map including the JWT token
        Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
        attributes.put("jwtToken", jwtToken);

        // Create a new OidcUser with the modified attributes
        // Return a new DefaultOidcUser with the updated attributes
        return new DefaultOidcUser(
            oidcUser.getAuthorities(),
            oidcUser.getIdToken(),
            oidcUser.getUserInfo(),
            "sub" // This is the name attribute key; adjust as per your requirements
        ) {
            @Override
            public Map<String, Object> getAttributes() {
                return attributes;
            }
        };
    }
}
