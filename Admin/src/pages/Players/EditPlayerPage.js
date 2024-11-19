import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
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
import Sidebar from '../../components/Sidebar';

const EditPlayerPage = () => {
  const location = useLocation();
  const navigate = useNavigate(); // Hook to navigate programmatically
  const { playerId } = location.state || {}; // Get player ID passed via navigation state
  const [editedPlayer, setEditedPlayer] = useState(null); // Player data for the form
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [openConfirmation, setOpenConfirmation] = useState(false);

  // Fetch player data by ID
  const fetchPlayer = async (id) => {
    try {
      const response = await fetch(`/admin/users/${id}`);
      if (!response.ok) {
        throw new Error(`Failed to fetch player: ${response.statusText}`);
      }
      const data = await response.json();
      setEditedPlayer(data);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching player:', err);
    } finally {
      setLoading(false);
    }
  };

  // Update player data
  const updatePlayer = async (id, updatedData) => {
    try {
      const response = await fetch(`/admin/users/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update player.');
      }

      setSuccess('Player updated successfully!');
    } catch (err) {
      setError(err.message);
      console.error('Error updating player:', err);
    }
  };

  useEffect(() => {
    if (playerId) {
      fetchPlayer(playerId);
    } else {
      setError('Player ID is missing.');
      setLoading(false);
    }
  }, [playerId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedPlayer((prev) => ({ ...prev, [name]: value }));
    setError(''); // Clear previous errors on input change
    setSuccess(''); // Clear previous success messages on input change
  };

  const handleSave = () => {
    setOpenConfirmation(true); // Open confirmation dialog
  };

  const handleCloseConfirmation = () => setOpenConfirmation(false);

  const handleConfirmSave = async () => {
    if (editedPlayer) {
      await updatePlayer(playerId, editedPlayer);
    }
    setOpenConfirmation(false); // Close the confirmation dialog
  };

  const handleBack = () => {
    navigate('/players/dataset', { replace: true });
  };

  if (loading) return <Typography>Loading player data...</Typography>;
  if (error) return <Alert severity="error">{error}</Alert>;

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <Typography variant="h4" sx={{ mb: 2 }}>Edit Player</Typography>

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
