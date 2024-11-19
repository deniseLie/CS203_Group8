package csd.backend.Account.MS.exception;

public class PlayerRegisterExisted extends RuntimeException {
    public PlayerRegisterExisted(String username) {
        super("Player with username " + username + " already exists.");
    }
}