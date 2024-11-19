package csd.backend.Account.MS.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Long playerId) {
        super("Player not found with player Id: " + playerId);
    }

    public PlayerNotFoundException(String username) {
        super("Player not found with username: " + username);
    }
}
