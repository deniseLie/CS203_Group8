// package csd.backend.Matching.MS.integration;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// import csd.backend.Matching.MS.service.matchmaking.MatchmakingService;
// import csd.backend.Matching.MS.service.player.PlayerService;
// import csd.backend.Matching.MS.model.PlayerStatus;

// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class MatchmakingIntegrationTest {

//     @LocalServerPort
//     private int port;

//     private final String baseUrl = "http://localhost:";

//     @Autowired
//     private MatchmakingService matchmakingService;

//     @Autowired
//     private PlayerService playerService;

//     private static final Long PLAYER_ID = 1L;
//     private static final Long CHAMPION_ID = 101L;
//     private static final Long RANK_ID = 5L;

//     @Test
//     void testJoinNormalTournament() {
//         // Arrange
//         playerService.addPlayerToPool(PLAYER_ID, PlayerStatus.AVAILABLE.getStatus(), RANK_ID.intValue());

//         // Act
//         ResponseEntity<Map<String, Object>> response = matchmakingService.joinMatchmaking(PLAYER_ID, CHAMPION_ID, false);

//         // Assert
//         assertEquals(200, response.getStatusCode().value(), "Status code mismatch");
//         assertNotNull(response.getBody(), "Response body is null");
//         assertTrue(response.getBody().containsKey("message"), "Response body does not contain 'message' key");
//     }

//     @Test
//     void testJoinSpeedUpQueue() {
//         // Arrange
//         playerService.addPlayerToPool(PLAYER_ID, PlayerStatus.AVAILABLE.getStatus(), RANK_ID.intValue());

//         // Act
//         ResponseEntity<Map<String, Object>> response = matchmakingService.joinMatchmaking(PLAYER_ID, CHAMPION_ID, true);

//         // Assert
//         assertEquals(200, response.getStatusCode().value(), "Status code mismatch");
//         assertNotNull(response.getBody(), "Response body is null");
//         assertTrue(response.getBody().containsKey("message"), "Response body does not contain 'message' key");
//     }

//     @Test
//     void testUnqueueFromNormalTournament() {
//         // Arrange
//         playerService.addPlayerToPool(PLAYER_ID, PlayerStatus.QUEUE.getStatus(), RANK_ID.intValue());

//         // Act
//         ResponseEntity<Map<String, Object>> response = matchmakingService.unqueuePlayer(PLAYER_ID);

//         // Assert
//         assertEquals(200, response.getStatusCode().value(), "Status code mismatch");
//         assertNotNull(response.getBody(), "Response body is null");
//         assertEquals("Player successfully removed from queue", response.getBody().get("message"), "Unqueue message mismatch");
//     }

//     @Test
//     void testUnqueueFromSpeedUpQueue() {
//         // Arrange
//         playerService.addPlayerToPool(PLAYER_ID, PlayerStatus.SPEEDUP_QUEUE.getStatus(), RANK_ID.intValue());

//         // Act
//         ResponseEntity<Map<String, Object>> response = matchmakingService.unqueueFromSpeedUpQueue(PLAYER_ID);

//         // Assert
//         assertEquals(200, response.getStatusCode().value(), "Status code mismatch");
//         assertNotNull(response.getBody(), "Response body is null");
//         assertEquals("Player successfully removed from queue", response.getBody().get("message"), "Unqueue message mismatch");
//     }
// }
