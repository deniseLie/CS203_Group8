package csd.backend.Ranking.MS;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Tournament {
    @Id
    private Long tournamentId;

    private LocalDateTime timestampStart;
    private LocalDateTime timestampEnd;

    public Tournament(){}

    public Tournament(Long tournamentId, LocalDateTime timestampStart, LocalDateTime timestampEnd) {
        this.tournamentId = tournamentId;
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
    }

     public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public LocalDateTime getTimestampStart() {
        return timestampStart;
    }

    public void setTimestampStart(LocalDateTime timestampStart) {
        this.timestampStart = timestampStart;
    }

    public LocalDateTime getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(LocalDateTime timestampEnd) {
        this.timestampEnd = timestampEnd;
    }
    
}
