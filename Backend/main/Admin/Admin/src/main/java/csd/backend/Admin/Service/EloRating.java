package csd.backend.Admin.Service;

public class EloRating {

    // Method to calculate Elo rating change
    public static double calculateElo(double playerElo, double opponentElo, double outcome, double kFactor) {
        // Expected outcome
        double expectedOutcome = 1 / (1 + Math.pow(10, (opponentElo - playerElo) / 400));

        // Calculate new Elo rating
        double newElo = playerElo + kFactor * (outcome - expectedOutcome);

        // Apply thresholds
        if (newElo - playerElo > 10) {  // Min +10 points
            newElo = playerElo + 10;
        } else if (newElo - playerElo < -50) {  // Max -50 points
            newElo = playerElo - 50;
        }

        return newElo;
    }

    // Method to apply streaks to the K-factor
    public static double applyStreaks(double playerElo, int winStreak, int lossStreak, double baseK) {
        double kFactor;

        // Apply multipliers based on streaks
        if (winStreak >= 3) {
            kFactor = baseK * 1.2;  // 20% bonus for win streak of 3+
        } else if (lossStreak >= 3) {
            kFactor = baseK * 0.8;  // 20% penalty for loss streak of 3+
        } else {
            kFactor = baseK;
        }

        return kFactor;
    }

    // Method to update Elo considering streaks
    public static double updateElo(double playerElo, double opponentElo, double outcome, int winStreak, int lossStreak) {
        // Apply streak multiplier
        double kFactor = applyStreaks(playerElo, winStreak, lossStreak, 32);  // Default K-factor is 32

        // Calculate the updated Elo rating
        return calculateElo(playerElo, opponentElo, outcome, kFactor);
    }
}
