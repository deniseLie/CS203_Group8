// src/components/PlayerTable.js
import React from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

const PlayerTable = ({ data }) => {
  return (
    <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>Player Table</Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Player Id</TableCell>
            <TableCell>Username</TableCell>
            <TableCell>Playername</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Auth Provider</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((player, index) => (
            <TableRow key={index}>
              <TableCell>{player.id}</TableCell>
              <TableCell>{player.username}</TableCell>
              <TableCell>{player.playername}</TableCell>
              <TableCell>{player.email}</TableCell>
              <TableCell>{player.authProvider}</TableCell>
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

export default PlayerTable;
