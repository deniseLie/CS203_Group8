import React, { useState } from 'react';
import Navbar from '../components/Navbar';
import backgroundImage from '../assets/srbackground.png';
import {
  Box,
  Select,
  MenuItem,
  TextField,
  Typography,
  LinearProgress,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import PlayerIcon from '../components/PlayerIcon';
import profilePicture from '../assets/summonerIcon/4895.jpg';

// Import each role icon explicitly
import assassinIcon from '../assets/championClass/roleicon-assassin.png';
import fighterIcon from '../assets/championClass/roleicon-fighter.png';
import mageIcon from '../assets/championClass/roleicon-mage.png';
import marksmanIcon from '../assets/championClass/roleicon-marksman.png';
import supportIcon from '../assets/championClass/roleicon-support.png';
import tankIcon from '../assets/championClass/roleicon-tank.png';

// Import rank icons
import bronzeIcon from '../assets/rankIcon/bronze.png';
import silverIcon from '../assets/rankIcon/silver.png';
import goldIcon from '../assets/rankIcon/gold.png';
import platinumIcon from '../assets/rankIcon/platinum.png';
import diamondIcon from '../assets/rankIcon/diamond.png';
import emeraldIcon from '../assets/rankIcon/emerald.png';

// Map roles to their respective icons
const roleIcons = {
  Assassin: assassinIcon,
  Fighter: fighterIcon,
  Mage: mageIcon,
  Marksman: marksmanIcon,
  Support: supportIcon,
  Tank: tankIcon,
};

// Map rank tiers to their respective images
const rankIcons = {
  Bronze: bronzeIcon,
  Silver: silverIcon,
  Gold: goldIcon,
  Emerald: emeraldIcon,
  Platinum: platinumIcon,
  Diamond: diamondIcon,
};

const Leaderboard = () => {
  // TO DO: replace dummy players data
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

  // Constants: Filter buttons and search
  const [selectedRank, setSelectedRank] = useState('ALL RANKS');
  const [selectedRole, setSelectedRole] = useState('ALL ROLES');
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
        overflowY: 'auto',
        '&::-webkit-scrollbar': { display: 'none' },
      }}
    >
      {/* Navbar */}
      <Box sx={{ position: 'relative', zIndex: 1 }}>
        <Navbar activePage="leaderboard" />
      </Box>

      {/* Filters and Search */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, px: 3, mt: 2 }}>
        
        {/* Left Side: Filter Buttons */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {/* Rank Select */}
          <Select
            value={selectedRank}
            onChange={(e) => setSelectedRank(e.target.value)}
            displayEmpty
            renderValue={(selected) => selected.toUpperCase() || "ALL RANKS"}
            sx={{
              backgroundColor: '#0F171D',
              color: '#CDBE91',
              border: '1px solid #8c7a5b',
              fontWeight: 'bold',
              fontSize: '1rem',
              minWidth: 100,
              borderRadius: 0,
              '& .MuiSelect-icon': {
                color: '#f0e6d2',
              },
            }}
          >
            <MenuItem value="ALL RANKS">
              <Typography className='headerPrimary' sx={{ fontWeight: 'bold', color:'#0F171D'}}>ALL RANKS</Typography>
            </MenuItem>
            {Object.entries(rankIcons).map(([rank, icon]) => (
              <MenuItem key={rank} value={rank} className='headerPrimary'>
                <Box component="img" src={icon} alt={rank} sx={{ width: 24, height: 24, mr: 1 }} />
                {rank.toUpperCase()}
              </MenuItem>
            ))}
          </Select>

          {/* Role Select */}
          <Select
            value={selectedRole}
            onChange={(e) => setSelectedRole(e.target.value)}
            displayEmpty
            renderValue={(selected) => selected.toUpperCase() || "ALL ROLES"}
            sx={{
              backgroundColor: '#0F171D',
              border: '1px solid #8c7a5b',
              fontWeight: 'bold',
              color: '#CDBE91',
              fontSize: '1rem',
              minWidth: 100,
              borderRadius: 0,
              '& .MuiSelect-icon': {
                color: '#f0e6d2',
              },
            }}
          >
            <MenuItem value="ALL ROLES">
              <Typography className='headerPrimary' sx={{ fontWeight: 'bold', color:'#0F171D'}}>ALL ROLES</Typography>
            </MenuItem>
            {Object.entries(roleIcons).map(([role, icon]) => (
              <MenuItem key={role} value={role} className='headerPrimary'>
                <Box component="img" src={icon} alt={role} sx={{ width: 24, height: 24, mr: 1 }} />
                {role.toUpperCase()}
              </MenuItem>
            ))}
          </Select>
        </Box>
            
        {/* Right Side: Search Bar */}
        <Box sx={{ display: 'flex', alignItems: 'center', backgroundColor: '#0F171D', borderRadius: 1, paddingX: 1, border: '1px solid #8c7a5b' }}>
          <SearchIcon sx={{ color: '#CDBE91' }} />
          <TextField
  variant="standard"
  placeholder="SEARCH PLAYER"
  InputProps={{
    disableUnderline: true,
    className: 'headerPrimary', // Apply your custom class to the input element
  }}
  sx={{
    ml: 1,
    minWidth: 200,
    '& .MuiInputBase-input': {
      color: '#CDBE91', 
      fontWeight: 'bold',
      fontFamily: 'inherit', // Ensures the class font applies if using font-family in CSS
    },
  }}
  value={search}
  onChange={(e) => setSearch(e.target.value)}
/>
        </Box>
      </Box>

      {/* Leaderboard Table */}
      <Box sx={{ mx: 3, borderRadius: 2, borderTop: '1px solid #33393f' }}>
        <Box sx={{ display: 'flex', paddingY: 1.5, fontWeight: 'bold', color: '#f0e6d2', borderBottom: '1px solid #33393f', alignItems: 'center' }}>
          <Typography className="headerPrimary" sx={{ flex: 1 }}>RANK</Typography>
          <Typography className="headerPrimary" sx={{ flex: 2 }}>PLAYER</Typography>
          <Typography className="headerPrimary" sx={{ flex: 1 }}>RANK</Typography>
          <Typography className="headerPrimary" sx={{ flex: 1 }}>LP</Typography>
          <Typography className="headerPrimary" sx={{ flex: 2 }}>WIN RATE</Typography>
          <Typography className="headerPrimary" sx={{ flex: 2 }}>MOST PLAYED ROLES</Typography>
        </Box>

        {players.map((player) => (
          <Box
            key={player.rank}
            sx={{
              display: 'flex',
              alignItems: 'center',
              paddingY: 1.5,
              color: '#f0e6d2',
              borderBottom: '1px solid #33393f',
            }}
          >
            <Typography sx={{ flex: 1, fontSize: '2em' }} className="headerPrimary">{player.rank}</Typography>
            <Box sx={{ flex: 2, display: 'flex', alignItems: 'center' }}>
              <PlayerIcon width={-1} height={-1} src={profilePicture} />
              <Typography className='headerPrimary' sx={{ marginLeft: 2 }}>{player.playerName}</Typography>
            </Box>

            {/* Rank Tier with Image */}
            <Box sx={{ flex: 1, display: 'flex', alignItems: 'center' }}>
              <Box
                component="img"
                src={rankIcons[player.rankTier]} // Display the icon based on rank
                alt={player.rankTier}
                sx={{
                  width: 24,
                  height: 24,
                  marginRight: 1,
                }}
              />
              <Typography className='headerPrimary'>{player.rankTier}</Typography>
            </Box>

            <Typography className='headerPrimary' sx={{ flex: 1 }}>{player.lp}</Typography>
            <Box sx={{ flex: 2, display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
              <Typography className='headerPrimary' sx={{ mb: 0.5 }}>{player.winRate}% ({player.wins}W {player.losses}L)</Typography>
              <LinearProgress
                variant="determinate"
                value={player.winRate}
                sx={{
                  width: '80%',
                  height: 8,
                  backgroundColor: '#2c3136',
                  '& .MuiLinearProgress-bar': { backgroundColor: '#D8A13A' },
                  borderRadius: 1,
                }}
              />
            </Box>

            {/* Role Classes Box */}
            <Box sx={{ flex: 2, display: 'flex', gap: 1 }}>
              {player.mostPlayedRoles.map((role, index) => (
                <Box
                  key={index}
                  component="img"
                  src={roleIcons[role]}
                  alt={role}
                  sx={{
                    width: '3vw',
                    height: '3vw',
                    marginRight: 2,
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
