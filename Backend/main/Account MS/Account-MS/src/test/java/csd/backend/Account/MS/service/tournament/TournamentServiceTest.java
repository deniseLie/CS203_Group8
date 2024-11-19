package csd.backend.Account.MS.service.tournament;

import csd.backend.Account.MS.exception.*;
import csd.backend.Account.MS.model.champion.*;
import csd.backend.Account.MS.model.player.*;
import csd.backend.Account.MS.model.tournament.*;
import csd.backend.Account.MS.repository.player.*;
import csd.backend.Account.MS.repository.tournament.*;
import csd.backend.Account.MS.service.champion.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentPlayerStatsRepository tournamentPlayerStatsRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ChampionService championService;

    @InjectMocks
    private TournamentService tournamentService;

    private Tournament sampleTournament;
    private Player samplePlayer;
    private TournamentPlayerStats sampleStats;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTournament = new Tournament();
        sampleTournament.setTournamentId(1L);
        sampleTournament.setTournamentSize(8);
        sampleTournament.setTimestampStart(LocalDateTime.now());

        samplePlayer = new Player();
        samplePlayer.setId(1L);
        samplePlayer.setUsername("TestPlayer");

        sampleStats = new TournamentPlayerStats();
        sampleStats.setTournament(sampleTournament);
        sampleStats.setPlayer(samplePlayer);
        sampleStats.setChampionPlayedId(1L);
        sampleStats.setKillCount(5);
        sampleStats.setDeathCount(3);
        sampleStats.setFinalPlacement(2);
        sampleStats.setPointObtain(100);
        sampleStats.setTimeEndPerPlayer(LocalDateTime.now());
        sampleStats.setIsAFK(false);
    }

    @Test
    void testGetAllTournaments_Success() {
        // Arrange
        when(tournamentRepository.findAll()).thenReturn(List.of(sampleTournament));

        // Act
        List<Tournament> result = tournamentService.getAllTournaments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void testGetTournamentById_Success() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(sampleTournament));

        // Act
        Tournament result = tournamentService.getTournamentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTournamentId());
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTournamentById_NotFound() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tournamentService.getTournamentById(1L));
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTournamentPlayerStats_Success() {
        // Arrange
        when(tournamentPlayerStatsRepository.findByTournamentTournamentId(1L))
                .thenReturn(List.of(sampleStats));

        // Act
        List<TournamentPlayerStats> result = tournamentService.getTournamentPlayerStats(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tournamentPlayerStatsRepository, times(1)).findByTournamentTournamentId(1L);
    }

    @Test
    void testGetTournamentPlayerStatsForPlayer_Success() {
        // Arrange
        when(tournamentPlayerStatsRepository.findByPlayerId(1L)).thenReturn(List.of(sampleStats));

        // Act
        List<TournamentPlayerStats> result = tournamentService.getTournamentPlayerStatsForPlayer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tournamentPlayerStatsRepository, times(1)).findByPlayerId(1L);
    }

    @Test
    void testCreateAndSaveTournament_Success() {
        // Arrange
        Map<String, String> tournamentData = Map.of(
                "tournamentSize", "8",
                "timestampStart", LocalDateTime.now().toString(),
                "playerId", "1",
                "championId", "1",
                "killCount", "5",
                "deathCount", "3",
                "finalPlacement", "2",
                "rankPoints", "100",
                "isAFK", "false",
                "endTime", LocalDateTime.now().toString()
        );

        when(playerRepository.findById(1L)).thenReturn(Optional.of(samplePlayer));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(sampleTournament);
        when(tournamentPlayerStatsRepository.save(any(TournamentPlayerStats.class))).thenReturn(sampleStats);

        // Act
        Tournament result = tournamentService.createAndSaveTournament(tournamentData);

        // Assert
        assertNotNull(result);
        assertEquals(8, result.getTournamentSize());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
        verify(tournamentPlayerStatsRepository, times(1)).save(any(TournamentPlayerStats.class));
    }

    @Test
    void testGetPlayerMatchHistory_Success() {
        // Arrange
        Player player = new Player(1L, "TestPlayer", "TestProfilePic");

        Tournament tournament = new Tournament();
        tournament.setTournamentId(1L);
        tournament.setTournamentSize(8);
        tournament.setTimestampStart(LocalDateTime.now());

        TournamentPlayerStats sampleStats = new TournamentPlayerStats(
            1L, // TournamentPlayerId
            tournament, // Tournament
            player, // Player
            1, // Rank after tournament
            100, // Points obtained
            1L, // Champion ID
            1, // Final placement
            LocalDateTime.now(), // Time end per player
            5, // Kill count
            2, // Death count
            false // AFK status
        );

        when(tournamentPlayerStatsRepository.findByPlayerId(1L)).thenReturn(List.of(sampleStats));
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(championService.getChampionById(1L)).thenReturn(new Champion(1L, "TestChampion", "Role"));

        // Act
        List<Map<String, Object>> result = tournamentService.getPlayerMatchHistory(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(tournamentPlayerStatsRepository, times(1)).findByPlayerId(1L);
        verify(tournamentRepository, times(1)).findById(1L);
    }


    @Test
    void testGetPlayerMatchHistory_NoStats() {
        // Arrange
        when(tournamentPlayerStatsRepository.findByPlayerId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<Map<String, Object>> result = tournamentService.getPlayerMatchHistory(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tournamentPlayerStatsRepository, times(1)).findByPlayerId(1L);
    }
}
