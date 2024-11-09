// src/components/Matches.js
import React from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableHead, TableRow, Button } from '@mui/material';

const Matches = ({ matches, onModify }) => (
  <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
    <Typography variant="h6" sx={{ mb: 2 }}>Matches</Typography>
    <Table>
      <TableHead>
        <TableRow>
          <TableCell>Match ID</TableCell>
          <TableCell>Round No.</TableCell>
          <TableCell>Status</TableCell>
          <TableCell>Actions</TableCell>
        </TableRow>
      </TableHead>
      <TableBody>
        {matches.map((match) => (
          <TableRow key={match.id}>
            <TableCell>{match.id}</TableCell>
            <TableCell>{match.round}</TableCell>
            <TableCell>
              <Button variant="contained" color={match.status === "Ongoing" ? "warning" : "success"}>
                {match.status}
              </Button>
            </TableCell>
            <TableCell>
              <Button variant="outlined" onClick={() => onModify(match.id)}>Modify</Button>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  </Box>
);

export default Matches;
