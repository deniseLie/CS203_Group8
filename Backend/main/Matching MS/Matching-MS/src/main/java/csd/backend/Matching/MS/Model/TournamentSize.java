package csd.backend.Matching.MS.Model;

public class TournamentSize {
    private static int tournamentSize = 8; // Default value is 8

    // Getter method to retrieve the tournament size
    public static int getTournamentSize() {
        return tournamentSize;
    }

    // Setter method to change the tournament size
    public static void setTournamentSize(int newSize) {
        tournamentSize = newSize;
    }
}
