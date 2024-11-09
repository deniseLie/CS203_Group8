import React, { useState } from 'react';
import { Box, Typography, Button, Select, MenuItem } from '@mui/material';

const EditMatchModal = ({ match, onClose, onSave }) => {
    const [selectedWinner, setSelectedWinner] = useState(match.winner || '');

    const handleSave = () => {
        const updatedMatch = { ...match, winner: selectedWinner }; // Update match with selected winner
        onSave(updatedMatch); // Save the updated match
    };

    return (
        <Box sx={{
            backgroundColor: '#ffffff',
            borderRadius: '8px',
            padding: '20px',
            width: '400px',
            boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)',
        }}>
            <Typography variant="h6" sx={{ mb: 2 }}>Edit Match</Typography>
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
            <Button variant="contained" onClick={handleSave} sx={{ mr: 1 }}>Save</Button>
            <Button variant="outlined" onClick={onClose}>Cancel</Button>
        </Box>
    );
};

export default EditMatchModal;
