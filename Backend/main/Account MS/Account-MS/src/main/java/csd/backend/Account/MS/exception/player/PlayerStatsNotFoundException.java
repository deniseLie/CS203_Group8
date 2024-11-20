package csd.backend.Account.MS.exception.player;

public class PlayerStatsNotFoundException extends RuntimeException {
    public PlayerStatsNotFoundException(Long playerId) {
        super("Player statistics not found for player with ID: " + playerId);
    }
}
