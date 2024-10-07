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

    // CREATE
    @Test
    public void createTournament_ValidInput_ReturnsCreatedTournament() {
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

    @Test
    public void createTournament_NullInput_ThrowsNullPointerException() {
        // Act & Assert: Expect a NullPointerException when trying to create a null tournament
        assertThatThrownBy(() -> tournamentService.createTournament(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Tournament cannot be null");

        // Verify: Ensure the repository save method was never called
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    public void createTournament_DuplicateId_ThrowsDuplicateIdException() {
        // Arrange: Mock existing tournament
        Tournament existingTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
        when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTournament));

        // Act & Assert: Expect exception when creating a tournament with the same ID
        Tournament duplicateTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 2, 10, 0), LocalDateTime.of(2023, 9, 2, 18, 0));
        assertThatThrownBy(() -> tournamentService.createTournament(duplicateTournament))
            .isInstanceOf(DuplicateIdException.class)
            .hasMessageContaining("Tournament with ID already exists");

        // Verify: Ensure save method was not called
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    @Test
    public void createTournament_InvalidFields_ThrowsInvalidFieldException() {
        // Act & Assert: Expect exception when creating a tournament with invalid fields
        Tournament invalidTournament = new Tournament(null, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
        assertThatThrownBy(() -> tournamentService.createTournament(invalidTournament))
            .isInstanceOf(InvalidFieldException.class)
            .hasMessageContaining("Tournament fields are invalid");

        // Verify: Ensure save method was not called
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }



    // READ
    @Test
    public void getAllTournaments_MultipleTournaments_ReturnsListOfTournaments() {
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

    @Test
    public void getAllTournaments_NoTournaments_ReturnsEmptyList() {
        // Act: Mock an empty list
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList());

        // Call the service method
        List<Tournament> result = tournamentService.getAllTournaments();

        // Assert: Verify that the result is an empty list
        assertThat(result).isEmpty();

        // Verify: Ensure the repository findAll method was called once
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    public void getTournamentById_TournamentNotFound_ThrowsRuntimeException() {
        // Arrange: Mock the repository to return an empty Optional
        when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert: Expect a RuntimeException or a custom exception
        assertThatThrownBy(() -> tournamentService.getTournamentById(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Tournament not found");

        // Verify: Ensure the repository findById method was called once
        verify(tournamentRepository, times(1)).findById(1L);
    }

    // UPDATE
    @Test
    public void updateTournament_ValidUpdate_SuccessfullyUpdatesStartAndEndTime() {
        // Arrange: Original tournament
        Tournament existingTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
        Tournament updatedTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 12, 0), LocalDateTime.of(2023, 9, 1, 20, 0)); // Both start and end times updated

        // Act: Mock the repository behavior
        when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(updatedTournament);

        // Call the update method
        Tournament result = tournamentService.updateTournament(updatedTournament);

        // Assert: Verify that both start and end times were updated
        assertThat(result.getTimestampStart()).isEqualTo(LocalDateTime.of(2023, 9, 1, 12, 0));
        assertThat(result.getTimestampEnd()).isEqualTo(LocalDateTime.of(2023, 9, 1, 20, 0));

        // Verify: Ensure the repository's save method was called once
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    public void updateTournament_NoChanges_DoesNotModifyTournament() {
        // Arrange: Mock existing tournament
        Tournament existingTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
        when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTournament));

        // Act: Call update without any changes
        Tournament result = tournamentService.updateTournament(existingTournament);

        // Assert: Verify that no changes were made
        assertThat(result).isEqualTo(existingTournament);
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }


    @Test
    public void updateTournament_InvalidTimestamps_ThrowsIllegalArgumentException() {
        // Arrange: Existing tournament
        Tournament existingTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
        Tournament invalidTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 2, 18, 0), LocalDateTime.of(2023, 9, 2, 10, 0));

        // Mock repository behavior
        when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(existingTournament));

        // Act & Assert: Expect an exception when trying to update
        assertThatThrownBy(() -> tournamentService.updateTournament(invalidTournament))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Start time cannot be after end time");

        // Verify: Ensure the repository save method was not called due to validation failure
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }

    // DELETE
    @Test
    public void deleteTournament_TournamentNotFound_ThrowsRuntimeException() {
        // Arrange: Mock repository to return empty
        doThrow(new RuntimeException("Tournament not found")).when(tournamentRepository).deleteById(1L);

        // Act & Assert: Expect a RuntimeException
        assertThatThrownBy(() -> tournamentService.deleteTournament(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Tournament not found");

        // Verify: Ensure the repository deleteById method was called once
        verify(tournamentRepository, times(1)).deleteById(1L);
    }

}
