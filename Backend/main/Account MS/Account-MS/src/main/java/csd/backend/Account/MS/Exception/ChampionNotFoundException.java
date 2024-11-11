package csd.backend.Account.MS.Exception;

public class ChampionNotFoundException extends RuntimeException {
    public ChampionNotFoundException(Long championId) {
        super("Champion not found with champion Id : " + championId);
    }
}
