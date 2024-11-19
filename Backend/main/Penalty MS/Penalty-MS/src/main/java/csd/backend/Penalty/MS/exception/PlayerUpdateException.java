package csd.backend.Penalty.MS.exception;

public class PlayerUpdateException extends RuntimeException {
    public PlayerUpdateException(String message) {
        super(message);
    }

    public PlayerUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
