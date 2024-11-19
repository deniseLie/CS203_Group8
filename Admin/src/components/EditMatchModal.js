import React, { useState } from 'react';
import { Box, Typography, Button, Select, MenuItem } from '@mui/material';

/**
 * EditMatchModal Component
 *
 * This component represents a modal used for editing a match. It allows the user
 * to select a winner for a specific match and save the updated information.
 *
 * @param {object} props - Component props.
 * @param {object} props.match - The match object containing details about the match.
 * @param {function} props.onClose - Callback function to close the modal.
 * @param {function} props.onSave - Callback function to save the updated match.
 */
const EditMatchModal = ({ match, onClose, onSave }) => {
    // State to store the selected winner
    const [selectedWinner, setSelectedWinner] = useState(match.winner || '');

    /**
     * Handles saving the updated match information.
     */
    const handleSave = () => {
        const updatedMatch = { ...match, winner: selectedWinner }; // Create a copy of the match with the updated winner
        onSave(updatedMatch); // Invoke the onSave callback with the updated match
    };

    return (
        <Box
            sx={{
                backgroundColor: '#ffffff', // Modal background color
                borderRadius: '8px', // Rounded corners for aesthetics
                padding: '20px', // Padding around the modal content
                width: '400px', // Fixed width for the modal
                boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)', // Subtle shadow for depth
            }}
        >
            {/* Modal Title */}
            <Typography variant="h6" sx={{ mb: 2 }}>
                Edit Match
            </Typography>

            {/* Label for the winner selection */}
            <Typography variant="body1">Select Winner:</Typography>

            {/* Dropdown to select the winner */}
            <Select
                value={selectedWinner} // Controlled component for the selected winner
                onChange={(e) => setSelectedWinner(e.target.value)} // Update state when selection changes
                fullWidth
                sx={{ mb: 2 }}
            >
                {/* Options for selecting Player 1 or Player 2 as the winner */}
                <MenuItem value={match.player1}>{match.player1}</MenuItem>
                <MenuItem value={match.player2}>{match.player2}</MenuItem>
            </Select>

            {/* Save and Cancel Buttons */}
            <Button
                variant="contained"
                onClick={handleSave} // Handle save action
                sx={{ mr: 1 }} // Add margin to the right for spacing
            >
                Save
            </Button>
            <Button
                variant="outlined"
                onClick={onClose} // Handle cancel action
            >
                Cancel
            </Button>
        </Box>
    );
};

export default EditMatchModal;
