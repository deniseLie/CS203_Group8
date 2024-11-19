package csd.backend.Account.MS.model.player;

import jakarta.persistence.*;

@Entity
public class PlayerChampionStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playerId")
    private Long playerId;  

    private Long championId;
    private double averagePlace;
    private double kdRate;
    private int totalWins;
    private int totalMatchNumber;

    // Mapping playerId to Player entity using @ManyToOne
    @ManyToOne
    @JoinColumn(name = "playerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

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

    public Long getChampionId() {
        return championId;
    }

    public void setChampionId(Long championId) {
        this.championId = championId;
    }

    public double getAveragePlace() {
        return averagePlace;
    }

    public void setAveragePlace(double averagePlace) {
        this.averagePlace = averagePlace;
    }

    public double getKdRate() {
        return kdRate;
    }

    public void setKdRate(double kdRate) {
        this.kdRate = kdRate;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalMatchNumber() {
        return totalMatchNumber;
    }

    public void setTotalMatchNumber(int totalMatchNumber) {
        this.totalMatchNumber = totalMatchNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
