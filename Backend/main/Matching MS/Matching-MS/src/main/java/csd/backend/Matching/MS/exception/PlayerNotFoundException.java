package csd.backend.Matching.MS.exception;

public class PlayerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // Default constructor with a default message
    public PlayerNotFoundException() {
        super("Player not found");
    }

    // Constructor with Player ID
    public PlayerNotFoundException(Long playerId) {
        super("Player " + playerId + " doesn't exist");
    }

    // Constructor with a custom message
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
