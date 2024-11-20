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

      const playersFromDataset = [
        { playerName: "avexx", playerId: 101 },
        { playerName: "Rodan", playerId: 102 },
        { playerName: "xDivineSword", playerId: 103 },
        { playerName: "lilWanton", playerId: 104 },
        { playerName: "DarkStar", playerId: 105 },
        { playerName: "Nebula", playerId: 106 },
        { playerName: "VoidWalker", playerId: 107 },
        { playerName: "WindRider", playerId: 108 },
      ];

      // Dummy data to replace API response
      const dummyTournaments = [
        {
          tournamentId: 1,
          status: 'Ongoing',
          timestampStart: '2024-11-01T10:00:00Z',
          timestampEnd: null,
          totalRounds: 4,
          currentRound: 2,
          players: playersFromDataset.slice(0, 4), // Use first 4 players
          rounds: [
            {
              roundNumber: 1,
              matches: [
                { id: 1, player1: playersFromDataset[0].playerName, player2: playersFromDataset[1].playerName, winner: playersFromDataset[0].playerName },
                { id: 2, player1: playersFromDataset[2].playerName, player2: playersFromDataset[3].playerName, winner: playersFromDataset[2].playerName },
              ],
            },
            {
              roundNumber: 2,
              matches: [
                { id: 3, player1: playersFromDataset[0].playerName, player2: playersFromDataset[2].playerName, winner: null },
              ],
            },
          ],
        },
        ...Array.from({ length: 9 }).map((_, index) => ({
          tournamentId: index + 2,
          status: 'Completed',
          timestampStart: `2024-10-${String(index + 1).padStart(2, '0')}T10:00:00Z`,
          timestampEnd: `2024-10-${String(index + 5).padStart(2, '0')}T18:00:00Z`,
          totalRounds: 3,
          currentRound: 3,
          players: playersFromDataset.slice(index % playersFromDataset.length, (index % playersFromDataset.length) + 2), // Rotate players
          rounds: [
            {
              roundNumber: 1,
              matches: [
                {
                  id: index * 10 + 1,
                  player1: playersFromDataset[index % playersFromDataset.length].playerName,
                  player2: playersFromDataset[(index + 1) % playersFromDataset.length].playerName,
                  winner: playersFromDataset[index % playersFromDataset.length].playerName,
                },
              ],
            },
            {
              roundNumber: 2,
              matches: [
                {
                  id: index * 10 + 2,
                  player1: playersFromDataset[index % playersFromDataset.length].playerName,
                  player2: playersFromDataset[(index + 2) % playersFromDataset.length].playerName,
                  winner: playersFromDataset[index % playersFromDataset.length].playerName,
                },
              ],
            },
            {
              roundNumber: 3,
              matches: [
                {
                  id: index * 10 + 3,
                  player1: playersFromDataset[index % playersFromDataset.length].playerName,
                  player2: playersFromDataset[(index + 3) % playersFromDataset.length].playerName,
                  winner: playersFromDataset[index % playersFromDataset.length].playerName,
                },
              ],
            },
          ],
        })),
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
