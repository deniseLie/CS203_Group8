package csd.backend.Admin.Service.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import csd.backend.Admin.Model.User.Player;
import csd.backend.Admin.Repository.User.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Find player by ID
    public Player getPlayerById(Long playerId) {
        return playerRepository.findByPlayerId(playerId).orElse(null); // Return null if not found
    }

    // Update user details based on userId
    public String updatePlayerProfile(Long playerId, String playerName, String username, String email, String password) {
        Player player = getPlayerById(playerId);

        if (player != null) {

            // Update fields only if new values are provided
            if (username != null && !username.isEmpty()) {
                player.setUsername(username);
            }

            if (playerName != null && !playerName.isEmpty()) {
                player.setPlayerName(playerName);
            }

            if (email != null && !email.isEmpty()) {
                player.setEmail(email);
            }

            // Hash the new password before saving
            if (password != null && !password.isEmpty()) {
                player.setPassword(passwordEncoder.encode(password));
            }

            // Save the updated user back to the database
            playerRepository.save(player);
            return "Player profile updated successfully.";
        } else {
            return "User not found.";
        }
    }
}
