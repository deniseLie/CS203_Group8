import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField, Stack, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import Sidebar from '../../components/Sidebar'; // Sidebar component for navigation
import TopBar from '../../components/TopBar'; // Top bar for page title or actions
import api from '../../services/api';

/**
 * ConfigureTournamentPage Component
 *
 * This component allows administrators to configure tournament settings, specifically the maximum number of players.
 * It fetches the current configuration, validates user input, and updates the settings via an API.
 */
const ConfigureTournamentPage = () => {
  // State variables
  const [maxPlayers, setMaxPlayers] = useState(''); // Stores the maximum players value
  const [openConfirmation, setOpenConfirmation] = useState(false); // Controls the confirmation dialog visibility
  const [error, setError] = useState(''); // Stores error messages
  const [success, setSuccess] = useState(''); // Stores success messages
  const [hasInteracted, setHasInteracted] = useState(false); // Tracks if the user has interacted with the input field

  /**
   * Fetch the current tournament configuration from the server.
   */
  const fetchTournamentSize = async () => {
    try {
      const response = await api.get('/admin/tournaments/getTournamentSize'); // API call to fetch configuration
      if (!response.ok) {
        throw new Error(`Failed to fetch: ${response.statusText}`);
      }
      const data = await response.json();
      setMaxPlayers(data.maxPlayers); // Set the current maximum players value
    } catch (error) {
      console.error('Error fetching tournament size:', error);
      setError('Failed to load tournament size.'); // Display an error message
    }
  };

  /**
   * Update the tournament configuration by sending the new maximum players value to the server.
   * 
   * @param {number} newSize - The new maximum number of players.
   */
  const updateTournamentSize = async (newSize) => {
    try {
      const response = await api.post('/admin/tournaments/updateTournamentSize', {
        method: 'POST', // HTTP POST method for updating data
        headers: {
          'Content-Type': 'application/json', // Indicate JSON payload
        },
        body: JSON.stringify({ maxPlayers: newSize }), // Send the new size as a JSON object
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update tournament size.');
      }

      setSuccess('Tournament configuration updated successfully!'); // Display success message
    } catch (error) {
      setError('Error updating tournament size. Try again later.'); // Display error message
      console.error('Error updating tournament size:', error);
    }
  };

  /**
   * Fetch the current tournament configuration when the component mounts.
   */
  useEffect(() => {
    fetchTournamentSize();
  }, []);

  /**
   * Validate and open the confirmation dialog when the Save button is clicked.
   */
  const handleSave = () => {
    const value = Number(maxPlayers); // Convert input to a number
    if (isNaN(value) || value % 2 !== 0 || value <= 0) {
      setError('Please enter a valid even number greater than 0.'); // Validate input
      return;
    }
    setOpenConfirmation(true); // Open confirmation dialog
  };

  /**
   * Confirm and save the new tournament configuration.
   */
  const handleConfirmSave = async () => {
    setError(''); // Clear any existing errors
    await updateTournamentSize(Number(maxPlayers)); // Update the configuration
    setOpenConfirmation(false); // Close the confirmation dialog
  };

  /**
   * Close the confirmation dialog without saving.
   */
  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  /**
   * Handle changes to the max players input field.
   * Validates that only numeric input is allowed.
   * 
   * @param {object} e - Event object from the input field.
   */
  const handleMaxPlayersChange = (e) => {
    const value = e.target.value; // Get input value
    if (/^\d*$/.test(value)) { // Allow only numeric input
      setMaxPlayers(value); // Update state
      setError(''); // Clear any previous errors
      setHasInteracted(true); // Track that the user has interacted
    }
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
        <Typography variant="h4" sx={{ mb: 3 }}>
          Configure Tournament Settings
        </Typography>

        {/* Tournament Configuration Form */}
        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <Typography variant="h6">Max Players</Typography>
            <TextField
              type="number"
              value={maxPlayers} // Bind input to state
              onChange={handleMaxPlayersChange} // Handle input changes
              fullWidth
              required
              helperText="Enter an even number"
              error={Boolean(error) && hasInteracted} // Show error if it exists and the user has interacted
            />

            {/* Display Error Message */}
            {error && hasInteracted && (
              <Typography color="error" sx={{ mt: 1 }}>
                {error}
              </Typography>
            )}

            {/* Display Success Message */}
            {success && (
              <Typography color="primary" sx={{ mt: 1 }}>
                {success}
              </Typography>
            )}

            {/* Save Button */}
            <Button variant="contained" color="primary" onClick={handleSave}>
              Save Configuration
            </Button>
          </Stack>
        </Box>

        {/* Confirmation Dialog */}
        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>Confirm Changes</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Changing the maximum number of players will affect every tournament created in the future. Are you sure you want to proceed?
            </DialogContentText>
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

export default ConfigureTournamentPage;
