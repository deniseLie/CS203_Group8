package csd.backend.Account.MS.Model;

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

    // Change userId to Long for consistency with Player's Id type
    private Long userId;  // Relates to Player

    private int rankId;
    private int rankPoints;
    private double overallAveragePlace;
    // private double overallFirstPlacePercentage;
    private double overallKdRate;
    private int totalWins;
    private int totalFirstPlaceMatches;
    private int totalNumberOfMatches;

    // One-to-one relationship with Player
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
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
