import React from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

const LeaderboardTable = ({ data }) => {
  return (
    <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>Leaderboard</Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Rank</TableCell>
            <TableCell>Player Id</TableCell>
            <TableCell>Username</TableCell>
            <TableCell>ELO</TableCell>
            <TableCell>Total Wins</TableCell>
            <TableCell>Total Matches</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((player, index) => (
            <TableRow key={index}>
              <TableCell>{index + 1}</TableCell>
              <TableCell>{player.id}</TableCell>
              <TableCell>{player.username}</TableCell>
              <TableCell>{player.elo}</TableCell>
              <TableCell>{player.totalWins}</TableCell>
              <TableCell>{player.totalMatches}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Typography sx={{ mt: 2, fontSize: '0.875rem', color: 'text.secondary' }}>
        Showing {data.length} players
      </Typography>
    </Box>
  );
};

export default LeaderboardTable;
