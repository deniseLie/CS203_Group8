package csd.backend.Account.MS.Exception;

public class PlayerChampionStatsNotFoundException extends RuntimeException {
    public PlayerChampionStatsNotFoundException(Long playerId) {
        super("Player champion stats not found for player with ID: " + playerId);
    }
}
