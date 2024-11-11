
package csd.backend.Account.MS;

import csd.backend.Account.MS.Model.Player;
import csd.backend.Account.MS.Model.PlayerChampionStats;
import csd.backend.Account.MS.Model.PlayerOverallStats;
import csd.backend.Account.MS.Service.PlayerService;
import csd.backend.Account.MS.Service.StatsService;
import csd.backend.Account.MS.Repository.PlayerRepository;
import csd.backend.Account.MS.Repository.PlayerChampionStatsRepository;
import csd.backend.Account.MS.Repository.PlayerOverallStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private PlayerService playerService;

    private Player testPlayer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Create a test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setUsername("TestPlayer");
    }

    @Test
    public void testRegisterUser() {
        // When player is registered
        playerService.registerUser(testPlayer);

        // Verify that save method was called once
        verify(playerRepository, times(1)).save(testPlayer);
    }

    @Test
    public void testHandleMatchCompletion() {
        // Assume match details
        long playerId = 1L;
        int championId = 1;
        double kdRate = 2.5;
        int finalPlacement = 1;
        int rankPoints = 100;
        boolean isWin = true;

        // Call handleMatchCompletion
        playerService.handleMatchCompletion(playerId, championId, kdRate, finalPlacement, rankPoints, isWin);

        // Verify that the stats updates are called with correct arguments
        verify(statsService, times(1)).updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
        verify(statsService, times(1)).updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
    }
}
