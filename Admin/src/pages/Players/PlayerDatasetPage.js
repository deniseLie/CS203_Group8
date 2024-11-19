import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField } from '@mui/material';
import Sidebar from '../../components/Sidebar'; // Sidebar for navigation
import TopBar from '../../components/TopBar'; // Top bar component
import PlayerTable from '../../components/PlayerTable'; // Table component to display player data
import { useNavigate } from 'react-router-dom'; // Hook for programmatic navigation
import api from '../../services/api'; // API service for HTTP requests

// Import profile pictures dynamically
import * as profileImages from '../../assets/summonerIcon/1.jpg'; // Assuming all images are in this directory

/**
 * PlayerDatasetPage Component
 *
 * This component fetches and displays a list of players in a dataset format.
 * It supports filtering, editing, and deleting players.
 */
const PlayerDatasetPage = () => {
  const [playerData, setPlayerData] = useState([]); // State to store the full player dataset
  const [filteredData, setFilteredData] = useState([]); // State to store filtered data
  const [loading, setLoading] = useState(true); // State to indicate loading state
  const [error, setError] = useState(''); // State to store error messages
  const [filterQuery, setFilterQuery] = useState(''); // State to store the filter query

  const navigate = useNavigate(); // Hook to navigate between pages

  /**
   * Fetch player data from the API.
   */
  const fetchPlayerData = async () => {
    setLoading(true); // Set loading to true while fetching data
    try {
      const response = await api.get('/admin/users'); // Fetch all users from the API
      const players = response.data; // Assuming the response contains player data
      setPlayerData(players); // Set the full player data
      setFilteredData(players); // Initialize the filtered data to include all players
    } catch (error) {
      setError('Failed to fetch player data. Please try again later.'); // Handle API errors
      console.error('Error fetching player data:', error);
    } finally {
      setLoading(false); // Stop loading spinner
    }
  };

  /**
   * Delete a player by ID.
   * 
   * @param {string} playerId - The ID of the player to delete.
   */
  const handleDeletePlayer = async (playerId) => {
    try {
      // Simulate API call to delete the player
      setPlayerData((prevData) => prevData.filter((player) => player.id !== playerId));
      setFilteredData((prevData) => prevData.filter((player) => player.id !== playerId)); // Update filtered data as well
    } catch (error) {
      setError('Failed to delete player. Please try again.');
    }
  };

  /**
   * Navigate to the edit player page.
   * 
   * @param {object} player - The player object to edit.
   */
  const handleEditPlayer = (player) => {
    navigate(`/players/edit`, { state: { player } }); // Pass the player data to the edit page
  };

  /**
   * Handle changes in the filter input field.
   * Filters the dataset in real-time based on the query.
   * 
   * @param {object} event - The input change event.
   */
  const handleFilterChange = (event) => {
    const query = event.target.value.toLowerCase();
    setFilterQuery(query);

    const filtered = playerData.filter((player) => {
      return (
        player.id.toString().includes(query) || // Filter by ID
        player.username.toLowerCase().includes(query) || // Filter by username
        player.playername.toLowerCase().includes(query) // Filter by playername
      );
    });

    setFilteredData(filtered); // Update the filtered data
  };

  /**
   * Fetch player data when the component mounts.
   */
  useEffect(() => {
    fetchPlayerData(); // Fetch player data on initial render
  }, []);

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar for navigation */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <TopBar /> {/* Top bar for title or navigation */}
        
        {/* Header Section */}
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
            onChange={handleFilterChange} // Handle filter input changes
            fullWidth
            variant="outlined"
          />
        </Box>

        {/* Content Section */}
        {loading ? (
          <Typography>Loading...</Typography> // Show loading state
        ) : error ? (
          <Typography color="error">{error}</Typography> // Show error message
        ) : (
          <PlayerTable
            data={filteredData} // Pass filtered data to the table
            onDelete={handleDeletePlayer} // Pass delete handler
            onEdit={handleEditPlayer} // Pass edit handler
            profileImages={profileImages} // Pass profile images
          />
        )}
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
