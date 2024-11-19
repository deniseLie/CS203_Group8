package csd.backend.Account.MS.service.champion;

import csd.backend.Account.MS.exception.ChampionNotFoundException;
import csd.backend.Account.MS.model.champion.Champion;
import csd.backend.Account.MS.repository.champion.ChampionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChampionServiceTest {

    @Mock
    private ChampionRepository championRepository;

    @InjectMocks
    private ChampionService championService;

    private Champion sampleChampion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample champion for testing
        sampleChampion = new Champion();
        sampleChampion.setChampionId(1L);
        sampleChampion.setChampionName("Aatrox");
        sampleChampion.setChampionRole("Fighter");
    }

    /*
     * Test get all champions - success
     */
    @Test
    void testGetAllChampions_Success() {
        // Arrange
        List<Champion> mockChampions = Arrays.asList(sampleChampion, new Champion());
        when(championRepository.findAll()).thenReturn(mockChampions);

        // Act
        List<Champion> champions = championService.getAllChampions();

        // Assert
        assertNotNull(champions);
        assertEquals(2, champions.size());
        verify(championRepository, times(1)).findAll();
    }

    /*
     * Test get champion by id - success
     */
    @Test
    void testGetChampionById_Success() {
        // Arrange
        when(championRepository.findById(sampleChampion.getChampionId())).thenReturn(Optional.of(sampleChampion));

        // Act
        Champion champion = championService.getChampionById(sampleChampion.getChampionId());

        // Assert
        assertNotNull(champion);
        assertEquals("Aatrox", champion.getChampionName());
        assertEquals("Fighter", champion.getChampionRole());
        verify(championRepository, times(1)).findById(sampleChampion.getChampionId());
    }

    /*
     * Test get champion by id - not found
     */
    @Test
    void testGetChampionById_NotFound() {
        // Arrange
        when(championRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ChampionNotFoundException.class, () -> championService.getChampionById(1L));
        verify(championRepository, times(1)).findById(1L);
    }

    /*
     * Test add champion - success
     */
    @Test
    void testAddChampion_Success() {
        // Arrange
        when(championRepository.save(sampleChampion)).thenReturn(sampleChampion);

        // Act
        Champion champion = championService.addChampion(sampleChampion);

        // Assert
        assertNotNull(champion);
        assertEquals("Aatrox", champion.getChampionName());
        verify(championRepository, times(1)).save(sampleChampion);
    }

    /*
     * Test update champion - success
     */
    @Test
    void testUpdateChampion_Success() {
        // Arrange
        Champion updatedChampionDetails = new Champion();
        updatedChampionDetails.setChampionName("Ahri");
        updatedChampionDetails.setChampionRole("Mage");

        when(championRepository.findById(sampleChampion.getChampionId())).thenReturn(Optional.of(sampleChampion));
        when(championRepository.save(any(Champion.class))).thenReturn(sampleChampion);

        // Act
        Champion updatedChampion = championService.updateChampion(sampleChampion.getChampionId(), updatedChampionDetails);

        // Assert
        assertNotNull(updatedChampion);
        assertEquals("Ahri", updatedChampion.getChampionName());
        assertEquals("Mage", updatedChampion.getChampionRole());
        verify(championRepository, times(1)).findById(sampleChampion.getChampionId());
        verify(championRepository, times(1)).save(sampleChampion);
    }

    /*
     * Test update champion - not found
     */
    @Test
    void testUpdateChampion_NotFound() {
        // Arrange
        Champion updatedChampionDetails = new Champion();
        updatedChampionDetails.setChampionName("Ahri");
        updatedChampionDetails.setChampionRole("Mage");

        when(championRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ChampionNotFoundException.class, () -> championService.updateChampion(1L, updatedChampionDetails));
        verify(championRepository, times(1)).findById(1L);
        verify(championRepository, times(0)).save(any());
    }

    /*
     * Test delete champion - success
     */
    @Test
    void testDeleteChampion_Success() {
        // Arrange
        when(championRepository.findById(sampleChampion.getChampionId())).thenReturn(Optional.of(sampleChampion));
        doNothing().when(championRepository).delete(sampleChampion);

        // Act
        assertDoesNotThrow(() -> championService.deleteChampion(sampleChampion.getChampionId()));

        // Assert
        verify(championRepository, times(1)).findById(sampleChampion.getChampionId());
        verify(championRepository, times(1)).delete(sampleChampion);
    }

    /*
     * Test delete champion - not found
     */
    @Test
    void testDeleteChampion_NotFound() {
        // Arrange
        when(championRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ChampionNotFoundException.class, () -> championService.deleteChampion(1L));
        verify(championRepository, times(1)).findById(1L);
        verify(championRepository, times(0)).delete(any());
    }
}
