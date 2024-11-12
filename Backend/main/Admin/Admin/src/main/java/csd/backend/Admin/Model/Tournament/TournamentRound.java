package csd.backend.Admin.Model.Tournament;

import jakarta.persistence.*;
import csd.backend.Admin.Model.User.*;

@Entity
public class TournamentRound {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;  // Unique round identifier
    
    @ManyToOne
    @JoinColumn(name = "tournamentId", nullable = false)
    private Tournament tournament;  // The tournament to which this round belongs
    
    @ManyToOne
    @JoinColumn(name = "firstPlayerId", nullable = false)
    private Player firstPlayer;  // The first player in the round
    
    @ManyToOne
    @JoinColumn(name = "secondPlayerId", nullable = false)
    private Player secondPlayer;  // The second player in the round
    
    @Column(name = "roundNumber", nullable = false)
    private int roundNumber;  // The round number in the tournament
    
    @ManyToOne
    @JoinColumn(name = "winnerPlayerId", nullable = true)
    private Player winnerPlayer;  // The winner of the round (nullable until the round is completed)

    // Getters and setters
    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Player getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(Player winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }
}
