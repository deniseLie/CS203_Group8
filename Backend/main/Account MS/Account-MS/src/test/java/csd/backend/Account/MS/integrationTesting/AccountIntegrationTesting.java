package csd.backend.Account.MS.integrationTesting;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.controller.AccountController;
import csd.backend.Account.MS.model.player.Player;
import csd.backend.Account.MS.service.player.PlayerService;
import csd.backend.Account.MS.service.player.PlayerStatsService;
import csd.backend.Account.MS.service.tournament.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AccountIntegrationTesting {

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerStatsService playerStatsService;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private AccountController accountController;

    private Player samplePlayer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample player
        samplePlayer = new Player();
        samplePlayer.setId(1L);
        samplePlayer.setUsername("testPlayer");
        samplePlayer.setProfilePicture("testPic.jpg");
    }

    /*
     * Test update player profile - success
     */
    @Test
    void testUpdatePlayerProfile_Success() {
        // Arrange
        PlayerProfileUpdateRequest updateRequest = new PlayerProfileUpdateRequest();
        updateRequest.setUsername("updatedPlayer");
        updateRequest.setProfilePicture("updatedPic.jpg");

        when(playerService.updatePlayerProfile(anyLong(), any(PlayerProfileUpdateRequest.class))).thenReturn(samplePlayer);

        // Act
        ResponseEntity<Map<String, Object>> response = accountController.updatePlayerProfile(1L, updateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Profile updated successfully.", response.getBody().get("message"));
        verify(playerService, times(1)).updatePlayerProfile(anyLong(), any(PlayerProfileUpdateRequest.class));
    }

    /*
     * Test get player profile picture - success
     */
    @Test
    void testGetPlayerProfilePicture_Success() {
        // Arrange
        when(playerService.getPlayerById(anyLong())).thenReturn(samplePlayer);

        // Act
        ResponseEntity<Map<String, Object>> response = accountController.getPlayerProfilePicture(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("/path/to/images/testPic.jpg", response.getBody().get("profilePictureUrl"));
        verify(playerService, times(1)).getPlayerById(anyLong());
    }

    /*
     * Test get player profile picture - player not found
     */
    @Test
    void testGetPlayerProfilePicture_PlayerNotFound() {
        // Arrange
        when(playerService.getPlayerById(anyLong())).thenThrow(new RuntimeException("Player not found"));

        // Act
        ResponseEntity<Map<String, Object>> response = accountController.getPlayerProfilePicture(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        verify(playerService, times(1)).getPlayerById(anyLong());
    }

    /*
     * Test get player profile - success
     */
    @Test
    void testGetPlayerProfile_Success() {
        // Arrange
        List<Map<String, Object>> topChampions = List.of(
            Map.of(
                "championId", 1L,
                "championName", "Aatrox",
                "averagePlace", 2.5,
                "kdRate", 3.0,
                "totalWins", 10,
                "totalMatchNumber", 50
            ),
            Map.of(
                "championId", 2L,
                "championName", "Ahri",
                "averagePlace", 3.0,
                "kdRate", 2.5,
                "totalWins", 8,
                "totalMatchNumber", 30
            )
        );

        Map<String, Object> playerStats = Map.of(
            "totalMatches", 100,
            "averagePlace", 2.5,
            "firstPlacePercentage", 10.0
        );

        when(playerService.getFormattedTopChampions(1L)).thenReturn(topChampions);
        when(playerService.getPlayerStats(anyLong())).thenReturn(playerStats);

        // Act
        ResponseEntity<Map<String, Object>> response = accountController.getPlayerProfile(1L);

        // Assert
        assertNotNull(response.getBody());
    }

    /*
     * Test get player match history - success
     */
    @Test
    void testGetPlayerMatchHistory_Success() {
        // Arrange
        List<Map<String, Object>> matchHistory = List.of(
                Map.of("tournamentId", 1, "playerName", "testPlayer", "champion", "Aatrox")
        );

        // Mock the service call to return the match history
        when(tournamentService.getPlayerMatchHistory(anyLong())).thenReturn(matchHistory);

        // Act
        ResponseEntity<Map<String, Object>> response = accountController.getPlayerMatchHistory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().containsKey("data"));
        assertEquals(matchHistory, response.getBody().get("data"));
        verify(tournamentService, times(1)).getPlayerMatchHistory(anyLong());
    }


    /*
    * Test get player rank - success
    */
    @Test
    void testGetPlayerRank_Success() {
        // Arrange
        String expectedRank = "Gold";
        when(playerStatsService.getPlayerRankName(1L)).thenReturn(expectedRank);

        // Act
        ResponseEntity<Map<String, Object>> response = accountController.getPlayerRank(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Check for status code OK (200)
        assertNotNull(response.getBody());  // Ensure the response body is not null

        // Extract rankName from the response body
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        assertEquals(expectedRank, data.get("rankName"));

        // Verify that the service method was called once
        verify(playerStatsService, times(1)).getPlayerRankName(1L);
    }


}
