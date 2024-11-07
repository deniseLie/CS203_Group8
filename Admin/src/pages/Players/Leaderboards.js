import React from 'react';
import { Box, Typography } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import FilterBar from '../../components/FilterBar'; // Optional filter bar if needed
import LeaderboardTable from '../../components/LeaderboardTable';

const leaderboardData = [
    {
      id: "#9093",
      username: "Daniel Park",
      elo: 2563,
      rankIcon: '/path/to/rank_icon.png', // replace with your rank image source
      avgPlace: 2.13,
      firstPlacePercentage: '61.7%',
      avgKdRate: 3.1,
      totalWins: 34,
      totalMatchNumber: 68
    },
    {
      id: "#9094", // Changed to a unique ID
      username: "Jane Doe",
      elo: 2450,
      rankIcon: '/path/to/rank_icon.png', // replace with your rank image source
      avgPlace: 3.00,
      firstPlacePercentage: '50.0%',
      avgKdRate: 2.5,
      totalWins: 20,
      totalMatchNumber: 50
    },
    // Add more unique player objects as needed
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
