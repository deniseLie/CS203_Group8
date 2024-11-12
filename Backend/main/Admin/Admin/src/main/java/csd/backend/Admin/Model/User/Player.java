package csd.backend.Admin.Model.User;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("player")  // This defines the discriminator value for this subclass
public class Player extends User {

    @Column(name = "playerId", nullable = true)
    private String playerId;

    @Column(name = "playerName", nullable = false)
    private String playerName;

    @Column(name = "profilePicture", nullable = false)
    private String profilePicture;

    // Getters and Setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
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
