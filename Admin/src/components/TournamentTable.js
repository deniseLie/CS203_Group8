import React, { useState } from 'react';
import { Box, Typography, Button, Stack, Avatar, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import MatchesPopup from './MatchesPopup';

const TournamentTable = ({ data }) => {
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [showPopup, setShowPopup] = useState(false);

  const handleTournamentClick = (tournament) => {
    setSelectedTournament(tournament); // Set the selected tournament
    setShowPopup(true); // Show the popup
  };

  const handleClosePopup = () => {
    setShowPopup(false); // Close the popup
  };

  const handleModifyMatch = (matchId) => {
    console.log(`Modify match with ID: ${matchId}`);
    // Logic for modifying match details can go here
  };

  return (
    <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Tournament Id</TableCell>
            <TableCell>Round No.</TableCell>
            <TableCell>Matches Completed</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Players</TableCell>
            <TableCell>Start Date-Time</TableCell>
            <TableCell>End Date-Time</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((tournament, index) => (
            <TableRow key={index}>
              <TableCell>
                <Button onClick={() => handleTournamentClick(tournament)}>{tournament.id}</Button>
              </TableCell>
              <TableCell>{tournament.round}</TableCell>
              <TableCell>{tournament.matchesCompleted}</TableCell>
              <TableCell>
                <Button size="small" variant="contained" color={tournament.status === "Ongoing" ? "warning" : "success"}>
                  {tournament.status}
                </Button>
              </TableCell>
              <TableCell>
                <Stack direction="row" spacing={1}>
                  {tournament.players.map((_, i) => (
                    <Avatar key={i} src={`/profile${i + 1}.jpg`} sx={{ width: 24, height: 24 }} />
                  ))}
                  <Typography>+{tournament.players.length}</Typography>
                </Stack>
              </TableCell>
              <TableCell>{tournament.startDateTime}</TableCell>
              <TableCell>{tournament.endDateTime}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Typography sx={{ mt: 2, fontSize: '0.875rem', color: 'text.secondary' }}>
        Showing 1-10 of {data.length}
      </Typography>

      {/* Matches Popup */}
      {showPopup && selectedTournament && (
        <MatchesPopup
          tournament={selectedTournament} // Pass the selected tournament object
          onClose={handleClosePopup}
          onModify={handleModifyMatch}
        />
      )}
    </Box>
  );
};

export default TournamentTable;
