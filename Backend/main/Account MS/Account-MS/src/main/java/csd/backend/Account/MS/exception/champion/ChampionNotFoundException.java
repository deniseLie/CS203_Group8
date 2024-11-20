package csd.backend.Account.MS.exception.champion;

public class ChampionNotFoundException extends RuntimeException {
    public ChampionNotFoundException(Long championId) {
        super("Champion not found with champion Id : " + championId);
    }
}
