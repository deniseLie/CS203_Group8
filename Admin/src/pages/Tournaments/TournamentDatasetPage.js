import React, { useState, useEffect } from 'react';
import { Box, Typography, Tabs, Tab, CircularProgress, Alert } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import TournamentTable from '../../components/TournamentTable';
import MatchesPopup from '../../components/MatchesPopup';

/**
 * TournamentsPage Component
 *
 * Displays a list of tournaments, matches, and player rankings.
 */
const TournamentsPage = () => {
  const [tournaments, setTournaments] = useState([]); // All tournaments
  const [selectedStatus, setSelectedStatus] = useState('Ongoing'); // Filter for status
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState(null); // Error state
  const [selectedTournament, setSelectedTournament] = useState(null); // Tournament for details popup

  // Fetch tournament data from the API
  // useEffect(() => {
  //   const fetchTournaments = async () => {
  //     try {
  //       setLoading(true);
  //       const response = await fetch('/admin/tournaments/getAllTournaments');
  //       if (!response.ok) throw new Error(`Error: ${response.statusText}`);
  //       const data = await response.json();
  //       setTournaments(data);
  //     } catch (err) {
  //       setError('Failed to fetch tournaments. Please try again.');
  //     } finally {
  //       setLoading(false);
  //     }
  //   };
  //   fetchTournaments();
  // }, []);

  // Use dummy data instead of fetching from API
  useEffect(() => {
    // Simulate fetching data with a timeout
    setTimeout(() => {
      setLoading(false);

      // Dummy data to replace API response
      const dummyTournaments = [
        {
          tournamentId: 1,
          status: 'Ongoing',
          timestampStart: '2024-11-01T10:00:00Z',
          timestampEnd: null,
          totalRounds: 4,
          currentRound: 2,
          players: [
            { playerId: 101, playerName: 'Alice', avatar: '' },
            { playerId: 102, playerName: 'Bob', avatar: '' },
            { playerId: 103, playerName: 'Charlie', avatar: '' },
          ],
          rounds: [
            {
              roundNumber: 1,
              matches: [
                { id: 1, player1: 'Alice', player2: 'Bob', winner: 'Alice' },
                { id: 2, player1: 'Charlie', player2: 'David', winner: 'Charlie' },
              ],
            },
            {
              roundNumber: 2,
              matches: [
                { id: 3, player1: 'Alice', player2: 'Charlie', winner: null },
              ],
            },
          ],
        },
        {
          tournamentId: 2,
          status: 'Completed',
          timestampStart: '2024-10-15T10:00:00Z',
          timestampEnd: '2024-10-20T18:00:00Z',
          totalRounds: 3,
          currentRound: 3,
          players: [
            { playerId: 201, playerName: 'Eve', avatar: '' },
            { playerId: 202, playerName: 'Frank', avatar: '' },
          ],
          rounds: [
            {
              roundNumber: 1,
              matches: [
                { id: 4, player1: 'Eve', player2: 'Frank', winner: 'Eve' },
              ],
            },
            {
              roundNumber: 2,
              matches: [
                { id: 5, player1: 'Eve', player2: 'Grace', winner: 'Eve' },
              ],
            },
            {
              roundNumber: 3,
              matches: [
                { id: 6, player1: 'Eve', player2: 'Hank', winner: 'Eve' },
              ],
            },
          ],
        },
      ];

      setTournaments(dummyTournaments);
    }, 1000);
  }, []);

  // Filter tournaments based on status
  const filteredTournaments = tournaments.filter(
    (tournament) => tournament.status === selectedStatus
  );

  /**
   * Handle updating a tournament when a match is modified.
   *
   * @param {object} updatedTournament - The updated tournament object.
   */
  const handleModifyTournament = (updatedTournament) => {
    setTournaments((prevTournaments) =>
      prevTournaments.map((tournament) =>
        tournament.tournamentId === updatedTournament.tournamentId
          ? updatedTournament
          : tournament
      )
    );
    setSelectedTournament(updatedTournament); // Keep the popup updated
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <TopBar />
        <Typography variant="h4" sx={{ mb: 3 }}>
          Tournaments
        </Typography>
        <Tabs
          value={selectedStatus}
          onChange={(e, newValue) => setSelectedStatus(newValue)}
          sx={{ mb: 3 }}
        >
          <Tab label="Ongoing" value="Ongoing" />
          <Tab label="Completed" value="Completed" />
        </Tabs>

        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <CircularProgress />
          </Box>
        )}

        {error && (
          <Box sx={{ mt: 3 }}>
            <Alert severity="error">{error}</Alert>
          </Box>
        )}

        {!loading && !error && (
          <TournamentTable
            data={filteredTournaments}
            onViewDetails={setSelectedTournament} // Show details popup
          />
        )}

        {selectedTournament && (
          <MatchesPopup
            tournament={selectedTournament}
            onClose={() => setSelectedTournament(null)}
            onModify={handleModifyTournament}
          />
        )}
      </Box>
    </Box>
  );
};

export default TournamentsPage;
