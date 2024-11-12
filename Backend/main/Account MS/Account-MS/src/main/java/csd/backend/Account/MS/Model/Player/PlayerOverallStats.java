package csd.backend.Account.MS.Model.Player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PlayerOverallStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playerId")
    private Long playerId;  // Relates to Player

    private Long rankId;
    private int rankPoints;
    private double overallAveragePlace;
    private double overallKdRate;
    private int totalWins;
    private int totalFirstPlaceMatches;
    private int totalNumberOfMatches;

    // One-to-one relationship with Player
    @OneToOne
    @JoinColumn(name = "playerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getRankId() {
        return rankId;
    }

    public void setRankId(Long rankId) {
        this.rankId = rankId;
    }

    public int getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(int rankPoints) {
        this.rankPoints = rankPoints;
    }

    public double getOverallAveragePlace() {
        return overallAveragePlace;
    }

    public void setOverallAveragePlace(double overallAveragePlace) {
        this.overallAveragePlace = overallAveragePlace;
    }
    public double getOverallKdRate() {
        return overallKdRate;
    }

    public void setOverallKdRate(double overallKdRate) {
        this.overallKdRate = overallKdRate;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalFirstPlaceMatches() {
        return totalFirstPlaceMatches;
    }

    public void setTotalFirstPlaceMatches(int totalFirstPlaceMatches) {
        this.totalFirstPlaceMatches = totalFirstPlaceMatches;
    }

    public int getTotalNumberOfMatches() {
        return totalNumberOfMatches;
    }

    public void setTotalNumberOfMatches(int totalNumberOfMatches) {
        this.totalNumberOfMatches = totalNumberOfMatches;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
