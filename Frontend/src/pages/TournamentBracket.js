import React from 'react';
import ahri from '../assets/champions/ahri.png';
import sett from '../assets/champions/sett.png';
import background from '../assets/cherrybackground_dark.png';
import playerIcon from '../assets/summonerIcon/4895.jpg';
import { Box, Typography } from '@mui/material';
import MatchUp from '../components/MatchUp';

// TO DO: replace Dummy data for players with win/lose/pending status
const playerData = [
  { playerName: "hide on bush", rank: "Diamond I", playerIcon: playerIcon, champion: "Ahri", championImg: ahri, status: "win" },
  { playerName: "Rodan", rank: "Diamond I", playerIcon: playerIcon, champion: "Sett", championImg: sett, status: "lose" },
  { playerName: "hide on bush", rank: "Diamond I", playerIcon: playerIcon, champion: "Ahri", championImg: ahri, status: "pending" },
  { playerName: "Rodan", rank: "Diamond I", playerIcon: playerIcon, champion: "Sett", championImg: sett, status: "win" },
  // Add more players as needed, ensuring the count is even
];

const TournamentBracket = () => {
  // Calculate the midpoint to split playerData into two halves
  const midpoint = playerData.length / 2;
  const leftColumnPlayers = playerData.slice(0, midpoint);
  const rightColumnPlayers = playerData.slice(midpoint);

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: '100%',
        height: '100vh',
        backgroundImage: `url(${background})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      {/* Title and Progress Indicator */}
      <Box sx={{ textAlign: 'center', color: 'white', mt: 5 }}>
        <Typography variant="h4" fontWeight="bold" color='#F0E6D2' className='headerPrimary'>
          MATCH 1
        </Typography>
        <Typography color='#949083' className='headerSecondary'>
          COMPLETE
        </Typography>
      </Box>

      {/* Main Bracket Layout */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '80%', mt: 4 }}>
        
        {/* Left Column */}
        <Box display="flex" flexDirection="column" alignItems="flex-end" spacing={3}>
          {leftColumnPlayers.map((player, index) => (
            <MatchUp
              key={index}
              leftPlayer={player}
              rightPlayer={rightColumnPlayers[index]}
            />
          ))}
        </Box>

        {/* Right Column */}
        <Box display="flex" flexDirection="column" alignItems="flex-start" spacing={3}>
          {rightColumnPlayers.map((player, index) => (
            <MatchUp
              key={index + midpoint}
              leftPlayer={leftColumnPlayers[index]}
              rightPlayer={player}
            />
          ))}
        </Box>
      </Box>
    </Box>
  );
};

export default TournamentBracket;
