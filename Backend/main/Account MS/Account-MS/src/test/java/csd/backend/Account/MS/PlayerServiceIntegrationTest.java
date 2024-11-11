// package csd.backend.Account.MS;

// import csd.backend.Account.MS.Model.Player;
// import csd.backend.Account.MS.Model.PlayerChampionStats;
// import csd.backend.Account.MS.Model.PlayerOverallStats;
// import csd.backend.Account.MS.Repository.PlayerChampionStatsRepository;
// import csd.backend.Account.MS.Repository.PlayerOverallStatsRepository;
// import csd.backend.Account.MS.Repository.PlayerRepository;
// import csd.backend.Account.MS.Service.PlayerService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
// class PlayerServiceIntegrationTest {

//     @Autowired
//     private PlayerService playerService;

//     @Autowired
//     private PlayerRepository playerRepository;

//     @Autowired
//     private PlayerChampionStatsRepository playerChampionStatsRepository;

//     @Autowired
//     private PlayerOverallStatsRepository playerOverallStatsRepository;

//     @Test
//     public void testHandleMatchCompletionWithDatabase() {
//         // Setup player data
//         Player testPlayer = new Player();
//         testPlayer.setUsername("TestPlayer");
//         playerRepository.save(testPlayer);

//         // Handle match completion and update stats
//         playerService.handleMatchCompletion(testPlayer.getId(), 1, 2.0, 3, 100, true);

//         // Retrieve the updated player stats
//         PlayerOverallStats updatedStats = playerOverallStatsRepository.findByPlayerId(testPlayer.getId());
//         PlayerChampionStats updatedChampionStats = playerChampionStatsRepository.findByPlayerIdAndChampionId(testPlayer.getId(), 1);

//         // Assertions to validate the stats are updated
//         assertNotNull(updatedStats);
//         assertEquals(1, updatedStats.getTotalNumberOfMatches());
//         assertNotNull(updatedChampionStats);
//     }
// }
