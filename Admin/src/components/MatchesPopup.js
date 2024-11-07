import React, { useState } from 'react';
import { Box, Typography, Button, Stack } from '@mui/material';
import EditMatchModal from './EditMatchModal';

const MatchesPopup = ({ tournament, onClose, onModify }) => {
    const [editMatch, setEditMatch] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);

    const createBracket = (rounds) => {
        if (!rounds || rounds.length === 0) return null; // Handle no rounds case
        return rounds.map((round) => (
            <Box key={round.roundNumber} sx={{ mb: 2 }}>
                <Typography variant="h6">Round {round.roundNumber}</Typography>
                {round.matches.map((match) => (
                    <Stack key={match.id} direction="row" justifyContent="space-between" sx={{ mb: 1 }}>
                        <Typography>
                            {match.player1} vs {match.player2}
                            {match.winner && <span> (Winner: {match.winner})</span>}
                        </Typography>
                        <Button variant="outlined" onClick={() => handleEditClick(match)}>Edit</Button>
                    </Stack>
                ))}
            </Box>
        ));
    };

    const handleEditClick = (match) => {
        setEditMatch(match);
        setShowEditModal(true);
    };

    const handleSaveMatch = (updatedMatch) => {
        const updatedRounds = tournament.rounds.map((round) => ({
            ...round,
            matches: round.matches.map((match) => 
                match.id === updatedMatch.id ? updatedMatch : match
            ),
        }));

        const updatedTournament = {
            ...tournament,
            rounds: updatedRounds,
        };

        onModify(updatedTournament); // Update parent component state
        setShowEditModal(false); // Close the modal after saving
    };

    return (
        <Box sx={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000,
        }}>
            <Box sx={{
                backgroundColor: '#ffffff',
                borderRadius: '8px',
                padding: '20px',
                width: '80%',
                maxWidth: '800px',
                boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.2)',
            }}>
                <Typography variant="h5" sx={{ mb: 2 }}>Tournament Matches</Typography>
                {createBracket(tournament.rounds)}

                {showEditModal && editMatch && (
                    <EditMatchModal 
                        match={editMatch} 
                        onClose={() => setShowEditModal(false)} 
                        onSave={handleSaveMatch} // Save function here
                    />
                )}
                <Button variant="contained" onClick={onClose} sx={{ mt: 2 }}>Close</Button>
            </Box>
        </Box>
    );
};

export default MatchesPopup;
