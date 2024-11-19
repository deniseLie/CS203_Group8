import React, { useEffect, useState } from 'react';
import { Box, Typography, Stack, Button, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import DashboardCard from '../../components/DashboardCard';
import { Dashboard, SportsEsports, People } from '@mui/icons-material';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import TournamentTable from '../../components/TournamentTable';

const DashboardPage = () => {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch tournaments from API
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await fetch('/admin/tournaments');

        if (!response.ok) {
          const errorText = await response.text(); // Get the response text for debugging
          throw new Error(`Error: ${response.status} - ${errorText}`);
        }

        const result = await response.json(); // Parse the JSON response
        setData(result); // Set the fetched data to the state
      } catch (error) {
        setError('Failed to fetch tournament data. Please try again later. ');
        console.error('Error fetching tournament data:', error);
      } finally {
        setLoading(false); // Always stop loading
      }
    };

    fetchTournaments();
  }, []);

  // Handle the button click to navigate to the /tournaments/configure page
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

        {/* Dashboard Content */}
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

        {/* Main Dashboard Content */}
        {!loading && !error && (
          <>
            <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
              <DashboardCard
                title="Tournaments"
                count={data.length}
                subCount={`${data.filter(d => d.status === 'Ongoing').length} Ongoing`}
                icon={<Dashboard />}
              />
              <DashboardCard
                title="Matches"
                count={data.reduce((sum, t) => sum + t.matchesCompleted, 0)}
                subCount={`${data.reduce((sum, t) => t.status === 'Ongoing' ? sum + t.matchesCompleted : sum, 0)} Ongoing`}
                icon={<SportsEsports />}
              />
              <DashboardCard
                title="Players"
                count={data.reduce((sum, t) => sum + t.players.length, 0)}
                subCount="Active Data Missing"
                icon={<People />}
              />
              <DashboardCard
                title="Top Players"
                content={['Faker - 100%', 'KylieHill - 87.6%', 'TonyStark - 79.2%']}
                icon={<EmojiEventsIcon />}
              />
            </Stack>

            {/* Most Recent Tournaments Table */}
            <TournamentTable data={data} />
          </>
        )}
      </Box>
    </Box>
  );
};

export default DashboardPage;
