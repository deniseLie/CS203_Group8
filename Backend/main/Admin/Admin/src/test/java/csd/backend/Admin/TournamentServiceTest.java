package csd.backend.Admin;

import csd.backend.Admin.Model.DTO.PlayerDTO;
import csd.backend.Admin.Model.DTO.TournamentDTO;
import csd.backend.Admin.Model.Tournament.Tournament;
import csd.backend.Admin.Model.Tournament.TournamentPlayer;
import csd.backend.Admin.Model.Tournament.TournamentRound;
import csd.backend.Admin.Model.User.Player;
import csd.backend.Admin.Repository.*;
import csd.backend.Admin.Repository.User.PlayerRepository;
import csd.backend.Admin.Service.SqsService;
import csd.backend.Admin.Service.TournamentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TournamentServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentPlayerRepository tournamentPlayerRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TournamentRoundRepository tournamentRoundRepository;

    @Mock
    private SqsService sqsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTournament() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setTournamentId(1L);
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        // Act
        Tournament result = tournamentService.createTournament(tournament);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTournamentId());
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testGetAllTournaments() {
        // Arrange
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament1, tournament2));

        // Act
        List<Tournament> result = tournamentService.getAllTournaments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void testGetTournamentById_Found() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setTournamentId(1L);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

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

        // Act
        Tournament result = tournamentService.getTournamentById(1L);

        // Assert
        assertNull(result);
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTournamentDetails() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setTournamentId(1L);
        tournament.setTournamentSize(8);

        TournamentPlayer tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setPlayerId(1L);

        Player player = new Player();
        player.setPlayerId(1L);
        player.setPlayerName("John Doe");

        TournamentRound latestRound = new TournamentRound();
        latestRound.setRoundNumber(2);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentPlayerRepository.findByTournament(tournament)).thenReturn(Collections.singletonList(tournamentPlayer));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(tournamentRoundRepository.findTopByTournamentOrderByRoundNumberDesc(tournament)).thenReturn(Optional.of(latestRound));

        // Act
        TournamentDTO result = tournamentService.getTournamentDetails(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getTournamentId());
        assertEquals(8, result.getTournamentSize());
        assertEquals(2, result.getCurrentRound());
        assertEquals(1, result.getPlayers().size());
        verify(tournamentRepository, times(1)).findById(1L);
        verify(tournamentPlayerRepository, times(1)).findByTournament(tournament);
        verify(playerRepository, times(1)).findById(1L);
        verify(tournamentRoundRepository, times(1)).findTopByTournamentOrderByRoundNumberDesc(tournament);
    }

    @Test
    void testUpdateTournamentSize() {
        // Arrange
        int newTournamentSize = 16;

        // Act
        tournamentService.updateTournamentSize(newTournamentSize);

        // Assert
        verify(sqsService, times(1)).sendMessageToQueue(eq("account"), anyString(), anyMap());
    }
}
