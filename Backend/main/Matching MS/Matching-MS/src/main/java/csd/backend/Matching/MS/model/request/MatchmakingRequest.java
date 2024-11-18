package csd.backend.Matching.MS.model.request;

/**
 * Represents a request to join the matchmaking system with a specific player and champion.
 */
public class MatchmakingRequest {

    // ID of the player requesting to join the matchmaking queue
    private String playerId;

    // ID of the champion the player is using in the matchmaking request
    private String championId;

    // Getters and setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getChampionId() {
        return championId;
    }

    public void setChampionId(String championId) {
        this.championId = championId;
    }
}