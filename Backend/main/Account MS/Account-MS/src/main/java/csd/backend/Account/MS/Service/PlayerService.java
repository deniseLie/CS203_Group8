package csd.backend.Account.MS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.backend.Account.MS.Model.Player;
import csd.backend.Account.MS.Repository.PlayerRepository;
import csd.backend.Account.MS.Service.StatsService; 
import jakarta.transaction.Transactional;

@Service
public class PlayerService {

    @Autowired 
    private PlayerRepository playerRepository;

    @Autowired
    private StatsService statsService; 

    // Insert new user into database
    public void registerUser(Player player) {
        playerRepository.save(player);
    }

    // Handle match completion and recalculate stats
    @Transactional
    public void handleMatchCompletion(Long playerId, int championId, double kdRate, int finalPlacement, int rankPoints, boolean isWin) {
        
        // Update Player stats in overall stats and champion stats
        statsService.updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
        statsService.updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
    }
}
