package csd.backend.Matching.MS.exception;

public class PlayerUpdateException extends RuntimeException {
    public PlayerUpdateException(String message) {
        super(message);
    }

    public PlayerUpdateException(Long playerId) {
        super("Error updating player with playerId: " + playerId);
    }
}
