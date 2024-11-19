package csd.backend.Account.MS.model.Tournament;

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
