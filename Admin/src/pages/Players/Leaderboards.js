import React from 'react';
import { Box, Typography } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import FilterBar from '../../components/FilterBar'; // Optional filter bar if needed
import LeaderboardTable from '../../components/LeaderboardTable';

// Example data for the leaderboard
const leaderboardData = [
  { id: "#9093", username: "Daniel Park", elo: 2563, totalWins: 34, totalMatches: 68 },
  { id: "#9094", username: "Jane Doe", elo: 2450, totalWins: 20, totalMatches: 50 },
  // Add more player data as needed
];

const LeaderboardsPage = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Page Title */}
        <Typography variant="h4" sx={{ mb: 3 }}>
          Leaderboards
        </Typography>

        {/* Filter Bar */}
        <FilterBar />

        {/* Leaderboard Table */}
        <LeaderboardTable data={leaderboardData} />
      </Box>
    </Box>
  );
};

export default LeaderboardsPage;
