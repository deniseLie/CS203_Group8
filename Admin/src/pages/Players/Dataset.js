import React, { useState, useEffect } from 'react';
import { Box, Typography, Button } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import FilterBar from '../../components/FilterBar';
import PlayerTable from '../../components/PlayerTable';
import { useNavigate } from 'react-router-dom';

// Import profile pictures
import player1Profile from '../../assets/summonerIcon/1.jpg';
import player2Profile from '../../assets/summonerIcon/2.jpg';
import player3Profile from '../../assets/summonerIcon/8.jpg';

const PlayerDatasetPage = () => {
  const [playerData, setPlayerData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  
  const navigate = useNavigate();

  // Mock fetchPlayerData API call
  const fetchPlayerData = async () => {
    setLoading(true);
    try {
      // Simulate fetching data from an API with the new fields
      const mockPlayerData = [
        { 
          userId: 1, 
          id: 1, 
          username: 'player1', 
          playername: 'Player One', 
          email: 'player1@example.com',
          profilePicture: player1Profile
        },
        { 
          userId: 2, 
          id: 2, 
          username: 'player2', 
          playername: 'Player Two', 
          email: 'player2@example.com',
          profilePicture: player2Profile
        },
        { 
          userId: 3, 
          id: 3, 
          username: 'player3', 
          playername: 'Player Three', 
          email: 'player3@example.com',
          profilePicture: player3Profile
        },
      ];
      setPlayerData(mockPlayerData); // Set mock player data
    } catch (error) {
      setError('Failed to fetch player data. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  // Handle delete player
  const handleDeletePlayer = async (playerId) => {
    try {
      setPlayerData(prevData => prevData.filter(player => player.id !== playerId));
    } catch (error) {
      setError('Failed to delete player. Please try again.');
    }
  };

  // Handle edit player (navigate to the edit page)
  const handleEditPlayer = (player) => {
    navigate(`/players/edit`, { state: { player } });
  };

  // Update player data
  const updatePlayer = (updatedPlayer) => {
    setPlayerData((prevData) =>
      prevData.map((player) =>
        player.id === updatedPlayer.id ? { ...player, ...updatedPlayer } : player
      )
    );
  };

  useEffect(() => {
    fetchPlayerData();
  }, []);

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <TopBar />
        <Box display="flex" marginBottom={2} justifyContent="space-between" alignItems="center">
          <Typography variant="h4">Player Dataset</Typography>
          <Button variant="outlined" onClick={() => navigate('/players/create')}>
            Create Player
          </Button>
        </Box>

        {loading ? (
          <Typography>Loading...</Typography>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <PlayerTable data={playerData} onDelete={handleDeletePlayer} onEdit={handleEditPlayer} />
        )}
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
