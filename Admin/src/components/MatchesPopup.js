import React, { useState } from 'react';
import { Box, Typography, Button, Stack } from '@mui/material';
import EditMatchModal from './EditMatchModal';

/**
 * MatchesPopup Component
 *
 * This component displays all matches for a specific tournament in a popup.
 * It supports:
 * - Viewing matches by round.
 * - Editing a match's details via a modal.
 * - Updating matches in the parent component state.
 *
 * @param {object} tournament - The tournament object containing match and round data.
 * @param {function} onClose - Function to close the popup.
 * @param {function} onModify - Callback to handle modifications to the tournament data.
 */
const MatchesPopup = ({ tournament, onClose, onModify }) => {
    // State to track the match being edited
    const [editMatch, setEditMatch] = useState(null);
    // State to control the visibility of the edit modal
    const [showEditModal, setShowEditModal] = useState(false);

    /**
     * Creates the match bracket UI by iterating through the tournament rounds.
     *
     * @param {object[]} rounds - Array of round objects, each containing match data.
     * @returns {JSX.Element[]} - A list of JSX elements displaying matches grouped by round.
     */
    const createBracket = (rounds) => {
        if (!rounds || rounds.length === 0) return null; // Handle case where no rounds exist
        return rounds.map((round) => (
            <Box key={round.roundNumber} sx={{ mb: 2 }}>
                <Typography variant="h6">Round {round.roundNumber}</Typography>
                {round.matches.map((match) => (
                    <Stack key={match.id} direction="row" justifyContent="space-between" sx={{ mb: 1 }}>
                        <Typography>
                            {match.player1} vs {match.player2}
                            {match.winner && <span> (Winner: {match.winner})</span>}
                        </Typography>
                        <Button variant="outlined" onClick={() => handleEditClick(match)}>
                            Edit
                        </Button>
                    </Stack>
                ))}
            </Box>
        ));
    };

    /**
     * Handles the click event for editing a match.
     *
     * @param {object} match - The match object to edit.
     */
    const handleEditClick = (match) => {
        setEditMatch(match); // Set the match to be edited
        setShowEditModal(true); // Show the edit modal
    };

    /**
     * Handles saving the updated match details.
     *
     * @param {object} updatedMatch - The match object with updated details.
     */
    const handleSaveMatch = (updatedMatch) => {
        // Update the tournament's rounds with the modified match
        const updatedRounds = tournament.rounds.map((round) => ({
            ...round,
            matches: round.matches.map((match) =>
                match.id === updatedMatch.id ? updatedMatch : match // Replace the updated match
            ),
        }));

        // Create a new tournament object with updated rounds
        const updatedTournament = {
            ...tournament,
            rounds: updatedRounds,
        };

        onModify(updatedTournament); // Call the parent's modify callback with the updated tournament
        setShowEditModal(false); // Close the edit modal
    };

    return (
        <Box
            sx={{
                position: 'fixed', // Fix the popup to the viewport
                top: 0,
                left: 0,
                right: 0,
                bottom: 0,
                backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent background
                display: 'flex', // Center the popup
                justifyContent: 'center',
                alignItems: 'center',
                zIndex: 1000, // Ensure it appears above other components
            }}
        >
            <Box
                sx={{
                    backgroundColor: '#ffffff', // White background
                    borderRadius: '8px', // Rounded corners
                    padding: '20px',
                    width: '80%',
                    maxWidth: '800px', // Max width for responsiveness
                    boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)', // Subtle shadow for depth
                }}
            >
                <Typography variant="h5" sx={{ mb: 2 }}>
                    Tournament Matches
                </Typography>

                {/* Render the match bracket */}
                {createBracket(tournament.rounds)}

                {/* Edit Match Modal */}
                {showEditModal && editMatch && (
                    <EditMatchModal
                        match={editMatch} // Pass the selected match to the modal
                        onClose={() => setShowEditModal(false)} // Close the modal
                        onSave={handleSaveMatch} // Save the updated match
                    />
                )}

                {/* Close Button */}
                <Button variant="contained" onClick={onClose} sx={{ mt: 2 }}>
                    Close
                </Button>
            </Box>
        </Box>
    );
};

export default MatchesPopup;
