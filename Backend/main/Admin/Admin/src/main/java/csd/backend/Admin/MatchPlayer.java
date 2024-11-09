package csd.backend.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "MatchPlayers")
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerId;

    @ManyToOne
    @JoinColumn(name = "matchId", nullable = false)
    private Match match;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}

