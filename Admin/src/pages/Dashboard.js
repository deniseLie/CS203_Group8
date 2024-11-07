import React from 'react';
import { Box, Typography, Stack } from '@mui/material';
import Sidebar from '../components/Sidebar';
import TopBar from '../components/TopBar';
import DashboardCard from '../components/DashboardCard';
import RecentTournaments from '../components/RecentTournaments';
import { Dashboard, SportsEsports, People } from '@mui/icons-material';

const DashboardPage = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Dashboard Content */}
        <Typography variant="h4" sx={{ mb: 2 }}>
          Dashboard
        </Typography>
        <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
          <DashboardCard title="Tournaments" count="19" subCount="2 Ongoing" icon={<Dashboard />} />
          <DashboardCard title="Matches" count="132" subCount="14 Ongoing" icon={<SportsEsports />} />
          <DashboardCard title="Players" count="238" subCount="45 Online" icon={<People />} />
          <DashboardCard title="Top Players" content={['Faker - 100%', 'KylieHill - 87.6%', 'TonyStark - 79.2%']} />
        </Stack>

        {/* Most Recent Tournaments Table */}
        <RecentTournaments />
      </Box>
    </Box>
  );
};

export default DashboardPage;
