package csd.backend.Admin.Model.User;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("player")  
public class Player extends User {

    @Column(name = "playerId", nullable = true)
    private Long playerId;

    @Column(name = "rankId", nullable = true)
    private Long rankId;

    @Column(name = "playerName", nullable = false)
    private String playerName;

    @Column(name = "profilePicture", nullable = false)
    private String profilePicture;

    // Getters and Setters
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
