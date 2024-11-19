package csd.backend.Account.MS.exception.tournament;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(Long tournamentId) {
        super("Tournament Id not found for player with ID: " + tournamentId);
    }

    public TournamentNotFoundException(String message) {
        super(message);
    }
}
