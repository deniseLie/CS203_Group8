package csd.backend.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "matchresult")
public class MatchResult {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private int matchresultid;

    @Id
    @Column(name = "tournament_id")
    private int tournamentId;

    private int player1;
    private int player2;
    private int player3;
    private int player4;
    private int player5;
    private int player6;
    private int player7;
    private int player8;
    private int winner;
    private int loser;

    // Getters and setters
    // public int getMatchresultId() {
    //     return matchresult_id;
    // }

    // public void setMatchresultId(int matchresult_id) {
    //     this.matchresult_id = matchresult_id;
    // }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }


    public int getPlayer1() {
        return player1;
    }
    public void setPlayer1(int player1) {
        this.player1 = player1;
    }
    public int getPlayer2() {
        return player2;
    }
    public void setPlayer2(int player2) {
        this.player2 = player2;
    }
    public int getPlayer3() {
        return player3;
    }
    public void setPlayer3(int player3) {
        this.player3 = player3;
    }
    public int getPlayer4() {
        return player4;
    }
    public void setPlayer4(int player4) {
        this.player4 = player4;
    }
    public int getPlayer5() {
        return player5;
    }
    public void setPlayer5(int player5) {
        this.player5 = player5;
    }
    public int getPlayer6() {
        return player6;
    }
    public void setPlayer6(int player6) {
        this.player6 = player6;
    }
    public int getPlayer7() {
        return player7;
    }
    public void setPlayer7(int player7) {
        this.player7 = player7;
    }
    public int getPlayer8() {
        return player8;
    }
    public void setPlayer8(int player8) {
        this.player8 = player8;
    }
    public int getLoser() {
        return loser;
    }
    public void setLoser(int loser) {
        this.loser = loser;
    }
    public int getWinner() {
        return winner;
    }
    public void setWinner(int winner) {
        this.winner = winner;
    }
}

