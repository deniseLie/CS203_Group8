package csd.backend.Account.MS.service.player;

import csd.backend.Account.MS.DTO.PlayerProfileUpdateRequest;
import csd.backend.Account.MS.exception.*;
import csd.backend.Account.MS.model.champion.Champion;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.repository.player.*;
import csd.backend.Account.MS.service.SqsService;
import csd.backend.Account.MS.service.champion.ChampionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerOverallStatsRepository playerOverallStatsRepository;

    @Mock
    private PlayerChampionStatsRepository playerChampionStatsRepository;

    @Mock
    private SqsService sqsService;

    @Mock
    private ChampionService championService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PlayerService playerService;

    private Player samplePlayer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePlayer = new Player();
        samplePlayer.setId(1L);
        samplePlayer.setUsername("testUser");
        samplePlayer.setProfilePicture("profilePic");
    }

    /*
     * Test : get player id - success
     */
    @Test
    void testGetPlayerById_Success() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.of(samplePlayer));

        // Act
        Player result = playerService.getPlayerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(playerRepository, times(1)).findById(1L);
    }

    /*
     * Test : get player id - not found
     */
    @Test
    void testGetPlayerById_NotFound() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerById(1L));
        verify(playerRepository, times(1)).findById(1L);
    }

    /*
     * Test register user - success
     */
    @Test
    void testRegisterUser_Success() {
        // Arrange
        when(playerRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(samplePlayer);

        // Act
        assertDoesNotThrow(() -> playerService.registerUser(samplePlayer));

        // Assert
        verify(playerRepository, times(1)).findByUsername("testUser");
        verify(playerRepository, times(1)).save(samplePlayer);
    }

    /*
     * Test register user - already exist 
     */
    @Test
    void testRegisterUser_AlreadyExists() {
        // Arrange
        when(playerRepository.findByUsername("testUser")).thenReturn(Optional.of(samplePlayer));

        // Act & Assert
        assertThrows(PlayerRegisterExisted.class, () -> playerService.registerUser(samplePlayer));
        verify(playerRepository, times(1)).findByUsername("testUser");
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    /*
     * Test get fomatted top champions - success
     */
    @Test
    void testGetFormattedTopChampions_Success() {
        // Arrange
        List<PlayerChampionStats> championStats = List.of(
                new PlayerChampionStats(1L, 1L, 5, 2.0, 10, 50),
                new PlayerChampionStats(1L, 2L, 3, 3.5, 8, 30)
        );
        Champion champion1 = new Champion();
        champion1.setChampionId(1L);
        champion1.setChampionName("Aatrox");
        champion1.setChampionRole("Fighter");

        Champion champion2 = new Champion();
        champion2.setChampionId(2L);
        champion2.setChampionName("Ahri");
        champion2.setChampionRole("Mage");

        when(playerRepository.existsById(1L)).thenReturn(true);
        when(playerChampionStatsRepository.findByPlayerId(1L)).thenReturn(championStats);
        when(championService.getChampionById(1L)).thenReturn(champion1);
        when(championService.getChampionById(2L)).thenReturn(champion2);

        // Act
        List<Map<String, Object>> result = playerService.getFormattedTopChampions(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Aatrox", result.get(0).get("championName"));
        assertEquals("Ahri", result.get(1).get("championName"));
        verify(playerRepository, times(1)).existsById(1L);
        verify(playerChampionStatsRepository, times(1)).findByPlayerId(1L);
    }

    /*
     * Test get player stats - success
     */
    @Test
    void testGetPlayerStats_Success() {
        // Arrange
        PlayerOverallStats stats = new PlayerOverallStats();
        stats.setPlayerId(1L);
        stats.setTotalNumberOfMatches(100);
        stats.setTotalFirstPlaceMatches(25);
        stats.setOverallAveragePlace(1.5);

        when(playerRepository.existsById(1L)).thenReturn(true);
        when(playerOverallStatsRepository.findByPlayerId(1L)).thenReturn(stats);

        // Act
        Map<String, Object> result = playerService.getPlayerStats(1L);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.get("totalMatches"));
        assertEquals(25.0, result.get("firstPlacePercentage"));
        verify(playerRepository, times(1)).existsById(1L);
        verify(playerOverallStatsRepository, times(1)).findByPlayerId(1L);
    }

    /*
     * Test delet player by player id - success
     */
    @Test
    void testDeletePlayerByPlayerId_Success() {
        // Arrange
        when(playerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(playerRepository).deleteByPlayerId(1L);

        // Act
        assertDoesNotThrow(() -> playerService.deletePlayerByPlayerId(1L));

        // Assert
        verify(playerRepository, times(1)).existsById(1L);
        verify(playerRepository, times(1)).deleteByPlayerId(1L);
    }

    /*
     * Test delete player by player id - not found
     */
    @Test
    void testDeletePlayerByPlayerId_NotFound() {
        // Arrange
        when(playerRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.deletePlayerByPlayerId(1L));
        verify(playerRepository, times(1)).existsById(1L);
        verify(playerRepository, times(0)).deleteByPlayerId(anyLong());
    }

    /*
     * Test update player profile - success
     */
    @Test
    void testUpdatePlayerProfile_Success() {
        // Arrange
        PlayerProfileUpdateRequest updateRequest = new PlayerProfileUpdateRequest(
            1L, "newUsername", null, null, null, "newProfilePic"
        );

        when(playerRepository.findById(1L)).thenReturn(Optional.of(samplePlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(samplePlayer);

        // Act
        Player updatedPlayer = playerService.updatePlayerProfile(1L, updateRequest);

        // Assert
        assertNotNull(updatedPlayer);
        assertEquals("newUsername", updatedPlayer.getUsername());
        assertEquals("newProfilePic", updatedPlayer.getProfilePicture());
        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(samplePlayer);
    }

}
