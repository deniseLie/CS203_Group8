package csd.backend.Matching.MS.model;

public enum QueueType {
    SPEED_UP("speedUpQueue"),
    NORMAL("normalQueue");

    private final String type;

    QueueType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isValid(String type) {
        for (QueueType queueType : values()) {
            if (queueType.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
