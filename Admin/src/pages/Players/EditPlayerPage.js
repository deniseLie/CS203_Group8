import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  Box, Typography, TextField, Button, Stack, FormControl, InputLabel, Select, MenuItem, Dialog, DialogActions, DialogContent, DialogTitle
} from '@mui/material';
import Sidebar from '../../components/Sidebar';

const EditPlayerPage = () => {
  const { playerId } = useParams(); // Get playerId from URL
  const [player, setPlayer] = useState(null); // State to hold player data
  const [openConfirmation, setOpenConfirmation] = useState(false); // For dialog open state
  const [confirmationAction, setConfirmationAction] = useState(""); // Track whether confirming save or delete

  // Dummy player data for testing layout
  const dummyPlayer = {
    id: "123",
    username: "testUser",
    playername: "Test Player",
    email: "test@example.com",
    authProvider: "LOCAL"
  };

  // Fetch player data based on playerId
  useEffect(() => {
    const fetchPlayer = async () => {
      try {
        const response = await fetch(`/api/players/${playerId}`); // Replace with your actual API endpoint
        const data = await response.json();
        setPlayer(data);
      } catch (error) {
        console.error("Failed to fetch player data:", error);
        // Use dummy data if fetching fails
        setPlayer(dummyPlayer);
      }
    };

    if (playerId) {
      fetchPlayer();
    } else {
      // Set dummy data if no playerId is provided
      setPlayer(dummyPlayer);
    }
  }, [playerId]);

  // Handle state changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setPlayer((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = () => {
    setConfirmationAction("save"); // Set action type to save
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleDelete = () => {
    setConfirmationAction("delete"); // Set action type to delete
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleCloseConfirmation = () => setOpenConfirmation(false);

  const handleConfirmAction = async () => {
    if (confirmationAction === "save") {
      // Save player changes logic here
      try {
        await fetch(`/api/players/${player.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(player),
        });
        console.log("Player saved successfully!");
      } catch (error) {
        console.error("Error saving player:", error);
      }
    } else if (confirmationAction === "delete") {
      // Delete player logic here
      try {
        await fetch(`/api/players/${player.id}`, {
          method: 'DELETE',
        });
        console.log("Player deleted successfully!");
        // Optionally, redirect to another page after deletion
      } catch (error) {
        console.error("Error deleting player:", error);
      }
    }
    handleCloseConfirmation();
  };

  if (!player) return <Typography>Loading...</Typography>; // Show loading if data is not ready

  return (
    <Box sx={{ display: 'flex' }}>
        
      {/* Sidebar */}
      <Sidebar />

      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <Typography variant="h4" sx={{ mb: 2 }}>Edit Player</Typography>

        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <TextField label="Player ID" value={player.id} fullWidth required disabled />
            <TextField label="Username" name="username" value={player.username} onChange={handleChange} fullWidth required />
            <TextField label="Email" name="email" value={player.email} onChange={handleChange} fullWidth required />
            <TextField label="Playername" name="playername" value={player.playername} onChange={handleChange} fullWidth required />
            <FormControl fullWidth required>
              <InputLabel>Auth Provider</InputLabel>
              <Select name="authProvider" value={player.authProvider} onChange={handleChange}>
                <MenuItem value="LOCAL">Local</MenuItem>
                <MenuItem value="GOOGLE">Google</MenuItem>
              </Select>
            </FormControl>
            <Stack direction="row" spacing={2}>
              <Button variant="contained" color="primary" onClick={handleSave}>Save Player</Button>
              <Button variant="outlined" color="secondary" onClick={handleDelete}>Delete User</Button>
            </Stack>
          </Stack>
        </Box>

        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>{confirmationAction === "delete" ? "Confirm Deletion" : "Confirm Changes"}</DialogTitle>
          <DialogContent>
            <Typography>
              {confirmationAction === "delete"
                ? "Are you sure you want to delete this player?"
                : "Are you sure you want to save changes to this player?"}
            </Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseConfirmation} color="primary">Cancel</Button>
            <Button onClick={handleConfirmAction} color="primary">
              {confirmationAction === "delete" ? "Delete" : "Confirm"}
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Box>
  );
};

export default EditPlayerPage;
