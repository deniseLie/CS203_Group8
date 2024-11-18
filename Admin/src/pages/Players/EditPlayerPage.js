import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import {
  Box, Typography, TextField, Button, Stack, Dialog, DialogActions, DialogContent, DialogTitle
} from '@mui/material';
import Sidebar from '../../components/Sidebar';

const EditPlayerPage = () => {
  const location = useLocation();
  const navigate = useNavigate(); // Hook to navigate programmatically
  const { player, updatePlayer } = location.state || {}; // Get player data and updatePlayer function from state
  const [editedPlayer, setEditedPlayer] = useState(player); // Use player data to populate form
  const [openConfirmation, setOpenConfirmation] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedPlayer((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = () => {
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleCloseConfirmation = () => setOpenConfirmation(false);

  const handleConfirmSave = () => {
    // Call updatePlayer to update the player data in the parent component
    navigate("/players/dataset", { replace: true }); // Navigate back to the dataset page
    handleCloseConfirmation(); // Close the confirmation dialog
  };

  if (!editedPlayer) return <Typography>Loading...</Typography>; // Show loading if data is not ready

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <Typography variant="h4" sx={{ mb: 2 }}>Edit Player</Typography>

        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <TextField label="Player ID" value={editedPlayer.id} fullWidth required disabled />
            <TextField label="Username" name="username" value={editedPlayer.username} onChange={handleChange} fullWidth required />
            <TextField label="Email" name="email" value={editedPlayer.email} onChange={handleChange} fullWidth required />
            <TextField label="Playername" name="playername" value={editedPlayer.playername} onChange={handleChange} fullWidth required />
            <Button variant="contained" color="primary" onClick={handleSave}>Save Player</Button>
          </Stack>
        </Box>

        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>Confirm Changes</DialogTitle>
          <DialogContent>
            <Typography>Are you sure you want to save changes to this player?</Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseConfirmation} color="primary">Cancel</Button>
            <Button onClick={handleConfirmSave} color="primary">Confirm</Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Box>
  );
};

export default EditPlayerPage;
