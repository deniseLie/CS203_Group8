package csd.backend.Ranking.MS;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import csd.backend.Ranking.MS.TournamentPlayerStats;
import csd.backend.Ranking.MS.TournamentPlayerStatsRepository;
import csd.backend.Ranking.MS.TournamentPlayerStatsService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TournamentPlayerStatsServiceTest {

    @Mock
    private TournamentPlayerStatsRepository tournamentPlayerStatsRepository;

    @InjectMocks
    private TournamentPlayerStatsService tournamentPlayerStatsService;

    // CREATE
    // @Test
    // public void createTournamentPlayerStats_ValidInput_ReturnsCreatedStats() {
    //     // Arrange
    //     TournamentPlayerStats newStats = new TournamentPlayerStats(1, 1, 2, 20, 101, 1, 3.5, 3600, 5000, 4500, 5);
    //     when(tournamentPlayerStatsRepository.save(newStats)).thenReturn(newStats);

    //     // Act
    //     TournamentPlayerStats createdStats = tournamentPlayerStatsService.createTournamentPlayerStats(newStats);

    //     // Assert
    //     assertThat(createdStats).isNotNull();
    //     assertThat(createdStats.getPlayerId()).isEqualTo(1);
    //     assertThat(createdStats.getTournamentId()).isEqualTo(1);

    //     // Verify
    //     verify(tournamentPlayerStatsRepository, times(1)).save(newStats);
    // }

    // // READ
    // @Test
    // public void getAllTournamentPlayerStats_MultipleStats_ReturnsListOfStats() {
    //     // Arrange
    //     TournamentPlayerStats stats1 = new TournamentPlayerStats(1, 1, 2, 20, 101, 1, 3.5, 3600, 5000, 4500, 5);
    //     TournamentPlayerStats stats2 = new TournamentPlayerStats(2, 1, 3, 15, 102, 2, 2.8, 3700, 4800, 4600, 4);
    //     List<TournamentPlayerStats> mockStatsList = Arrays.asList(stats1, stats2);

    //     when(tournamentPlayerStatsRepository.findAll()).thenReturn(mockStatsList);

    //     // Act
    //     List<TournamentPlayerStats> result = tournamentPlayerStatsService.getAllTournamentPlayerStats();

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.size()).isEqualTo(2);
    //     assertThat(result).contains(stats1, stats2);

    //     // Verify
    //     verify(tournamentPlayerStatsRepository, times(1)).findAll();
    // }

    // @Test
    // public void getTournamentPlayerStatsById_StatsExists_ReturnsStats() {
    //     // Arrange
    //     TournamentPlayerStatsId id = new TournamentPlayerStatsId(1, 1);
    //     TournamentPlayerStats mockStats = new TournamentPlayerStats(1, 1, 2, 20, 101, 1, 3.5, 3600, 5000, 4500, 5);
    //     when(tournamentPlayerStatsRepository.findById(id)).thenReturn(Optional.of(mockStats));

    //     // Act
    //     TournamentPlayerStats result = tournamentPlayerStatsService.getTournamentPlayerStatsById(id);

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.getPlayerId()).isEqualTo(1);

    //     // Verify
    //     verify(tournamentPlayerStatsRepository, times(1)).findById(1);
    // }

    // // UPDATE
    // @Test
    // public void updateTournamentPlayerStats_ValidUpdate_ReturnsUpdatedStats() {
    //     // Arrange
    //     TournamentPlayerStatsId id = new TournamentPlayerStatsId(1, 1);
    //     TournamentPlayerStats existingStats = new TournamentPlayerStats(1, 1, 2, 20, 101, 1, 3.5, 3600, 5000, 4500, 5);
    //     TournamentPlayerStats updatedStats = new TournamentPlayerStats(1, 1, 2, 25, 101, 1, 3.8, 3700, 5100, 4600, 6);
    //     when(tournamentPlayerStatsRepository.findById(id)).thenReturn(Optional.of(existingStats));
    //     when(tournamentPlayerStatsRepository.save(any(TournamentPlayerStats.class))).thenReturn(updatedStats);

    //     // Act
    //     TournamentPlayerStats result = tournamentPlayerStatsService.updateTournamentPlayerStats(updatedStats);

    //     // Assert
    //     assertThat(result).isNotNull();
    //     assertThat(result.getPointObtain()).isEqualTo(25);
    //     assertThat(result.getKdRate()).isEqualTo(3.8);

    //     // Verify
    //     verify(tournamentPlayerStatsRepository, times(1)).save(updatedStats);
    // }

    // // DELETE
    // @Test
    // public void deleteTournamentPlayerStats_StatsExists_DeletesSuccessfully() {
    //     // Arrange
    //     TournamentPlayerStatsId id = new TournamentPlayerStatsId(1, 1);
    //     TournamentPlayerStats existingStats = new TournamentPlayerStats(1, 1, 2, 20, 101, 1, 3.5, 3600, 5000, 4500, 5);
    //     when(tournamentPlayerStatsRepository.findById(id)).thenReturn(Optional.of(existingStats));

    //     // Act
    //     tournamentPlayerStatsService.deleteTournamentPlayerStats(id); 

    //     // Verify
    //     verify(tournamentPlayerStatsRepository, times(1)).deleteById(id);
    // }
}
