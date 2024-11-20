import React, { useEffect, useState } from 'react';
import { Box, Typography, Stack, Button, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import DashboardCard from '../../components/DashboardCard';
import { Dashboard, People } from '@mui/icons-material';
import TournamentTable from '../../components/TournamentTable';
import api from '../../services/api';

/**
 * DashboardPage Component
 * 
 * This component renders the main dashboard page. It fetches and displays:
 * - Total number of tournaments
 * - Total number of registered players
 * - Number of active players in ongoing tournaments
 * - A table of recent tournaments
 */
const DashboardPage = () => {
  const navigate = useNavigate(); // For navigation to other pages
  const [data, setData] = useState([]); // State to store the list of tournaments
  const [recentTournaments, setRecentTournaments] = useState([]); // State for recent tournaments (last 6)
  const [totalPlayers, setTotalPlayers] = useState(0); // State to store the total number of players
  const [loading, setLoading] = useState(true); // Loading state for API calls
  const [error, setError] = useState(null); // Error state to display API fetch issues

  /**
   * Fetch tournament and player data on component mount
   */
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setLoading(true); // Start loading spinner

        // Fetch total players
        const userResponse = await api.get('/admin/user/getAllUsers');
        if (!userResponse.ok) {
          throw new Error(`Error fetching users: ${userResponse.statusText}`);
        }
        const users = await userResponse.json();
        setTotalPlayers(users.length); // Set total players count

        // Fetch tournaments
        const response = await api.get('/admin/tournament/getAllTournaments');
        if (!response.ok) {
          throw new Error(`Error fetching tournaments: ${response.statusText}`);
        }
        const result = await response.json();
        setData(result); // Store all tournament data
        setRecentTournaments(result.slice(0, 6)); // Store only the first 6 tournaments
      } catch (err) {
        setError(err.message); // Store error message for display
      } finally {
        setLoading(false); // Stop loading spinner
      }
    };

    fetchTournaments(); // Call the async function to fetch data
  }, []);

  /**
   * Calculate the number of active players in ongoing tournaments
   * @returns {number} Sum of players in all ongoing tournaments
   */
  const activePlayers = data
    .filter((tournament) => tournament.status === 'Ongoing') // Filter ongoing tournaments
    .reduce((sum, tournament) => sum + tournament.players.length, 0); // Sum up players in ongoing tournaments

  /**
   * Navigate to the tournament configuration page
   */
  const handleConfigureTournament = () => {
    navigate('/tournaments/configure');
  };

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar for navigation */}
      <Sidebar />

      {/* Main Content Area */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Header Section with Page Title and Configure Button */}
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

        {/* Show Loading Spinner While Fetching Data */}
        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '50vh' }}>
            <CircularProgress />
          </Box>
        ) : error ? (
          /* Show Error Message if Fetching Fails */
          <Alert severity="error" sx={{ mb: 4 }}>
            {error}
          </Alert>
        ) : (
          <>
            {/* Dashboard Summary Cards */}
            <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
              {/* Total Tournaments Card */}
              <DashboardCard
                title="Tournaments"
                count={data.length} // Total tournaments count
                subCount={`${data.filter((t) => t.status === 'Ongoing').length} Ongoing`} // Count of ongoing tournaments
                icon={<Dashboard />}
              />
              {/* Total Players Card */}
              <DashboardCard
                title="Players"
                count={totalPlayers} // Total number of registered players
                subCount={`${activePlayers} Active`} // Count of active players in ongoing tournaments
                icon={<People />}
              />
            </Stack>

            {/* Recent Tournaments Table */}
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
