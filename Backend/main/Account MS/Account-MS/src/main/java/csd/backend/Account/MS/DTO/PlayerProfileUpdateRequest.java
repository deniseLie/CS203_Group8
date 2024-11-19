package csd.backend.Account.MS.DTO;

public class PlayerProfileUpdateRequest {
    private Long playerId;
    private String username;
    private String playerName;
    private String email;
    private String password;
    private String profilePicture; 
    
    // Constructors
    public PlayerProfileUpdateRequest() {
    }

    public PlayerProfileUpdateRequest(Long playerId, String username, String playerName, String email, String password, String profilePicture) {
        this.playerId = playerId;
        this.username = username;
        this.playerName = playerName;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
    }
    
    // Getters and setters
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
