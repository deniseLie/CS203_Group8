import React, { useEffect, useState } from 'react';
import { Box, Typography, Stack, Button, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom'; // For navigation
import Sidebar from '../../components/Sidebar'; // Sidebar component
import TopBar from '../../components/TopBar'; // TopBar component
import DashboardCard from '../../components/DashboardCard'; // Card component for dashboard stats
import { Dashboard, SportsEsports, People } from '@mui/icons-material'; // Icons for dashboard stats
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents'; // Icon for top players
import TournamentTable from '../../components/TournamentTable'; // Table component for tournaments

/**
 * DashboardPage Component
 * 
 * Displays a dashboard with tournament data, statistics, and navigation options.
 */
const DashboardPage = () => {
  const navigate = useNavigate();
  const [data, setData] = useState([]); // Tournament data
  const [recentTournaments, setRecentTournaments] = useState([]); // Recent tournaments
  const [totalPlayers, setTotalPlayers] = useState(0); // Total players
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState(null); // Error state

  /**
   * Fetch tournaments from the API on component mount.
   */
  // useEffect(() => {
  //   const fetchTournaments = async () => {
  //     try {
  //       // Fetch total players
  //       const userResponse = await fetch('/admin/user/getAllUsers');
  //       if (!userResponse.ok) {
  //         throw new Error(`Error fetching users: ${userResponse.statusText}`);
  //       }
  //       const users = await userResponse.json();
  //       setTotalPlayers(users.length); // Set total players count

  //       const response = await fetch('/admin/tournament/getAllTournaments'); // Updated API endpoint

  //       if (!response.ok) {
  //         const errorText = await response.text(); // Get detailed error message
  //         throw new Error(`Error: ${response.status} - ${errorText}`);
  //       }

  //       const result = await response.json(); // Parse JSON response
  //       setData(result); // Set all tournament data in state
  //       setRecentTournaments(result.slice(0, 6)); // Retrieve the first 6 tournaments
  //     } catch (error) {
  //       setError('Failed to fetch tournament data. Please try again later.');
  //       console.error('Error fetching tournament data:', error); // Log error for debugging
  //     } finally {
  //       setLoading(false); // Stop loading spinner
  //     }
  //   };

  //   fetchTournaments();
  // }, []);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        // Dummy dataset
        const playersFromDataset = [
          { playerName: 'avexx', playerId: 101 },
          { playerName: 'Rodan', playerId: 102 },
          { playerName: 'xDivineSword', playerId: 103 },
          { playerName: 'lilWanton', playerId: 104 },
          { playerName: 'DarkStar', playerId: 105 },
          { playerName: 'Nebula', playerId: 106 },
          { playerName: 'VoidWalker', playerId: 107 },
          { playerName: 'WindRider', playerId: 108 },
        ];

        setTotalPlayers(playersFromDataset.length);

        const dummyTournaments = [
          {
            tournamentId: 1,
            status: 'Ongoing',
            timestampStart: '2024-11-01T10:00:00Z',
            timestampEnd: null,
            totalRounds: 4,
            currentRound: 2,
            players: playersFromDataset.slice(0, 4),
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
            players: playersFromDataset.slice(index % playersFromDataset.length, (index % playersFromDataset.length) + 2),
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
            ],
          })),
        ];

        setData(dummyTournaments.sort((a, b) => b.tournamentId - a.tournamentId));
        setRecentTournaments(dummyTournaments.slice(0, 6));
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const activePlayers = data
    .filter((t) => t.status === 'Ongoing')
    .reduce((sum, t) => sum + t.players.length, 0);

  const handleConfigureTournament = () => {
    navigate('/tournaments/configure');
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <TopBar />

        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
          <Typography variant="h4">Dashboard</Typography>
          <Button
            variant="contained"
            color="primary"
            sx={{ backgroundColor: '#1976d2' }}
            onClick={handleConfigureTournament}
          >
            Configure Tournaments
          </Button>
        </Box>

        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '50vh' }}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Alert severity="error" sx={{ mb: 4 }}>
            {error}
          </Alert>
        ) : (
          <>
            <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
              <DashboardCard
                title="Tournaments"
                count={data.length}
                subCount={`${data.filter((t) => t.status === 'Ongoing').length} Ongoing`}
                icon={<Dashboard />}
              />
              <DashboardCard
                title="Players"
                count={totalPlayers}
                subCount={`${activePlayers} Active`}
                icon={<People />}
              />
            </Stack>
            <Typography variant="h6" sx={{ mb: 2 }}>
              Recent Tournaments
            </Typography>
            <TournamentTable data={recentTournaments} />
          </>
        )}
      </Box>
    </Box>
  );
};

export default DashboardPage;