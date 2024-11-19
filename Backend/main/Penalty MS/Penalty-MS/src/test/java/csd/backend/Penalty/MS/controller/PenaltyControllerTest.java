package csd.backend.Penalty.MS.controller;

import csd.backend.Penalty.MS.service.PenaltyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PenaltyControllerTest {

    @Mock
    private PenaltyService penaltyService;

    @InjectMocks
    private PenaltyController penaltyController;

    private static final Long PLAYER_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * Test ban player method - successful
     */
    @Test
    void testBanPlayer_Success() {
        // Arrange
        Map<String, Object> successResponse = Map.of("message", "Player banned successfully.");
        when(penaltyService.banPlayer(PLAYER_ID))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyController.banPlayer(PLAYER_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Player banned successfully.", response.getBody().get("message"));

        // Verify interactions
        verify(penaltyService, times(1)).banPlayer(PLAYER_ID);
    }

    /*
     * Test ban player method - failure
     */
    @Test
    void testBanPlayer_Failure() {
        // Arrange
        Map<String, Object> errorResponse = Map.of("message", "Error banning player.");
        when(penaltyService.banPlayer(PLAYER_ID))
                .thenReturn(new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyController.banPlayer(PLAYER_ID);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error banning player.", response.getBody().get("message"));

        // Verify interactions
        verify(penaltyService, times(1)).banPlayer(PLAYER_ID);
    }

    /*
     * Test check player ban status method - successful
     */
    @Test
    void testCheckPlayerBanStatus_Banned() {
        // Arrange
        Map<String, Object> bannedResponse = Map.of(
                "playerId", PLAYER_ID,
                "queueStatus", "banned",
                "remainingTime", 300L
        );
        when(penaltyService.checkPlayerBanStatus(PLAYER_ID))
                .thenReturn(new ResponseEntity<>(bannedResponse, HttpStatus.OK));

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyController.checkPlayerBanStatus(PLAYER_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(PLAYER_ID, response.getBody().get("playerId"));
        assertEquals("banned", response.getBody().get("queueStatus"));
        assertEquals(300L, response.getBody().get("remainingTime"));

        // Verify interactions
        verify(penaltyService, times(1)).checkPlayerBanStatus(PLAYER_ID);
    }

    /*
     * Test check player ban status method - not found
     */
    @Test
    void testCheckPlayerBanStatus_NotFound() {
        // Arrange
        Map<String, Object> notFoundResponse = Map.of("message", "Player not found");
        when(penaltyService.checkPlayerBanStatus(PLAYER_ID))
                .thenReturn(new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND));

        // Act
        ResponseEntity<Map<String, Object>> response = penaltyController.checkPlayerBanStatus(PLAYER_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Player not found", response.getBody().get("message"));

        // Verify interactions
        verify(penaltyService, times(1)).checkPlayerBanStatus(PLAYER_ID);
    }
}
