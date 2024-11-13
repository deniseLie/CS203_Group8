package csd.backend.Admin.Model.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class TournamentDTO {
    private Long tournamentId;
    private int tournamentSize;
    private String status; // Ongoing or Completed
    private LocalDateTime timestampStart;
    private LocalDateTime timeStampEnd;
    private List<PlayerDTO> players;
    private int totalRounds; // Calculated value
    private int currentRound; // Calculated value

    // GETTERS AND SETTERS
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestampStart() {
        return timestampStart;
    }

    // Removed the argument in this method, it is now just a getter
    public void setTimestampStart(LocalDateTime timestampStart) {
        this.timestampStart = timestampStart;
    }

    public LocalDateTime getTimeStampEnd() {
        return timeStampEnd;
    }

    // Removed the argument in this method, it is now just a getter
    public void setTimeStampEnd(LocalDateTime timeStampEnd) {
        this.timeStampEnd = timeStampEnd;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
