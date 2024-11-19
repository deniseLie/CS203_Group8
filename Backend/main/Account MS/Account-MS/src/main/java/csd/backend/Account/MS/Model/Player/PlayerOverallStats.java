package csd.backend.Account.MS.model.player;

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

    // No-argument constructor (JPA requirement)
    public PlayerOverallStats() {
    }

    // One-to-one relationship with Player
    @OneToOne
    @JoinColumn(name = "playerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    // Constructor for all fields except id
    public PlayerOverallStats(Long playerId, Long rankId, int rankPoints, double overallAveragePlace, 
                            double overallKdRate, int totalWins, int totalFirstPlaceMatches, int totalNumberOfMatches) {
        this.playerId = playerId;
        this.rankId = rankId;
        this.rankPoints = rankPoints;
        this.overallAveragePlace = overallAveragePlace;
        this.overallKdRate = overallKdRate;
        this.totalWins = totalWins;
        this.totalFirstPlaceMatches = totalFirstPlaceMatches;
        this.totalNumberOfMatches = totalNumberOfMatches;
    }

    // Constructor for all fields (including id)
    public PlayerOverallStats(Long id, Long playerId, Long rankId, int rankPoints, double overallAveragePlace, 
                            double overallKdRate, int totalWins, int totalFirstPlaceMatches, int totalNumberOfMatches, Player player) {
        this.id = id;
        this.playerId = playerId;
        this.rankId = rankId;
        this.rankPoints = rankPoints;
        this.overallAveragePlace = overallAveragePlace;
        this.overallKdRate = overallKdRate;
        this.totalWins = totalWins;
        this.totalFirstPlaceMatches = totalFirstPlaceMatches;
        this.totalNumberOfMatches = totalNumberOfMatches;
        this.player = player;
    }

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
