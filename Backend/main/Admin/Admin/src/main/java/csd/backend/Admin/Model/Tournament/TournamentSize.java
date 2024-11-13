package csd.backend.Admin.Model.Tournament;

public class TournamentSize {
    private int tournamentSize = 8;

    // Getter method to retrieve the tournament size
    public int getTournamentSize() {
        return tournamentSize;
    }

    // Setter method to change the tournament size
    public void setTournamentSize(int newSize) {
        tournamentSize = newSize;
    }
}
