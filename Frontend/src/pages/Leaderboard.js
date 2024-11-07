// import React from 'react';
// import { Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography } from '@mui/material';
// import Navbar from '../components/Navbar';
// import backgroundImage from '../assets/srbackground.png';

// // Sample data for the leaderboard
// const leaderboardData = [
//   { rank: 1, name: 'John Doe', elo: 1500 },
//   { rank: 2, name: 'Jane Smith', elo: 1400 },
//   { rank: 3, name: 'Alice Johnson', elo: 1300 },
//   { rank: 4, name: 'Michael Brown', elo: 1200 },
//   { rank: 5, name: 'Chris Evans', elo: 1100 },
// ];

// function Leaderboard() {
//   return (
//     <Box
//       sx={{
//         backgroundImage: `url(${backgroundImage})`,
//         backgroundSize: 'cover',
//         backgroundPosition: 'center',
//         backgroundRepeat: 'no-repeat',
//         minHeight: '100vh',
//         width: '100%',
//         position: 'relative',
//       }}
//     >
//       {/* Navbar */}
//       <Box sx={{ position: 'relative', zIndex: 1 }}>
//         <Navbar activePage="leaderboard" />
//       </Box>

//       {/* Main Content Container */}
//       <Box
//         sx={{
//           display: 'flex',
//           flexDirection: 'column',
//           alignItems: 'center',
//           // justifyContent: 'center',
//           paddingTop: 5,
//           paddingBottom: 5
//         }}
//       >
//         {/* Leaderboard Header */}
//         <Typography variant="h4" component="h1" gutterBottom sx={{ color: 'white', mb: 3 }}>
//           Leaderboard (Dummy)
//         </Typography>

//         {/* Leaderboard Table */}
//         <TableContainer component={Paper} sx={{ maxWidth: 800 }}>
//           <Table aria-label="leaderboard table">
//             <TableHead>
//               <TableRow>
//                 <TableCell sx={{ fontWeight: 'bold' }}>Rank</TableCell>
//                 <TableCell sx={{ fontWeight: 'bold' }}>Name</TableCell>
//                 <TableCell sx={{ fontWeight: 'bold' }}>Elo</TableCell>
//               </TableRow>
//             </TableHead>
//             <TableBody>
//               {leaderboardData.map((player) => (
//                 <TableRow key={player.rank}>
//                   <TableCell>{player.rank}</TableCell>
//                   <TableCell>{player.name}</TableCell>
//                   <TableCell>{player.elo}</TableCell>
//                 </TableRow>
//               ))}
//             </TableBody>
//           </Table>
//         </TableContainer>
//       </Box>
//     </Box>
//   );
// }
import React, { useState } from 'react';
import Navbar from '../components/Navbar';
import backgroundImage from '../assets/srbackground.png'; // Ensure this path is correct
import {
  Box,
  Button,
  Select,
  MenuItem,
  TextField,
  Typography,
  Avatar,
  LinearProgress,
} from '@mui/material';
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import SearchIcon from '@mui/icons-material/Search';

const Leaderboard = () => {
  // Sample data; replace this with real data or API calls
  const players = Array.from({ length: 10 }, (_, i) => ({
    rank: i + 1,
    playerName: 'hide on bush',
    rankTier: 'Diamond',
    lp: 313,
    winRate: 72,
    wins: 47,
    losses: 12,
    mostPlayedRoles: ['Assassin', 'Fighter', 'Tank'],
  }));

  const [selectedRank, setSelectedRank] = useState('All');
  const [selectedRole, setSelectedRole] = useState('All');
  const [search, setSearch] = useState('');

  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        minHeight: '100vh',
        width: '100%',
        color: '#f0e6d2',
        overflow: 'hidden', // Remove both vertical and horizontal scroll bars
        position: 'relative',
        overflowY: 'auto',
        '&::-webkit-scrollbar': { display: 'none' }, // Hide scrollbar
        msOverflowStyle: 'none',
        scrollbarWidth: 'none'
      }}
    >
      {/* Navbar */}
      <Box sx={{ position: 'relative', zIndex: 1 }}>
        <Navbar activePage="leaderboard" />
      </Box>

      {/* Filters and Search */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3, px: 3, mt: 2 }}>
        <Button
          variant="contained"
          startIcon={<FilterAltIcon />}
          sx={{ backgroundColor: '#1f2327', color: '#f0e6d2', textTransform: 'none' }}
        >
          Filters
        </Button>
        <Select
          value={selectedRank}
          onChange={(e) => setSelectedRank(e.target.value)}
          sx={{ backgroundColor: '#1f2327', color: '#f0e6d2', minWidth: 100 }}
        >
          <MenuItem value="All">Rank</MenuItem>
          <MenuItem value="Diamond">Diamond</MenuItem>
          {/* Add other ranks */}
        </Select>
        <Select
          value={selectedRole}
          onChange={(e) => setSelectedRole(e.target.value)}
          sx={{ backgroundColor: '#1f2327', color: '#f0e6d2', minWidth: 100 }}
        >
          <MenuItem value="All">Role</MenuItem>
          <MenuItem value="Top">Top</MenuItem>
          {/* Add other roles */}
        </Select>
        <Box sx={{ display: 'flex', alignItems: 'center', backgroundColor: '#1f2327', borderRadius: 1, paddingX: 1 }}>
          <SearchIcon sx={{ color: '#f0e6d2' }} />
          <TextField
            variant="standard"
            placeholder="Search Player Name"
            InputProps={{
              disableUnderline: true,
            }}
            sx={{
              ml: 1,
              minWidth: 200,
              '& input': { color: '#f0e6d2' }, // Use sx to set input color directly
            }}
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </Box>
      </Box>

      {/* Leaderboard Table */}
      <Box sx={{ mx: 3, borderRadius: 2 }}>
        <Box sx={{ display: 'flex', padding: 2, fontWeight: 'bold', color: '#f0e6d2' }}>
          <Typography sx={{ flex: 1 }}>Rank</Typography>
          <Typography sx={{ flex: 2 }}>Player</Typography>
          <Typography sx={{ flex: 1 }}>Rank</Typography>
          <Typography sx={{ flex: 1 }}>LP</Typography>
          <Typography sx={{ flex: 2, mr: 5 }}>Win Rate</Typography> {/* Adjusted spacing */}
          <Typography sx={{ flex: 3, pl: 5 }}>Most Played Roles</Typography> {/* Adjusted spacing */}
        </Box>

        {players.map((player) => (
          <Box
            key={player.rank}
            sx={{
              display: 'flex',
              alignItems: 'center',
              paddingY: 2,
              paddingX: 0,
              color: '#f0e6d2',
              borderBottom: '1px solid #33393f',
              mx: 2
            }}
          >
            <Typography sx={{ flex: 1 }}>{player.rank}</Typography>
            <Box sx={{ flex: 2, display: 'flex', alignItems: 'center' }}>
              <Avatar src="/path/to/player-avatar.png" alt={player.playerName} sx={{ mr: 2 }} />
              <Typography>{player.playerName}</Typography>
            </Box>
            <Typography sx={{ flex: 1 }}>{player.rankTier}</Typography>
            <Typography sx={{ flex: 1 }}>{player.lp}</Typography>
            <Box sx={{ flex: 2, display: 'flex', flexDirection: 'column', mr: 5 }}> {/* Adjusted spacing */}
              <Typography>{player.winRate}% ({player.wins}W {player.losses}L)</Typography>
              <LinearProgress
                variant="determinate"
                value={player.winRate}
                sx={{
                  height: 8,
                  backgroundColor: '#2c3136',
                  '& .MuiLinearProgress-bar': { backgroundColor: '#00b9e4' },
                  borderRadius: 1,
                }}
              />
            </Box>
            <Box sx={{ flex: 3, display: 'flex', gap: 1, pl: 5 }}> {/* Adjusted spacing */}
              {player.mostPlayedRoles.map((role, index) => (
                <Avatar
                  key={index}
                  src={`../assets/championClass/roleicon-${role.toLowerCase()}.png`}
                  alt={role}
                  sx={{
                    width: 24,
                    height: 24,
                    fontSize: '0.75rem',
                  }}
                />
              ))}
            </Box>
          </Box>
        ))}
      </Box>
    </Box>
  );
};

export default Leaderboard;
