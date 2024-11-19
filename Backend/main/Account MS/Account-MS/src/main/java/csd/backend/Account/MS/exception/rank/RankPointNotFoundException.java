package csd.backend.Account.MS.exception.rank;

public class RankPointNotFoundException extends RuntimeException {
    public RankPointNotFoundException(int points) {
        super("Rank not found for points: " + points);
    }
}
