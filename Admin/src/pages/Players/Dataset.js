import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import FilterBar from '../../components/FilterBar'; // Ensure this is imported if you have it
import PlayerTable from '../../components/PlayerTable';

import { useNavigate } from 'react-router-dom'; // Import useNavigate
const PlayerDatasetPage = () => {
  // Example data for the leaderboard
  const playerData = [
    { id: "#9093", username: "Daniel Park", playername: 'Hide on rock', email: 'danielpark@gmail.com', authProvider: 'LOCAL' },
    { id: "#9094", username: "Jane Doe", playername: 'Keria', email: 'janedoe@gmail.com', authProvider: 'LOCAL' },
    // Add more player data as needed
  ];

  const navigate = useNavigate();

  const handleCreateNewPlayer = ()=> {
    navigate('/players/create')
  }

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Page Title */}
        <Box display={'flex'}marginBottom={2}  justifyContent={'space-between'} alignItems={'center'}>

        <Typography variant="h4" sx={{ } } alignSelf={'center'} justifySelf={'center'}>
          Player Dataset
        </Typography>


        <Button variant='outlined' onClick={handleCreateNewPlayer}>
          <Typography>
          Create Player
          </Typography></Button>
        </Box>
        
        {/* Filter Bar */}
        <FilterBar />

        {/* Player Dataset Table */}
        <PlayerTable data={playerData} />
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
