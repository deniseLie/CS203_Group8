import React, { useState } from 'react';
import { Box, Typography, Button, Select, MenuItem, CircularProgress, Alert } from '@mui/material';
import api from '../services/api';

/**
 * EditMatchModal Component
 *
 * Simulates editing a match by selecting a winner and saving the updated information.
 *
 * @param {object} props - Component props.
 * @param {object} props.match - The match object containing details about the match.
 * @param {function} props.onClose - Callback function to close the modal.
 * @param {function} props.onSave - Callback function to refresh parent state with the updated match.
 */
const EditMatchModal = ({ match, onClose, onSave }) => {
  // Initialize selectedWinner with the match's winner name
  const [selectedWinner, setSelectedWinner] = useState(match.winner || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Handles saving the updated match information.
   */
  const handleSave = async () => {
    setLoading(true);
    setError(null);
    try {
      // API call to create or update a round
      const response = await api.post('/admin/tournaments/round', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          tournamentId: match.tournamentId,
          firstPlayerId: match.player1Id,
          secondPlayerId: match.player2Id,
          winnerPlayerId: selectedWinner,
          roundNumber: match.roundNumber,
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to update match.');
      }

      const result = await response.json();
      console.log('Match updated:', result.message);

      // Notify parent to refresh state with updated match
      const updatedMatch = { ...match, winner: selectedWinner };
      onSave(updatedMatch);
    } catch (err) {
      setError(err.message || 'An unexpected error occurred.');
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <Box
      sx={{
        backgroundColor: '#ffffff',
        borderRadius: '8px',
        padding: '20px',
        width: '400px',
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)',
      }}
    >
      <Typography variant="h6" sx={{ mb: 2 }}>
        Edit Match
      </Typography>

      <Typography variant="body1">Select Winner:</Typography>
      <Select
        value={selectedWinner}
        onChange={(e) => setSelectedWinner(e.target.value)}
        fullWidth
        sx={{ mb: 2 }}
      >
        <MenuItem value={match.player1}>{match.player1}</MenuItem>
        <MenuItem value={match.player2}>{match.player2}</MenuItem>
      </Select>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Button
        variant="contained"
        onClick={handleSave}
        sx={{ mr: 1 }}
        disabled={loading || !selectedWinner}
      >
        {loading ? <CircularProgress size={24} /> : 'Save'}
      </Button>
      <Button variant="outlined" onClick={onClose}>
        Cancel
      </Button>
    </Box>
  );
};

export default EditMatchModal;
