import React from 'react';
import { Box, Typography, Stack, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import DashboardCard from '../../components/DashboardCard';
import RecentTournaments from '../../components/RecentTournaments';
import { Dashboard, SportsEsports, People } from '@mui/icons-material';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';

const DashboardPage = () => {

  const navigate = useNavigate(); // Initialize useNavigate

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
            onClick={handleCreateNewProject} // Add click handler
          >
            Configure Tournaments
          </Button>
        </Box>
        
        <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
          <DashboardCard title="Tournaments" count="19" subCount="2 Ongoing" icon={<Dashboard />} />
          <DashboardCard title="Matches" count="132" subCount="14 Ongoing" icon={<SportsEsports />} />
          <DashboardCard title="Players" count="238" subCount="45 Online" icon={<People />} />
          <DashboardCard title="Top Players" content={['Faker - 100%', 'KylieHill - 87.6%', 'TonyStark - 79.2%']} icon={<EmojiEventsIcon />} />
        </Stack>

        {/* Most Recent Tournaments Table */}
        <RecentTournaments />
      </Box>
    </Box>
  );
};

export default DashboardPage;
