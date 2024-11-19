package csd.backend.Account.MS.model.tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentId;

    private int tournamentSize;
    private LocalDateTime timestampStart;

    // Constructor
    public Tournament() {}
    
    public Tournament(Long tournamentId, int tournamentSize, LocalDateTime timestampStart) {
        this.tournamentId = tournamentId;
        this.tournamentSize = tournamentSize;
        this.timestampStart = timestampStart;
    }
    // Getters and setters
    public Long getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }
    public int getTournamentSize() {
        return tournamentSize;
    }
    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
    public LocalDateTime getTimestampStart() {
        return timestampStart;
    }
    public void setTimestampStart(LocalDateTime timestampStart) {
        this.timestampStart = timestampStart;
    }
}
