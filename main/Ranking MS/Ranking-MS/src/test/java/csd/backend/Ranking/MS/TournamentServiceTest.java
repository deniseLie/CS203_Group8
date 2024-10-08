package csd.backend.Ranking.MS;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

    // // CREATE
    // @Test
    // public void createTournament_ValidInput_ReturnsCreatedTournament() {
    //     // Arrange
    //     Tournament newTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));

    //     // Act
    //     when(tournamentRepository.save(newTournament)).thenReturn(newTournament);
    //     Tournament createdTournament = tournamentService.createTournament(newTournament);

    //     // Assert
    //     assertThat(createdTournament).isNotNull();
    //     assertThat(createdTournament.getTournamentId()).isEqualTo(1);

    //     // Verify
    //     verify(tournamentRepository, times(1)).save(newTournament);
    // }

    // // Valid Case: Creating a tournament with a valid start time and no end time : when just start matching
    // @Test
    // public void createTournament_ValidWithoutEndTime_ReturnsCreatedTournament() {
    //     // Arrange
    //     Tournament newTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), null);

    //     // Act
    //     when(tournamentRepository.save(newTournament)).thenReturn(newTournament);
    //     Tournament createdTournament = tournamentService.createTournament(newTournament);

    //     // Assert
    //     assertThat(createdTournament).isNotNull();
    //     assertThat(createdTournament.getTournamentId()).isEqualTo(1);
    //     assertThat(createdTournament.getTimestampEnd()).isNull();  // End time should be null

    //     // Verify
    //     verify(tournamentRepository, times(1)).save(newTournament);
    // }

    // @Test
    // public void createTournament_NullInput_ThrowsNullPointerException() {
    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.createTournament(null))
    //         .isInstanceOf(NullPointerException.class)
    //         .hasMessageContaining("Tournament cannot be null");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void createTournament_DuplicateId_ThrowsDuplicateIdException() {
    //     // Arrange
    //     Tournament existingTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(existingTournament));

    //     // Act & Assert
    //     Tournament duplicateTournament = new Tournament(1, LocalDateTime.of(2023, 9, 2, 10, 0), LocalDateTime.of(2023, 9, 2, 18, 0));
    //     assertThatThrownBy(() -> tournamentService.createTournament(duplicateTournament))
    //         .isInstanceOf(DuplicateIdException.class)
    //         .hasMessageContaining("Tournament with ID already exists");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void createTournament_NullStartTime_ThrowsInvalidFieldException() {
    //     // Arrange
    //     Tournament invalidTournament = new Tournament(1L, null, LocalDateTime.of(2023, 9, 1, 18, 0));

    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.createTournament(invalidTournament))
    //         .isInstanceOf(InvalidFieldException.class)
    //         .hasMessageContaining("Tournament start time cannot be null");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void createTournament_StartTimeAfterEndTime_ThrowsIllegalArgumentException() {
    //     // Arrange
    //     Tournament invalidTournament = new Tournament(1L, LocalDateTime.of(2023, 9, 2, 18, 0), LocalDateTime.of(2023, 9, 2, 10, 0));

    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.createTournament(invalidTournament))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("Start time cannot be after end time");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void createTournament_StartTimeEqualToEndTime_ThrowsIllegalArgumentException() {
    //     // Arrange
    //     Tournament invalidTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 10, 0));

    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.createTournament(invalidTournament))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("Start time cannot be equal to or after end time");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // // READ
    // @Test
    // public void getAllTournaments_MultipleTournaments_ReturnsListOfTournaments() {
    //     // Arrange
    //     Tournament tournament1 = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     Tournament tournament2 = new Tournament(2L, LocalDateTime.of(2023, 9, 2, 10, 0), LocalDateTime.of(2023, 9, 2, 18, 0));
    //     List<Tournament> mockTournaments = Arrays.asList(tournament1, tournament2);

    //     // Act
    //     when(tournamentRepository.findAll()).thenReturn(mockTournaments);

    //     // Act
    //     List<Tournament> result = tournamentService.getAllTournaments();

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.size()).isEqualTo(2);
    //     assertThat(result).contains(tournament1, tournament2);

    //     // Verify
    //     verify(tournamentRepository, times(1)).findAll();
    // }

    // @Test
    // public void getTournamentById_TournamentExists_ReturnsTournament() {
    //     // Arrange
    //     Tournament mockTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));

    //     // Act
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(mockTournament));

    //     // Act
    //     Tournament result = tournamentService.getTournamentById(1);

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.getTournamentId()).isEqualTo(1);

    //     // Verify
    //     verify(tournamentRepository, times(1)).findById(1);
    // }

    // // UPDATE
    // @Test
    // public void updateTournament_ValidUpdate_SuccessfullyUpdatesStartAndEndTime() {
    //     // Arrange
    //     Tournament existingTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     Tournament updatedTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 12, 0), LocalDateTime.of(2023, 9, 1, 20, 0)); // Both start and end times updated

    //     // Act
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(existingTournament));
    //     when(tournamentRepository.save(any(Tournament.class))).thenReturn(updatedTournament);

    //     // Call the update method
    //     Tournament result = tournamentService.updateTournament(updatedTournament);

    //     // Assert
    //     assertThat(result.getTimestampStart()).isEqualTo(LocalDateTime.of(2023, 9, 1, 12, 0));
    //     assertThat(result.getTimestampEnd()).isEqualTo(LocalDateTime.of(2023, 9, 1, 20, 0));

    //     // Verify
    //     verify(tournamentRepository, times(1)).save(any(Tournament.class));
    // }

    // @Test
    // public void updateTournament_NoChanges_DoesNotModifyTournament() {
    //     // Arrange
    //     Tournament existingTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(existingTournament));

    //     // Act
    //     Tournament result = tournamentService.updateTournament(existingTournament);

    //     // Assert
    //     assertThat(result).isEqualTo(existingTournament);
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void updateTournament_TournamentNotFound_ThrowsRuntimeException() {
    //     // Arrange
    //     Tournament nonExistentTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.empty());

    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.updateTournament(nonExistentTournament))
    //         .isInstanceOf(RuntimeException.class)
    //         .hasMessageContaining("Tournament not found");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void updateTournament_NullTournament_ThrowsNullPointerException() {
    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.updateTournament(null))
    //         .isInstanceOf(NullPointerException.class)
    //         .hasMessageContaining("Tournament cannot be null");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void updateTournament_InvalidTimestamps_ThrowsIllegalArgumentException() {
    //     // Arrange
    //     Tournament existingTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     Tournament invalidTournament = new Tournament(1, LocalDateTime.of(2023, 9, 2, 18, 0), LocalDateTime.of(2023, 9, 2, 10, 0));

    //     // Mock repository behavior
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(existingTournament));

    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.updateTournament(invalidTournament))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("Start time cannot be after end time");

    //     // Verify
    //     verify(tournamentRepository, never()).save(any(Tournament.class));
    // }

    // @Test
    // public void updateTournament_ValidStartTimeAndNullEndTime_SuccessfullyUpdates() {
    //     // Arrange
    //     Tournament existingTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));

    //     // Tournament with updated start time but no end time
    //     Tournament updatedTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 12, 0), null);

    //     // Mock repository behavior
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(existingTournament));
    //     when(tournamentRepository.save(any(Tournament.class))).thenReturn(updatedTournament);

    //     // Act
    //     Tournament result = tournamentService.updateTournament(updatedTournament);

    //     // Assert
    //     assertThat(result.getTimestampStart()).isEqualTo(LocalDateTime.of(2023, 9, 1, 12, 0));
    //     assertThat(result.getTimestampEnd()).isNull();

    //     // Verify
    //     verify(tournamentRepository, times(1)).save(any(Tournament.class));
    // }


    // // DELETE
    // @Test
    // public void deleteTournament_TournamentExists_DeletesSuccessfully() {
    //     // Arrange
    //     Tournament existingTournament = new Tournament(1, LocalDateTime.of(2023, 9, 1, 10, 0), LocalDateTime.of(2023, 9, 1, 18, 0));
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.of(existingTournament));

    //     // Act
    //     tournamentService.deleteTournament(1);

    //     // Assert
    //     verify(tournamentRepository, times(1)).deleteById(1);
    // }

    // @Test
    // public void deleteTournament_TournamentNotFound_ThrowsRuntimeException() {
    //     // Arrange
    //     when(tournamentRepository.findById(1)).thenReturn(java.util.Optional.empty());

    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.deleteTournament(1))
    //         .isInstanceOf(RuntimeException.class)
    //         .hasMessageContaining("Tournament not found");

    //     // Verify
    //     verify(tournamentRepository, never()).deleteById(1);
    // }

    // @Test
    // public void deleteTournament_NullId_ThrowsIllegalArgumentException() {
    //     // Act & Assert:
    //     assertThatThrownBy(() -> tournamentService.deleteTournament(null))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("Tournament ID cannot be null");

    //     // Verify
    //     verify(tournamentRepository, never()).deleteById(any());
    // }

    // @Test
    // public void deleteTournament_InvalidId_ThrowsIllegalArgumentException() {
    //     // Act & Assert
    //     assertThatThrownBy(() -> tournamentService.deleteTournament(-1))
    //         .isInstanceOf(IllegalArgumentException.class)
    //         .hasMessageContaining("Invalid tournament ID");

    //     // Verify
    //     verify(tournamentRepository, never()).deleteById(any());
    // }
}
