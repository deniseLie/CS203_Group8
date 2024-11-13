// import React from 'react';
// import { Box, Typography, Button } from '@mui/material';
// import Sidebar from '../../components/Sidebar';
// import TopBar from '../../components/TopBar';
// import FilterBar from '../../components/FilterBar'; // Ensure this is imported if you have it
// import PlayerTable from '../../components/PlayerTable';

// import { useNavigate } from 'react-router-dom'; // Import useNavigate
// const PlayerDatasetPage = () => {
//   // Example data for the leaderboard
//   const playerData = [
//     { id: "#9093", username: "Daniel Park", playername: 'Hide on rock', email: 'danielpark@gmail.com', authProvider: 'LOCAL' },
//     { id: "#9094", username: "Jane Doe", playername: 'Keria', email: 'janedoe@gmail.com', authProvider: 'LOCAL' },
//     // Add more player data as needed
//   ];

//   const navigate = useNavigate();

//   const handleCreateNewPlayer = ()=> {
//     navigate('/players/create')
//   }

//   return (
//     <Box sx={{ display: 'flex' }}>
//       {/* Sidebar */}
//       <Sidebar />

//       {/* Main Content */}
//       <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
//         {/* Top Bar */}
//         <TopBar />

//         {/* Page Title */}
//         <Box display={'flex'}marginBottom={2}  justifyContent={'space-between'} alignItems={'center'}>

//         <Typography variant="h4" sx={{ } } alignSelf={'center'} justifySelf={'center'}>
//           Player Dataset
//         </Typography>


//         <Button variant='outlined' onClick={handleCreateNewPlayer}>
//           <Typography>
//           Create Player
//           </Typography></Button>
//         </Box>
        
//         {/* Filter Bar */}
//         <FilterBar />

//         {/* Player Dataset Table */}
//         <PlayerTable data={playerData} />
//       </Box>
//     </Box>
//   );
// };

// export default PlayerDatasetPage;

import React, { useState, useEffect } from 'react';
import { Box, Typography, Button } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import FilterBar from '../../components/FilterBar';
import PlayerTable from '../../components/PlayerTable';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const PlayerDatasetPage = () => {
  const [playerData, setPlayerData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  
  const navigate = useNavigate();

  // Fetch player data from the API
  useEffect(() => {
    const fetchPlayerData = async () => {
      setLoading(true);
      try {
        const response = await axios.get('/admin/user/getAllUsers');
        setPlayerData(response.data);  // Assuming response.data is an array of players
      } catch (error) {
        setError('Failed to fetch player data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchPlayerData();
  }, []);

  const handleDeletePlayer = async (playerId) => {
    try {
      await axios.delete(`/admin/user/deleteUser`, { data: { id: playerId } });
      setPlayerData(prevData => prevData.filter(player => player.id !== playerId));
    } catch (error) {
      setError('Failed to delete player. Please try again.');
    }
  };

  const handleCreateNewPlayer = () => {
    navigate('/players/create');
  };

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Page Title */}
        <Box display="flex" marginBottom={2} justifyContent="space-between" alignItems="center">
          <Typography variant="h4">Player Dataset</Typography>
          <Button variant="outlined" onClick={handleCreateNewPlayer}>
            <Typography>Create Player</Typography>
          </Button>
        </Box>

        {/* Filter Bar */}
        <FilterBar />

        {/* Display loading, error, or data */}
        {loading ? (
          <Typography>Loading...</Typography>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <PlayerTable data={playerData} onDelete={handleDeletePlayer} />
        )}
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
