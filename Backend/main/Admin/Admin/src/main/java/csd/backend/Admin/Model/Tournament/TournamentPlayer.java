package csd.backend.Admin.Model.Tournament;

import jakarta.persistence.*;

@Entity
@Table(name = "TournamentPlayer")
public class TournamentPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;

    @ManyToOne
    @JoinColumn(name = "tournamentId", nullable = false)
    private Tournament tournament;

    // Getters and setters
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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}

