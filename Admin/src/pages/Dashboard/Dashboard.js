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
  const navigate = useNavigate(); // Hook for programmatic navigation
  const [data, setData] = useState([]); // State to store tournament data
  const [loading, setLoading] = useState(true); // State to track loading status
  const [error, setError] = useState(null); // State to store error messages

  /**
   * Fetch tournaments from the API on component mount.
   */
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await fetch('/admin/tournaments'); // API endpoint for fetching tournaments

        if (!response.ok) {
          const errorText = await response.text(); // Get detailed error message
          throw new Error(`Error: ${response.status} - ${errorText}`);
        }

        const result = await response.json(); // Parse JSON response
        setData(result); // Set tournament data in state
      } catch (error) {
        setError('Failed to fetch tournament data. Please try again later.');
        console.error('Error fetching tournament data:', error); // Log error for debugging
      } finally {
        setLoading(false); // Stop loading spinner
      }
    };

    fetchTournaments();
  }, []);

  /**
   * Navigate to the tournament configuration page.
   */
  const handleCreateNewProject = () => {
    navigate('/tournaments/configure');
  };

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Header Section */}
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
          <Typography variant="h4">Dashboard</Typography>
          <Button
            variant="contained"
            color="primary"
            sx={{ backgroundColor: '#1976d2' }}
            onClick={handleCreateNewProject}
          >
            Configure Tournaments
          </Button>
        </Box>

        {/* Loading Indicator */}
        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '50vh' }}>
            <CircularProgress />
          </Box>
        )}

        {/* Error Message */}
        {error && (
          <Alert severity="error" sx={{ mb: 4 }}>
            {error}
          </Alert>
        )}

        {/* Dashboard Content */}
        {!loading && !error && (
          <>
            {/* Dashboard Stats */}
            <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
              {/* Card for Tournaments */}
              <DashboardCard
                title="Tournaments"
                count={data.length} // Total tournaments
                subCount={`${data.filter(d => d.status === 'Ongoing').length} Ongoing`} // Ongoing tournaments
                icon={<Dashboard />}
              />

              {/* Card for Matches */}
              <DashboardCard
                title="Matches"
                count={data.reduce((sum, t) => sum + t.matchesCompleted, 0)} // Total matches completed
                subCount={`${data.reduce((sum, t) => t.status === 'Ongoing' ? sum + t.matchesCompleted : sum, 0)} Ongoing`} // Ongoing matches
                icon={<SportsEsports />}
              />

              {/* Card for Players */}
              <DashboardCard
                title="Players"
                count={data.reduce((sum, t) => sum + t.players.length, 0)} // Total players across tournaments
                subCount="Active Data Missing" // Placeholder for additional player stats
                icon={<People />}
              />

              {/* Card for Top Players */}
              <DashboardCard
                title="Top Players"
                content={['Faker - 100%', 'KylieHill - 87.6%', 'TonyStark - 79.2%']} // Example top players
                icon={<EmojiEventsIcon />}
              />
            </Stack>

            {/* Tournament Table */}
            <TournamentTable data={data} />
          </>
        )}
      </Box>
    </Box>
  );
};

export default DashboardPage;
