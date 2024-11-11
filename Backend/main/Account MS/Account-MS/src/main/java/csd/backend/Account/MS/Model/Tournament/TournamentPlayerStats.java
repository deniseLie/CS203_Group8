package csd.backend.Account.MS.Model.Tournament;

import java.time.LocalDateTime;

import csd.backend.Account.MS.Model.Player.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TournamentPlayerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentPlayerId;

    @ManyToOne
    @JoinColumn(name = "tournamentId", referencedColumnName = "tournamentId")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "playerId", referencedColumnName = "id")
    private Player player;

    private int rankIdAfterTournament;
    private int pointObtain;
    private Long championPlayedId;
    private int finalPlacement;
    private LocalDateTime timeEndPerPlayer;
    private int killCount;
    private int deathCount;
    private int largestWinStreak;
    private boolean isAFK;

    // Getters and setters
    public Long getTournamentPlayerId() {
        return tournamentPlayerId;
    }
    public void setTournamentPlayerId(Long tournamentPlayerId) {
        this.tournamentPlayerId = tournamentPlayerId;
    }
    public Tournament getTournament() {
        return tournament;
    }
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public int getRankIdAfterTournament() {
        return rankIdAfterTournament;
    }
    public void setRankIdAfterTournament(int rankIdAfterTournament) {
        this.rankIdAfterTournament = rankIdAfterTournament;
    }
    public int getPointObtain() {
        return pointObtain;
    }
    public void setPointObtain(int pointObtain) {
        this.pointObtain = pointObtain;
    }
    public Long getChampionPlayedId() {
        return championPlayedId;
    }
    public void setChampionPlayedId(Long championPlayedId) {
        this.championPlayedId = championPlayedId;
    }
    public int getFinalPlacement() {
        return finalPlacement;
    }
    public void setFinalPlacement(int finalPlacement) {
        this.finalPlacement = finalPlacement;
    }
    public LocalDateTime getTimeEndPerPlayer() {
        return timeEndPerPlayer;
    }
    public void setTimeEndPerPlayer(LocalDateTime timeEndPerPlayer) {
        this.timeEndPerPlayer = timeEndPerPlayer;
    }
    public int getKillCount() {
        return killCount;
    }
    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }
    public int getDeathCount() {
        return deathCount;
    }
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }
    public int getLargestWinStreak() {
        return largestWinStreak;
    }
    public void setLargestWinStreak(int largestWinStreak) {
        this.largestWinStreak = largestWinStreak;
    }
    public boolean getIsAFK() {
        return isAFK;
    }
    public void setIsAFK(boolean isAFK) {
        this.isAFK = isAFK;
    }
}
