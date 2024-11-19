package csd.backend.Matching.MS.model;

public enum PlayerStatus {
    AVAILABLE("available"),
    QUEUE("queue"),
    SPEEDUP_QUEUE("speedupQueue"),
    BAN("ban");

    private final String status;

    PlayerStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
