import React, { useState } from 'react';
import { Box, Typography, Button, MenuItem, Stack, Select, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';

const ConfigureTournamentPage = () => {
  const [maxPlayers, setMaxPlayers] = useState(8); // Default to 8 players
  const [openConfirmation, setOpenConfirmation] = useState(false); // State for confirmation dialog

  const handleSave = () => {
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleConfirmSave = () => {
    const tournamentConfig = {
      maxPlayers,
    };
    console.log('Tournament Config:', tournamentConfig);
    // Here you would typically send this data to your backend or update your state management

    // Close the dialog after saving
    setOpenConfirmation(false);
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
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
            <Select
              value={maxPlayers}
              onChange={(e) => setMaxPlayers(Number(e.target.value))}
              fullWidth
              required
            >
              {[4, 8, 16, 32].map((num) => (
                <MenuItem key={num} value={num}>{num}</MenuItem>
              ))}
            </Select>
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
