import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom'; // Hooks for routing and navigation
import {
  Box,
  Typography,
  TextField,
  Button,
  Stack,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Alert,
} from '@mui/material';
import api from '../../services/api'; // Custom API service for handling requests
import Sidebar from '../../components/Sidebar';

/**
 * EditPlayerPage Component
 *
 * This component is used to edit an existing player's details.
 * It fetches the player's data using their ID, allows modifications, and updates the details via an API call.
 */
const EditPlayerPage = () => {
  const location = useLocation(); // Hook to get navigation state (e.g., playerId)
  const navigate = useNavigate(); // Hook for programmatic navigation

  const { playerId } = location.state || {}; // Extract player ID from navigation state
  const [editedPlayer, setEditedPlayer] = useState(null); // State to store player data for the form
  const [loading, setLoading] = useState(true); // State to manage loading state
  const [error, setError] = useState(''); // State to handle errors
  const [success, setSuccess] = useState(''); // State to handle success messages
  const [openConfirmation, setOpenConfirmation] = useState(false); // State for confirmation dialog

  /**
   * Fetch player data by their ID from the API.
   * 
   * @param {string} id - The ID of the player to fetch.
   */
  const fetchPlayer = async (id) => {
    try {
      const response = await api.get(`/admin/users/${id}`); // API call to fetch player details
      if (!response.ok) {
        throw new Error(`Failed to fetch player: ${response.statusText}`);
      }
      const data = await response.json();
      setEditedPlayer(data); // Populate form with fetched data
    } catch (err) {
      setError(err.message);
      console.error('Error fetching player:', err);
    } finally {
      setLoading(false); // Stop the loading spinner
    }
  };

  /**
   * Update player details by making a PUT request to the API.
   * 
   * @param {string} id - The ID of the player to update.
   * @param {object} updatedData - The updated player data.
   */
  const updatePlayer = async (id, updatedData) => {
    try {
      const response = await fetch(`/admin/users/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData), // Send updated player data as JSON
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update player.');
      }

      setSuccess('Player updated successfully!'); // Show success message
    } catch (err) {
      setError(err.message); // Show error message
      console.error('Error updating player:', err);
    }
  };

  /**
   * Fetch player data on component mount if playerId is available.
   */
  useEffect(() => {
    if (playerId) {
      fetchPlayer(playerId); // Fetch player details
    } else {
      setError('Player ID is missing.');
      setLoading(false);
    }
  }, [playerId]);

  /**
   * Handle input changes and update the edited player state.
   * 
   * @param {object} e - Event object from input field change.
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedPlayer((prev) => ({ ...prev, [name]: value })); // Update state with new value
    setError(''); // Clear any previous error
    setSuccess(''); // Clear any previous success message
  };

  /**
   * Open the confirmation dialog when Save button is clicked.
   */
  const handleSave = () => {
    setOpenConfirmation(true);
  };

  /**
   * Close the confirmation dialog.
   */
  const handleCloseConfirmation = () => setOpenConfirmation(false);

  /**
   * Confirm changes and update the player details via API.
   */
  const handleConfirmSave = async () => {
    if (editedPlayer) {
      await updatePlayer(playerId, editedPlayer); // Make API call to update player
    }
    setOpenConfirmation(false); // Close the confirmation dialog
  };

  /**
   * Navigate back to the player dataset page.
   */
  const handleBack = () => {
    navigate('/players/dataset', { replace: true });
  };

  // Show loading message if data is being fetched
  if (loading) return <Typography>Loading player data...</Typography>;

  // Show error message if an error occurs
  if (error) return <Alert severity="error">{error}</Alert>;

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar for navigation */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <Typography variant="h4" sx={{ mb: 2 }}>Edit Player</Typography>

        {/* Form to edit player details */}
        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <TextField label="Player ID" value={editedPlayer.id} fullWidth required disabled />
            <TextField
              label="Username"
              name="username"
              value={editedPlayer.username || ''}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Email"
              name="email"
              value={editedPlayer.email || ''}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Playername"
              name="playername"
              value={editedPlayer.playername || ''}
              onChange={handleChange}
              fullWidth
              required
            />
            <Button variant="contained" color="primary" onClick={handleSave}>
              Save Player
            </Button>
            <Button variant="outlined" color="secondary" onClick={handleBack}>
              Back to Players
            </Button>
            {success && (
              <Typography color="primary" sx={{ mt: 1 }}>
                {success}
              </Typography>
            )}
          </Stack>
        </Box>

        {/* Confirmation Dialog */}
        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>Confirm Changes</DialogTitle>
          <DialogContent>
            <Typography>Are you sure you want to save changes to this player?</Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseConfirmation} color="primary">
              Cancel
            </Button>
            <Button onClick={handleConfirmSave} color="primary">
              Confirm
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Box>
  );
};

export default EditPlayerPage;
