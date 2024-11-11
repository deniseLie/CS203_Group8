package csd.backend.Account.MS.Model;

import jakarta.persistence.*;

@Entity
public class PlayerChampionStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;  

    private int championId;
    private double averagePlace;
    private double kdRate;
    private int totalWins;
    private int totalMatchNumber;

    // Mapping userId to Player entity using @ManyToOne
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
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
