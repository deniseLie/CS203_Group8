import React from 'react';
import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Button,
  Avatar,
  Stack,
} from '@mui/material';

/**
 * TournamentTable Component
 *
 * Displays a table of tournaments and player details.
 */
const TournamentTable = ({ data, onViewDetails }) => {
  return (
    <Box
      sx={{
        backgroundColor: '#ffffff',
        borderRadius: '8px',
        p: 2,
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
      }}
    >
      <Typography variant="h6" sx={{ mb: 2 }}>
        Tournaments List
      </Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Tournament ID</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Players</TableCell>
            <TableCell>Start Time</TableCell>
            <TableCell>End Time</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((tournament) => (
            <TableRow key={tournament.tournamentId}>
              <TableCell>{tournament.tournamentId}</TableCell>
              <TableCell>{tournament.status}</TableCell>
              <TableCell>
                <Stack direction="row" spacing={1}>
                  {tournament.players.slice(0, 3).map((player, index) => (
                    <Avatar key={index} src={player.avatar || ''} alt={player.name} />
                  ))}
                  {tournament.players.length > 3 && (
                    <Typography>+{tournament.players.length - 3}</Typography>
                  )}
                </Stack>
              </TableCell>
              <TableCell>{tournament.timestampStart}</TableCell>
              <TableCell>{tournament.timestampEnd || 'Ongoing'}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  onClick={() => onViewDetails(tournament)}
                  sx={{ textTransform: 'none' }}
                >
                  View Details
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Box>
  );
};

export default TournamentTable;
