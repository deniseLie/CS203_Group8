import React from 'react';
import { Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography } from '@mui/material';
import Navbar from '../components/Navbar';
import backgroundImage from '../assets/srbackground.png';

// Sample data for the leaderboard
const leaderboardData = [
  { rank: 1, name: 'John Doe', elo: 1500 },
  { rank: 2, name: 'Jane Smith', elo: 1400 },
  { rank: 3, name: 'Alice Johnson', elo: 1300 },
  { rank: 4, name: 'Michael Brown', elo: 1200 },
  { rank: 5, name: 'Chris Evans', elo: 1100 },
];

function Leaderboard() {
  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        minHeight: '100vh',
        width: '100%',
        position: 'relative',
      }}
    >
      {/* Navbar */}
      <Box sx={{ position: 'relative', zIndex: 1 }}>
        <Navbar activePage="leaderboard" />
      </Box>

      {/* Main Content Container */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          // justifyContent: 'center',
          paddingTop: 5,
          paddingBottom: 5
        }}
      >
        {/* Leaderboard Header */}
        <Typography variant="h4" component="h1" gutterBottom sx={{ color: 'white', mb: 3 }}>
          Leaderboard (Dummy)
        </Typography>

        {/* Leaderboard Table */}
        <TableContainer component={Paper} sx={{ maxWidth: 800 }}>
          <Table aria-label="leaderboard table">
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: 'bold' }}>Rank</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }}>Name</TableCell>
                <TableCell sx={{ fontWeight: 'bold' }}>Elo</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {leaderboardData.map((player) => (
                <TableRow key={player.rank}>
                  <TableCell>{player.rank}</TableCell>
                  <TableCell>{player.name}</TableCell>
                  <TableCell>{player.elo}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Box>
  );
}

export default Leaderboard;
