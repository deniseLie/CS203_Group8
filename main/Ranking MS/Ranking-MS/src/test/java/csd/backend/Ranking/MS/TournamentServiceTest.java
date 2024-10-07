package csd.backend.Ranking.MS;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import csd.backend.Ranking.MS.Tournament;
import csd.backend.Ranking.MS.TournamentRepository;
import csd.backend.Ranking.MS.TournamentService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @Test
    public void testGetAllTournaments() {
        // Arrange: Create a mock list of tournaments
        Tournament tournament1 = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
        Tournament tournament2 = new Tournament(2L, LocalDateTime.of(2023, 9, 2, 10, 0), LocalDateTime.of(2023, 9, 2, 18, 0));

        List<Tournament> mockTournaments = Arrays.asList(tournament1, tournament2);

        // Act: Set up the mock behavior
        when(tournamentRepository.findAll()).thenReturn(mockTournaments);

        // Act: Call the service method
        List<Tournament> result = tournamentService.getAllTournaments();

        // Assert: Verify the results
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(tournament1, tournament2);

        // Verify: Ensure the repository method was called once
        verify(tournamentRepository, times(1)).findAll();
    }

    // @Test
    // public void testTimestampValidationError() {
    //     // Arrange: Create a tournament where timestampStart > timestampEnd
    //     LocalDateTime timestampStart = LocalDateTime.of(2023, 9, 2, 18, 0);  // End time
    //     LocalDateTime timestampEnd = LocalDateTime.of(2023, 9, 2, 10, 0);    // Start time

    //     Tournament invalidTournament = new Tournament(1L, timestampStart, timestampEnd);

    //     // Act & Assert: Expect an IllegalArgumentException when saving the invalid tournament
    //     assertThatThrownBy(() -> tournamentService.createTournament(invalidTournament))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("Start time cannot be after end time");

    //     // Verify: Ensure that the repository save method is never called due to the validation failure
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    @Test
    public void testCreateTournament() {
        // Arrange: Create a valid tournament
        Tournament newTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));

        // Act: Call the service method to create the tournament
        when(tournamentRepository.save(newTournament)).thenReturn(newTournament);
        Tournament createdTournament = tournamentService.createTournament(newTournament);

        // Assert: Verify the result
        assertThat(createdTournament).isNotNull();
        assertThat(createdTournament.getTournamentId()).isEqualTo(1L);

        // Verify: Ensure the repository's save method was called once
        verify(tournamentRepository, times(1)).save(newTournament);
    }

    // @Test
    // public void testGetTournamentById() {
    //     // Arrange: Create a mock tournament
    //     Tournament tournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));

    //     // Act: Mock the behavior of the repository
    //     when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(tournament));
    //     Tournament foundTournament = tournamentService.getTournamentById(1L);

    //     // Assert: Verify the result
    //     assertThat(foundTournament).isNotNull();
    //     assertThat(foundTournament.getTournamentId()).isEqualTo(1L);

    //     // Verify: Ensure the repository's findById method was called once
    //     verify(tournamentRepository, times(1)).findById(1L);
    // }

    // @Test
    // public void testUpdateTournament() {
    //     // Arrange: Create an existing tournament and an updated version
    //     Tournament existingTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     Tournament updatedTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 12, 0), LocalDateTime.of(2023, 9, 1, 18, 0));

    //     // Act: Mock the repository's behavior
    //     when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTournament));
    //     when(tournamentRepository.save(updatedTournament)).thenReturn(updatedTournament);

    //     Tournament result = tournamentService.updateTournament(updatedTournament);

    //     // Assert: Verify the result
    //     assertThat(result).isNotNull();
    //     assertThat(result.getTimestampStart()).isEqualTo(LocalDateTime.of(2023, 9, 1, 12, 0));

    //     // Verify: Ensure the repository's save method was called once
    //     verify(tournamentRepository, times(1)).save(updatedTournament);
    // }

    // @Test
    // public void testDeleteTournament() {
    //     // Act: Perform the delete operation
    //     tournamentService.deleteTournament(1L);

    //     // Verify: Ensure the repository's deleteById method was called once
    //     verify(tournamentRepository, times(1)).deleteById(1L);
    // }
}
