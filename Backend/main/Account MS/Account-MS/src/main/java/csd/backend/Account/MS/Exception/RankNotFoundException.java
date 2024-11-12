package csd.backend.Account.MS.Exception;

public class RankNotFoundException extends RuntimeException {
    public RankNotFoundException(Long rankId) {
        super("Rank not found with ID: " + rankId);
    }
}
