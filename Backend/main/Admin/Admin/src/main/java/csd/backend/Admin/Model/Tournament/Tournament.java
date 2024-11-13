package csd.backend.Admin.Model.Tournament;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentId;

    private int tournamentSize;
    private boolean status;     // 0 - ongoing, 1 - completed
    private LocalDateTime timestampStart;
    private LocalDateTime timeStampEnd;

    // Mark tournament as done by setting the end timestamp
    public void markAsDone() {
        if (status == true) {
            timeStampEnd = LocalDateTime.now();  // Set current time as end timestamp
        }
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
    public boolean getStatus(){
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestampStart() {
        return timestampStart;
    }
    public void setTimestampStart(LocalDateTime timestampStart) {
        this.timestampStart = timestampStart;
    }
    public LocalDateTime getTimestampEnd() {
        return timeStampEnd;
    }
    public void setTimestampEnd(LocalDateTime timeStampEnd) {
        this.timeStampEnd = timeStampEnd;
    }
}