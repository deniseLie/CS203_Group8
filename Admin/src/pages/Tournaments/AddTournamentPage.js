import React, { useState } from 'react';
import { Box, Typography, Button, MenuItem, Stack, FormControl, Select, Dialog, DialogActions, DialogContent, DialogTitle, Checkbox, ListItemText, Autocomplete, TextField } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';

const playerList = ['Player A', 'Player B', 'Player C', 'Player D']; // Example player list

const AddTournamentPage = () => {
  const [startDate, setStartDate] = useState(new Date().toISOString().split("T")[0]); // Default to today's date
  const [maxPlayers, setMaxPlayers] = useState(8); // Default to 8 players
  const [selectedPlayers, setSelectedPlayers] = useState([]);
  const [openConfirmation, setOpenConfirmation] = useState(false); // State for confirmation dialog
  const [openSuccess, setOpenSuccess] = useState(false); // State for success dialog

  const handlePlayerChange = (event, value) => {
    setSelectedPlayers(value); // Update selected players based on input
  };

  const handleSave = () => {
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleConfirmSave = () => {
    const tournamentConfig = {
      startDate,
      maxPlayers,
      players: selectedPlayers,
    };
    console.log('New Tournament Config:', tournamentConfig);
    // Here you would typically send this data to your backend or update your state management

    // Clear the data
    setStartDate(new Date().toISOString().split("T")[0]); // Reset to today's date
    setMaxPlayers(8); // Reset to default
    setSelectedPlayers([]); // Clear selected players

    setOpenConfirmation(false); // Close the confirmation dialog
    setOpenSuccess(true); // Open the success dialog
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  const handleCloseSuccess = () => {
    setOpenSuccess(false);
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
        <Typography variant="h4" sx={{ mb: 2 }}>
          Add Tournament
        </Typography>

        {/* Tournament Configuration Form */}
        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <Box>
              <Typography variant="subtitle1">Start Date</Typography>
              <TextField
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                required
                fullWidth
                sx={{ mb: 2 }}
              />
            </Box>

            <FormControl fullWidth required>
              <Typography variant="subtitle1">Max Players</Typography>
              <Select
                value={maxPlayers}
                onChange={(e) => setMaxPlayers(Number(e.target.value))}
              >
                {[4, 8, 16, 32].map((num) => (
                  <MenuItem key={num} value={num}>{num}</MenuItem>
                ))}
              </Select>
            </FormControl>

            {/* Player Selection with Autocomplete Functionality */}
            <Typography variant="subtitle1">Select Players</Typography>
            <Autocomplete
              multiple
              options={playerList}
              value={selectedPlayers}
              onChange={handlePlayerChange}
              renderInput={(params) => (
                <TextField {...params} variant="outlined" placeholder="Type to search..." fullWidth />
              )}
            />

            <Button variant="contained" color="primary" onClick={handleSave}>
              Save Tournament
            </Button>
          </Stack>
        </Box>

        {/* Confirmation Dialog */}
        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>Confirm Tournament Creation</DialogTitle>
          <DialogContent>
            <Typography>
              Are you sure you want to create this tournament?
            </Typography>
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

        {/* Success Dialog */}
        <Dialog open={openSuccess} onClose={handleCloseSuccess}>
          <DialogTitle>Tournament Created Successfully!</DialogTitle>
          <DialogContent>
            <Typography>
              The tournament has been successfully created.
            </Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseSuccess} color="primary">
              Close
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Box>
  );
};

export default AddTournamentPage;
