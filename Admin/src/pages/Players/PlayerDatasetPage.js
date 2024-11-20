import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField } from '@mui/material';
import Sidebar from '../../components/Sidebar'; // Sidebar for navigation
import TopBar from '../../components/TopBar'; // Top bar component
import PlayerTable from '../../components/PlayerTable'; // Table component to display player data
import { useNavigate } from 'react-router-dom'; // Hook for programmatic navigation
import api from '../../services/api'; // API service for HTTP requests

/**
 * Dynamically imports all profile pictures from the assets directory.
 */
const importAllImages = (requireContext) => {
  const images = {};
  requireContext.keys().forEach((key) => {
    const fileName = key.replace('./', ''); // Remove './' from the file name
    images[fileName] = requireContext(key);
  });
  return images;
};

// Import all images from the specified directory
const profileImages = importAllImages(require.context('../../assets/summonerIcon', false, /\.(png|jpe?g|svg)$/));

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
  // const fetchPlayerData = async () => {
  //   setLoading(true); // Set loading to true while fetching data
  //   try {
  //     const response = await api.get('/admin/users'); // Fetch all users from the API
  //     const players = response.data.map((player) => ({
  //       ...player,
  //       profilePicture: profileImages[player.profilePicture || 'default.jpg'], // Map the profile picture
  //     }));
  //     setPlayerData(players); // Set the full player data
  //     setFilteredData(players); // Initialize the filtered data to include all players
  //   } catch (error) {
  //     setError('Failed to fetch player data. Please try again later.'); // Handle API errors
  //     console.error('Error fetching player data:', error);
  //   } finally {
  //     setLoading(false); // Stop loading spinner
  //   }
  // };

  /**
   * Fetch player data (replaced with dummy data).
   */
  const fetchPlayerData = async () => {
    setLoading(true); // Set loading to true while fetching data
    try {
      // Dummy data for players
      const players = [
        { id: 1, username: 'avexx', playername: 'avexx', email: 'avexx@example.com', profilePicture: '1.jpg' },
        { id: 2, username: 'Rodan', playername: 'Rodan', email: 'rodan@example.com', profilePicture: '2.jpg' },
        { id: 3, username: 'xDivineSword', playername: 'xDivineSword', email: 'xdivinesword@example.com', profilePicture: '3.jpg' },
        { id: 4, username: 'lilWanton', playername: 'lilWanton', email: 'lilwanton@example.com', profilePicture: '4.jpg' },
        { id: 5, username: 'DarkStar', playername: 'DarkStar', email: 'darkstar@example.com', profilePicture: '5.jpg' },
        { id: 6, username: 'Nebula', playername: 'Nebula', email: 'nebula@example.com', profilePicture: '6.jpg' },
        { id: 7, username: 'VoidWalker', playername: 'VoidWalker', email: 'voidwalker@example.com', profilePicture: '7.jpg' },
        { id: 8, username: 'WindRider', playername: 'WindRider', email: 'windrider@example.com', profilePicture: '8.jpg' },
      ];

      // Map players to include profile picture paths
      const mappedPlayers = players.map((player) => ({
        ...player,
        profilePicture: profileImages[player.profilePicture || 'default.jpg'], // Map the profile picture
      }));

      setPlayerData(mappedPlayers); // Set the full player data
      setFilteredData(mappedPlayers); // Initialize the filtered data to include all players
    } catch (error) {
      console.error('Error fetching player data:', error);
    } finally {
      setLoading(false); // Stop loading spinner
    }
  };

  /**
   * Delete a player by ID using the backend API.
   * 
   * @param {string} playerId - The ID of the player to delete.
   */
  const handleDeletePlayer = async (playerId) => {
    try {
      setLoading(true); // Set loading to true during delete
      await api.delete(`/admin/users/deleteUser/${playerId}`); // DELETE request to API
      // Remove the deleted player from state
      setPlayerData((prevData) => prevData.filter((player) => player.id !== playerId));
      setFilteredData((prevData) => prevData.filter((player) => player.id !== playerId)); // Update filtered data as well
    } catch (error) {
      setError('Failed to delete player. Please try again.');
      console.error('Error deleting player:', error);
    } finally {
      setLoading(false); // Stop loading spinner
    }
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
          />
        )}
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
