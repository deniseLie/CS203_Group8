package csd.backend.Matching.MS.Player;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "PlayerOverallStatistics")
public class PlayerOverallStatistic {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int userId;
    int rankId;
    double overallAveragePlace;
    double overallFirstPlacePercentage;
    double overallKdRate;
    int totalWins;
    int totalNumberOfMatches;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private Player player;
    
    public PlayerOverallStatistic(){}
    
    public PlayerOverallStatistic(int userId, int rankId, double overallAveragePlace,
            double overallFirstPlacePercentage, double overallKdRate, int totalWins, int totalNumberOfMatches) {
        this.userId = userId;
        this.rankId = rankId;
        this.overallAveragePlace = overallAveragePlace;
        this.overallFirstPlacePercentage = overallFirstPlacePercentage;
        this.overallKdRate = overallKdRate;
        this.totalWins = totalWins;
        this.totalNumberOfMatches = totalNumberOfMatches;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    public double getOverallAveragePlace() {
        return overallAveragePlace;
    }

    public void setOverallAveragePlace(double overallAveragePlace) {
        this.overallAveragePlace = overallAveragePlace;
    }

    public double getOverallFirstPlacePercentage() {
        return overallFirstPlacePercentage;
    }

    public void setOverallFirstPlacePercentage(double overallFirstPlacePercentage) {
        this.overallFirstPlacePercentage = overallFirstPlacePercentage;
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

    public int getTotalNumberOfMatches() {
        return totalNumberOfMatches;
    }

    public void setTotalNumberOfMatches(int totalNumberOfMatches) {
        this.totalNumberOfMatches = totalNumberOfMatches;
    }

    
}
