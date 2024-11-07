// src/pages/TournamentPage.js
import React from 'react';
import { Box, Typography, Stack } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import FilterBar from '../../components/FilterBar';
import TournamentTable from '../../components/TournamentTable';

const TournamentPage = ({ title, status, data }) => {
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
          {title}
        </Typography>

        {/* Filter Bar */}
        <FilterBar />

        {/* Tournament Table */}
        <TournamentTable data={data} status={status} />
      </Box>
    </Box>
  );
};

export default TournamentPage;
