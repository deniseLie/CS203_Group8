// package csd.backend.Matching.MS.Player;

// import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.PrimaryKeyJoinColumn;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "Players")
// public class Player {

//     @Id
//     @GeneratedValue(strategy=GenerationType.AUTO)
//     private int userId;
//     private String username;
//     private String password;

//     @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
//     @PrimaryKeyJoinColumn
//     private PlayerOverallStatistic playerStatistic;

//     protected Player(){}

//     public Player (int userId, String username, String password, PlayerOverallStatistic playerStatistic){
//         this.userId = userId;
//         this.username = username;
//         this.password = password;
//         this.playerStatistic = playerStatistic;
//     }

//     public int getUserId() {
//         return userId;
//     }

//     public void setUserId(int userId) {
//         this.userId = userId;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     public PlayerOverallStatistic getPlayerStatistic() {
//         return playerStatistic;
//     }

//     public void setPlayerStatistic(PlayerOverallStatistic playerStatistic) {
//         this.playerStatistic = playerStatistic;
//     }

    
// }
