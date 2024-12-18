package csd.backend.Account.MS.model.rank;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "RankTable") 
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankId;
    
    private String rankName; 
    private int pointsRequired; 

    // Constructor
    public Rank() {
        // Default constructor
    }

    public Rank(Long rankId, String rankName, int pointsRequired) {
        this.rankId = rankId;
        this.rankName = rankName;
        this.pointsRequired = pointsRequired;
    }

    // Getters and setters
    public Long getRankId() {
        return rankId;
    }

    public void setRankId(Long rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }
}
