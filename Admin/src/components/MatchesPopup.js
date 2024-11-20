import React, { useState } from 'react';
import { Box, Typography, Stack, Button } from '@mui/material';
import EditMatchModal from './EditMatchModal';

/**
 * MatchesPopup Component
 *
 * Displays matches for a tournament and allows editing via `EditMatchModal`.
 */
const MatchesPopup = ({ tournament, onClose, onModify }) => {
  const [editMatch, setEditMatch] = useState(null); // Match being edited

  /**
   * Handle saving the updated match details.
   *
   * @param {object} updatedMatch - The updated match object.
   */
  const handleSaveMatch = (updatedMatch) => {
    const updatedRounds = tournament.rounds.map((round) => ({
      ...round,
      matches: round.matches.map((match) =>
        match.id === updatedMatch.id ? updatedMatch : match
      ),
    }));

    const updatedTournament = { ...tournament, rounds: updatedRounds };
    onModify(updatedTournament);
    setEditMatch(null); // Close the modal
  };

  return (
    <Box
      sx={{
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <Box
        sx={{
          backgroundColor: '#ffffff',
          borderRadius: '8px',
          padding: '20px',
          width: '80%',
          maxWidth: '800px',
        }}
      >
        <Typography variant="h5" sx={{ mb: 2 }}>
          Matches for Tournament: {tournament.tournamentId}
        </Typography>

        {tournament.rounds.map((round) => (
          <Box key={round.roundNumber} sx={{ mb: 2 }}>
            <Typography variant="h6">Round {round.roundNumber}</Typography>
            {round.matches.map((match) => (
              <Stack
                key={match.id}
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                sx={{ mb: 1 }}
              >
                <Typography>
                  {match.player1} vs {match.player2} {match.winner && ` (Winner: ${match.winner})`}
                </Typography>
                <Button variant="outlined" onClick={() => setEditMatch(match)}>
                  Edit
                </Button>
              </Stack>
            ))}
          </Box>
        ))}

        {editMatch && (
          <EditMatchModal
            match={editMatch}
            onClose={() => setEditMatch(null)}
            onSave={handleSaveMatch}
          />
        )}

        <Button variant="contained" onClick={onClose} sx={{ mt: 2 }}>
          Close
        </Button>
      </Box>
    </Box>
  );
};

export default MatchesPopup;
