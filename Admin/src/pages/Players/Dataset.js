import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import PlayerTable from '../../components/PlayerTable';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';

// Import profile pictures dynamically
import * as profileImages from '../../assets/summonerIcon/1.jpg'; // assuming all images are in this directory

const PlayerDatasetPage = () => {
  const [playerData, setPlayerData] = useState([]);
  const [filteredData, setFilteredData] = useState([]); // State for filtered data
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterQuery, setFilterQuery] = useState(''); // State for the filter query

  const navigate = useNavigate();

  // Fetch player data from the API
  const fetchPlayerData = async () => {
    setLoading(true);
    try {
      const response = await api.get('/admin/user/getAllUser');
      const players = response.data; // Assuming the response data contains the player data
      setPlayerData(players); // Set the player data
      setFilteredData(players); // Set filtered data initially to all players
    } catch (error) {
      setError('Failed to fetch player data. Please try again later.');
      console.error('Error fetching player data:', error);
    } finally {
      setLoading(false);
    }
  };

  // Handle delete player
  const handleDeletePlayer = async (playerId) => {
    try {
      setPlayerData(prevData => prevData.filter(player => player.id !== playerId));
      setFilteredData(prevData => prevData.filter(player => player.id !== playerId)); // Also update filtered data
    } catch (error) {
      setError('Failed to delete player. Please try again.');
    }
  };

  // Handle edit player (navigate to the edit page)
  const handleEditPlayer = (player) => {
    navigate(`/players/edit`, { state: { player } });
  };

  // Handle filter input change
  const handleFilterChange = (event) => {
    const query = event.target.value.toLowerCase();
    setFilterQuery(query);

    const filtered = playerData.filter((player) => {
      return (
        player.id.toString().includes(query) ||
        player.username.toLowerCase().includes(query) ||
        player.playername.toLowerCase().includes(query)
      );
    });

    setFilteredData(filtered); // Update the filtered data in real-time
  };

  useEffect(() => {
    fetchPlayerData(); // Fetch data when the component mounts
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

        {/* Filter Bar */}
        <Box display="flex" marginBottom={2}>
          <TextField
            label="Filter by ID, Username, or Playername"
            value={filterQuery}
            onChange={handleFilterChange}
            fullWidth
            variant="outlined"
          />
        </Box>

        {loading ? (
          <Typography>Loading...</Typography>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <PlayerTable data={filteredData} onDelete={handleDeletePlayer} onEdit={handleEditPlayer} profileImages={profileImages} />
        )}
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
