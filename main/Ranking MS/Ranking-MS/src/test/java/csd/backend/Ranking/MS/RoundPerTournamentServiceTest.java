package csd.backend.Ranking.MS;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import csd.backend.Ranking.MS.RoundPerTournament;
import csd.backend.Ranking.MS.RoundPerTournamentId;
import csd.backend.Ranking.MS.RoundPerTournamentRepository;
import csd.backend.Ranking.MS.RoundPerTournamentService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoundPerTournamentServiceTest {

    @Mock
    private RoundPerTournamentRepository roundPerTournamentRepository;

    @InjectMocks
    private RoundPerTournamentService roundPerTournamentService;

    // CREATE
    // @Test
    // public void createRoundPerTournament_ValidInput_ReturnsCreatedRound() {
    //     // Arrange
    //     RoundPerTournament newRound = new RoundPerTournament(1, 101, 102, true, 1, 100, 50, 30, 80);
    //     when(roundPerTournamentRepository.save(newRound)).thenReturn(newRound);

    //     // Act
    //     RoundPerTournament createdRound = roundPerTournamentService.createRoundPerTournament(newRound);

    //     // Assert
    //     assertThat(createdRound).isNotNull();
    //     assertThat(createdRound.getTournamentId()).isEqualTo(1);
    //     assertThat(createdRound.getFirstPlayerId()).isEqualTo(101);

    //     // Verify
    //     verify(roundPerTournamentRepository, times(1)).save(newRound);
    // }

    // // READ
    // @Test
    // public void getAllRoundsPerTournament_MultipleRounds_ReturnsListOfRounds() {
    //     // Arrange
    //     RoundPerTournament round1 = new RoundPerTournament(1, 101, 102, true, 1, 100, 50, 30, 80);
    //     RoundPerTournament round2 = new RoundPerTournament(2, 103, 104, false, 2, 90, 40, 60, 70);
    //     List<RoundPerTournament> mockRoundsList = Arrays.asList(round1, round2);

    //     when(roundPerTournamentRepository.findAll()).thenReturn(mockRoundsList);

    //     // Act
    //     List<RoundPerTournament> result = roundPerTournamentService.getAllRoundsPerTournament();

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.size()).isEqualTo(2);
    //     assertThat(result).contains(round1, round2);

    //     // Verify
    //     verify(roundPerTournamentRepository, times(1)).findAll();
    // }

    // @Test
    // public void getRoundPerTournamentById_RoundExists_ReturnsRound() {
    //     // Arrange
    //     RoundPerTournamentId id = new RoundPerTournamentId(1, 101, 102);
    //     RoundPerTournament mockRound = new RoundPerTournament(1, 101, 102, true, 1, 100, 50, 30, 80);
    //     when(roundPerTournamentRepository.findById(id)).thenReturn(Optional.of(mockRound));

    //     // Act
    //     RoundPerTournament result = roundPerTournamentService.getRoundPerTournamentById(id);

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.getTournamentId()).isEqualTo(1);

    //     // Verify
    //     verify(roundPerTournamentRepository, times(1)).findById(id);
    // }

    // // UPDATE
    // @Test
    // public void updateRoundPerTournament_ValidUpdate_ReturnsUpdatedRound() {
    //     // Arrange
    //     RoundPerTournamentId id = new RoundPerTournamentId(1, 101, 102);
    //     RoundPerTournament existingRound = new RoundPerTournament(1, 101, 102, true, 1, 100, 50, 30, 80);
    //     RoundPerTournament updatedRound = new RoundPerTournament(1, 101, 102, false, 1, 90, 40, 60, 70);
    //     when(roundPerTournamentRepository.findById(id)).thenReturn(Optional.of(existingRound));
    //     when(roundPerTournamentRepository.save(any(RoundPerTournament.class))).thenReturn(updatedRound);

    //     // Act
    //     RoundPerTournament result = roundPerTournamentService.updateRoundPerTournament(updatedRound);

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.isFirstPlayerWin()).isEqualTo(false);
    //     assertThat(result.getFirstPlayerHealth()).isEqualTo(90);

    //     // Verify
    //     verify(roundPerTournamentRepository, times(1)).save(updatedRound);
    // }

    // // DELETE
    // @Test
    // public void deleteRoundPerTournament_RoundExists_DeletesSuccessfully() {
    //     // Arrange
    //     RoundPerTournamentId id = new RoundPerTournamentId(1, 101, 102);
    //     RoundPerTournament existingRound = new RoundPerTournament(1, 101, 102, true, 1, 100, 50, 30, 80);
    //     when(roundPerTournamentRepository.findById(id)).thenReturn(Optional.of(existingRound));

    //     // Act
    //     roundPerTournamentService.deleteRoundPerTournament(id);

    //     // Verify
    //     verify(roundPerTournamentRepository, times(1)).deleteById(id);
    // }
}
