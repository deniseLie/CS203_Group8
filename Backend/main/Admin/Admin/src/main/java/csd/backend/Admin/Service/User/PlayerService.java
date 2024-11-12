package csd.backend.Admin.Service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import csd.backend.Admin.Model.User.Player;
import csd.backend.Admin.Repository.UserRepository;
import csd.backend.Admin.Repository.User.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Find player by ID
    public Player getPlayerById(Long playerId) {
        return playerRepository.findByPlayerId(playerId).orElse(null); // Return null if not found
    }
}
