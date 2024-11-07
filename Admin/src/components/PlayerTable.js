// src/components/PlayerTable.js
import React from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

const PlayerTable = ({ data }) => (
  <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
    <Typography variant="h6" sx={{ mb: 2 }}>Player Table</Typography>
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Player Id</TableCell>
          <TableCell>Username</TableCell>
          <TableCell>ELO</TableCell>
          <TableCell>Rank</TableCell>
          <TableCell>Avg Place</TableCell>
          <TableCell>First Place %</TableCell>
          <TableCell>Avg KD Rate</TableCell>
          <TableCell>Total Wins</TableCell>
          <TableCell>Total Match No.</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {data.map((player) => (
          <TableRow key={player.id}>
            <TableCell>{player.id}</TableCell>
            <TableCell>{player.username}</TableCell>
            <TableCell>{player.elo}</TableCell>
            <TableCell>
              <img src={player.rankIcon} alt="Rank" style={{ width: '24px', height: '24px' }} />
            </TableCell>
            <TableCell>{player.avgPlace}</TableCell>
            <TableCell>{player.firstPlacePercentage}</TableCell>
            <TableCell>{player.avgKdRate}</TableCell>
            <TableCell>{player.totalWins}</TableCell>
            <TableCell>{player.totalMatchNumber}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
    <Typography sx={{ mt: 2, fontSize: '0.875rem', color: 'text.secondary' }}>
      Showing 1-{data.length} of {data.length}
    </Typography>
  </Box>
);

export default PlayerTable;
