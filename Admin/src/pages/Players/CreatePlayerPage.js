import React, { useState } from 'react';
import { Box, Typography, TextField, Button, Stack, FormControl, InputLabel, Select, MenuItem, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import axios from 'axios';

const CreatePlayerPage = () => {
  const [playerId, setPlayerId] = useState('');
  const [username, setUsername] = useState('');
  const [playername, setPlayername] = useState(''); // New state for playername
  const [email, setEmail] = useState('');
  const [authProvider, setAuthProvider] = useState('LOCAL'); // Default value
  const [openConfirmation, setOpenConfirmation] = useState(false); // State for confirmation dialog
  const [error, setError] = useState(''); // State for error handling
  const [success, setSuccess] = useState(''); // State for success message

  const handleSave = () => {
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleConfirmSave = async () => {
    const playerData = {
      playerId,
      username,
      playername,  // Include playername in data
      email,
      authProvider,
    };

    try {
      const response = await axios.post('/admin/user/createUser', playerData);
      if (response.status === 201) {
        setSuccess('Player created successfully!');
        resetForm(); // Clear the form fields after saving
      }
    } catch (error) {
      setError('Failed to create player. Please try again.');
      console.error('Error creating player:', error);
    } finally {
      setOpenConfirmation(false); // Close the dialog after saving
    }
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  const resetForm = () => {
    setPlayerId('');
    setUsername('');
    setPlayername(''); // Reset playername field
    setEmail('');
    setAuthProvider('LOCAL');
    setError('');
    setSuccess('');
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
          Create Player
        </Typography>

        {/* Player Details Form */}
        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <TextField
              label="Player ID"
              value={playerId}
              onChange={(e) => setPlayerId(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Player Name"
              value={playername}
              onChange={(e) => setPlayername(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              fullWidth
              required
            />
            <FormControl fullWidth required>
              <InputLabel>Auth Provider</InputLabel>
              <Select
                value={authProvider}
                onChange={(e) => setAuthProvider(e.target.value)}
              >
                <MenuItem value="LOCAL">Local</MenuItem>
                <MenuItem value="GOOGLE">Google</MenuItem>
                {/* Add more auth providers as needed */}
              </Select>
            </FormControl>

            {error && (
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
              Save Player
            </Button>
          </Stack>
        </Box>

        {/* Confirmation Dialog */}
        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>Confirm Player Creation</DialogTitle>
          <DialogContent>
            <Typography>
              Are you sure you want to create this player?
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
      </Box>
    </Box>
  );
};

export default CreatePlayerPage;
