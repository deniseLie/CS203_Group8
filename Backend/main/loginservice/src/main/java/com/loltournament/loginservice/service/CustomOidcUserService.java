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

        Player user = userRepository.findByEmail(googleUserInfo.getEmail()).orElseGet(() -> {
            Player newUser = new Player();
            newUser.setEmail(googleUserInfo.getEmail());
            newUser.setUsername(googleUserInfo.getName());
            newUser.setPlayername(googleUserInfo.getName());
            newUser.setAuthProvider("GOOGLE");

            long playerId = newUser.getId();
            String playerEmail = newUser.getEmail();

            String messageBody = "{\"playerId\": \"" + playerId + "\", \"email\": \"" + playerEmail + "\"}";
            String messageGroupId = "player-" + playerId;
            String actionType = "addPlayer";
            sqsService.sendMessageToQueue(sqsService.accountQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.matchmakingQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.penaltyQueueUrl, messageBody, messageGroupId, actionType);
            sqsService.sendMessageToQueue(sqsService.adminQueueUrl, messageBody, messageGroupId, actionType);
            return userRepository.save(newUser);
        });

        // Check that user and userId are not null before generating the token
        if (user == null || user.getUserId() == null) {
            throw new IllegalStateException("User or User ID is null. Cannot generate JWT token.");
        }

        // Generate a JWT for the authenticated user
        String jwtToken = jwtUtil.generateToken(googleUserInfo.getName(), user.getUserId());

        // Create a new attributes map including the JWT token
        Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());

        if (jwtToken != null) {
            attributes.put("jwtToken", jwtToken);
        } else {
            System.out.println("Warning: JWT Token is null.");
        }

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
