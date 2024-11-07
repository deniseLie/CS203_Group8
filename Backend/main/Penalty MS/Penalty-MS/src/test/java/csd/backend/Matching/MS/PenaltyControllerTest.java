import csd.backend.Penalty.MS.PenaltyController;
import csd.backend.Penalty.MS.PenaltyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PenaltyControllerTest {

    @Mock
    private PenaltyService penaltyService;

    @InjectMocks
    private PenaltyController penaltyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void banPlayer_ShouldReturnSuccessMessage() {
        // Arrange
        String playerName = "Player1";
        int duration = 5; // Duration in minutes

        doNothing().when(penaltyService).banPlayer(playerName);

        // Act
        String response = penaltyController.banPlayer(playerName, duration);

        // Assert
        assertEquals("Player banned successfully for " + duration + " minutes.", response);
        verify(penaltyService, times(1)).banPlayer(playerName);
    }

    @Test
    void checkPlayerStatus_ShouldReturnPlayerStatus() {
        // Arrange
        String playerName = "Player1";
        Map<String, Object> expectedStatus = new HashMap<>();
        expectedStatus.put("playerName", playerName);
        expectedStatus.put("queueStatus", "banned");
        expectedStatus.put("remainingTime", 1000L);

        when(penaltyService.checkPlayerStatus(playerName)).thenReturn(expectedStatus);

        // Act
        Map<String, Object> response = penaltyController.checkPlayerStatus(playerName);

        // Assert
        assertEquals(expectedStatus, response);
        verify(penaltyService, times(1)).checkPlayerStatus(playerName);
    }
}
