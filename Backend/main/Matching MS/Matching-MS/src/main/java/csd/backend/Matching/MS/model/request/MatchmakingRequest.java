package csd.backend.Matching.MS.model.request;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to join the matchmaking system with a specific player and champion.
*/
public class MatchmakingRequest {

    // ID of the player requesting to join the matchmaking queue
    @NotNull(message = "Player ID is required")
    private Long playerId;

    // ID of the champion the player is using in the matchmaking request
    @NotNull(message = "Champion ID is required")
    private Long championId;

    // Speed Up queue flag (true or false)
    private boolean isSpeedUp;

    // Getters and setters
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getChampionId() {
        return championId;
    }

    public void setChampionId(Long championId) {
        this.championId = championId;
    }

    public boolean getIsSpeedUp() {
        return isSpeedUp;
    }

    public void setIsSpeedUp(boolean isSpeedUp) {
        this.isSpeedUp = isSpeedUp;
    }
}