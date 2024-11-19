import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField, Stack, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';

const ConfigureTournamentPage = () => {
  const [maxPlayers, setMaxPlayers] = useState(''); // Initial empty value
  const [openConfirmation, setOpenConfirmation] = useState(false); // State for confirmation dialog
  const [error, setError] = useState(''); // State for error handling
  const [success, setSuccess] = useState(''); // State for success message
  const [hasInteracted, setHasInteracted] = useState(false); // Track if the user has interacted with the input

  // Fetch the current tournament configuration
  const fetchTournamentSize = async () => {
    try {
      const response = await fetch('/admin/tournaments/configure');
      if (!response.ok) {
        throw new Error(`Failed to fetch: ${response.statusText}`);
      }
      const data = await response.json();
      setMaxPlayers(data.maxPlayers); // Update state with the fetched data
    } catch (error) {
      console.error('Error fetching tournament size:', error);
      setError('Failed to load tournament size.');
    }
  };

  // Update the tournament configuration
  const updateTournamentSize = async (newSize) => {
    try {
      const response = await fetch('/admin/tournaments/configure', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ maxPlayers: newSize }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update tournament size.');
      }

      setSuccess('Tournament configuration updated successfully!');
    } catch (error) {
      setError('Error updating tournament size. Try again later.');
      console.error('Error updating tournament size:', error);
    }
  };

  // Fetch current tournament size when component mounts
  useEffect(() => {
    fetchTournamentSize();
  }, []);

  const handleSave = () => {
    const value = Number(maxPlayers);
    if (isNaN(value) || value % 2 !== 0 || value <= 0) {
      setError('Please enter a valid even number greater than 0.');
      return;
    }
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleConfirmSave = async () => {
    setError(''); // Clear existing errors
    await updateTournamentSize(Number(maxPlayers));
    setOpenConfirmation(false); // Close the dialog after saving
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  const handleMaxPlayersChange = (e) => {
    const value = e.target.value;
    if (/^\d*$/.test(value)) { // Ensure only digits are allowed
      setMaxPlayers(value);
      setError(''); // Clear error if the value is valid
      setHasInteracted(true); // Mark that the user has interacted with the input
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
              value={maxPlayers}
              onChange={handleMaxPlayersChange}
              fullWidth
              required
              helperText="Enter an even number"
              error={Boolean(error) && hasInteracted} // Show error only after interaction
            />

            {error && hasInteracted && (
              <Typography color="error" sx={{ mt: 1 }}>
                {error}
              </Typography>
            )}

            {success && (
              <Typography color="primary" sx={{ mt: 1 }}>
                {success}
              </Typography>
            )}

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
