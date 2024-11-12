
// package csd.backend.Account.MS;

// import csd.backend.Account.MS.Exception.*;
// import csd.backend.Account.MS.Model.Player.*;
// import csd.backend.Account.MS.Service.Player.*;
// import csd.backend.Account.MS.Repository.Player.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.*;

// public class PlayerServiceTest {

//     @Mock
//     private PlayerRepository playerRepository;

//     @Mock
//     private PlayerOverallStatsRepository playerOverallStatsRepository;

//     @Mock
//     private PlayerChampionStatsRepository playerChampionStatsRepository;

//     @Mock
//     private PlayerStatsService playerStatsService;

//     @InjectMocks
//     private PlayerService playerService;

//     private Player testPlayer;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//         // Create a test player
//         testPlayer = new Player();
//         testPlayer.setId(1L);
//         testPlayer.setUsername("TestPlayer");
//     }

//     @Test
//     public void testRegisterUser() {
//         // When player is registered
//         playerService.registerUser(testPlayer);

//         // Verify that save method was called once
//         verify(playerRepository, times(1)).save(testPlayer);
//     }

//     @Test
//     public void testRegisterUser_ThrowsPlayerRegisterExisted() {
//         // Mock the behavior of the repository to simulate an existing player
//         when(playerRepository.findByUsername(testPlayer.getUsername())).thenReturn(java.util.Optional.of(testPlayer));

//         // Verify that PlayerRegisterExisted exception is thrown
//         PlayerRegisterExisted exception = assertThrows(PlayerRegisterExisted.class, () -> {
//             playerService.registerUser(testPlayer);
//         });

//         // Verify the exception message
//         assertEquals("Player with username TestPlayer already exists.", exception.getMessage
//         ());
//     }

//     @Test
//     public void testHandleMatchCompletion() {
//         // Assume match details
//         long playerId = 1L;
//         long championId = 1L;
//         double kdRate = 2.5;
//         int finalPlacement = 1;
//         int rankPoints = 100;
//         boolean isWin = true;

//         // Mock the repository to simulate an existing player
//         when(playerRepository.existsById(playerId)).thenReturn(true);

//         // Call handleMatchCompletion
//         playerService.handleMatchCompletion(playerId, championId, kdRate, finalPlacement, rankPoints, isWin);

//         // Verify that the stats updates are called with correct arguments
//         verify(playerStatsService, times(1)).updateOverallStats(playerId, rankPoints, kdRate, finalPlacement, isWin);
//         verify(playerStatsService, times(1)).updateChampionStats(playerId, championId, kdRate, finalPlacement, isWin);
//     }

//     @Test
//     public void testHandleMatchCompletion_PlayerNotFoundException() {
//         long playerId = 1L;
//         long championId = 1;
//         double kdRate = 2.5;
//         int finalPlacement = 1;
//         int rankPoints = 100;
//         boolean isWin = true;

//         // Mock the repository to simulate a non-existing player
//         when(playerRepository.existsById(playerId)).thenReturn(false);

//         // Verify that PlayerNotFoundException is thrown
//         PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> {
//             playerService.handleMatchCompletion(playerId, championId, kdRate, finalPlacement, rankPoints, isWin);
//         });

//         // Verify the exception message
//         assertEquals("Player not found with player Id: 1", exception.getMessage());
//     }

//     @Test
//     public void testGetTop3PlayedChampions() {
//         long playerId = 1L;

//         // Mock that the player exists
//         when(playerRepository.existsById(playerId)).thenReturn(true);

//         // Create mock champion stats data
//         PlayerChampionStats champion1 = new PlayerChampionStats();
//         champion1.setChampionId(1L);
//         champion1.setTotalMatchNumber(10);
        
//         PlayerChampionStats champion2 = new PlayerChampionStats();
//         champion2.setChampionId(2L);
//         champion2.setTotalMatchNumber(15);
        
//         PlayerChampionStats champion3 = new PlayerChampionStats();
//         champion3.setChampionId(3L);
//         champion3.setTotalMatchNumber(20);

//         PlayerChampionStats champion4 = new PlayerChampionStats();
//         champion4.setChampionId(4L);
//         champion4.setTotalMatchNumber(5);

//         // Mock findByPlayerId to return a list of PlayerChampionStats
//         List<PlayerChampionStats> mockChampionStats = Arrays.asList(champion1, champion2, champion3, champion4);
//         when(playerChampionStatsRepository.findByPlayerId(playerId)).thenReturn(mockChampionStats);

//         // Call getTop3PlayedChampions
//         List<PlayerChampionStats> topChampions = playerService.getTop3PlayedChampions(playerId);

//         // Verify that the repository method was called
//         verify(playerChampionStatsRepository, times(1)).findByPlayerId(playerId);

//         // Assert that topChampions contains only the top 3 champions by total matches
//         assertEquals(3, topChampions.size());
//         assertEquals(20, topChampions.get(0).getTotalMatchNumber()); // champion 3 should be first
//         assertEquals(15, topChampions.get(1).getTotalMatchNumber()); // champion 2 should be second
//         assertEquals(10, topChampions.get(2).getTotalMatchNumber()); // champion 1 should be third
//     }


//     @Test
//     public void testGetTop3PlayedChampions_PlayerNotFoundException() {
//         long playerId = 1L;

//         // Mock the repository to simulate a non-existing player
//         when(playerRepository.existsById(playerId)).thenReturn(false);

//         // Verify that PlayerNotFoundException is thrown
//         PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> {
//             playerService.getTop3PlayedChampions(playerId);
//         });

//         // Verify the exception message
//         assertEquals("Player not found with player Id: 1", exception.getMessage());
//     }

//     @Test 
//     public void testGetPlayerStats() {
//         long playerId = 1L;

//         // Assume that the player exists and has stats
//         when(playerRepository.existsById(playerId)).thenReturn(true);

//         // Mock a valid PlayerOverallStats response
//         PlayerOverallStats mockPlayerStats = new PlayerOverallStats();
//         mockPlayerStats.setTotalNumberOfMatches(10);
//         mockPlayerStats.setOverallAveragePlace(2.5);
//         mockPlayerStats.setTotalFirstPlaceMatches(2);

//         // Mock findByPlayerId to return the mockStats
//         when(playerOverallStatsRepository.findByPlayerId(playerId)).thenReturn(mockPlayerStats);

//         // Call getPlayerStats
//         Map<String, Object> stats = playerService.getPlayerStats(playerId);

//         // Verify that the stats map is not null and contains the correct keys
//         assertNotNull(stats);
//         assertTrue(stats.containsKey("totalMatches"));
//         assertTrue(stats.containsKey("averagePlace"));
//         assertTrue(stats.containsKey("firstPlacePercentage"));

//         // Verify that the values are correct
//         assertEquals(10, stats.get("totalMatches"));
//         assertEquals(2.5, stats.get("averagePlace"));
//         assertEquals(20.0, stats.get("firstPlacePercentage")); // 2/10 * 100 = 20%
//     }

//     @Test
//     public void testGetPlayerStats_PlayerNotFoundException() {
//         long playerId = 1L;

//         // Mock the repository to simulate a non-existing player
//         when(playerRepository.existsById(playerId)).thenReturn(false);

//         // Verify that PlayerNotFoundException is thrown
//         PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> {
//             playerService.getPlayerStats(playerId);
//         });

//         // Verify the exception message
//         assertEquals("Player not found with player Id: 1", exception.getMessage());
//     }
// }
