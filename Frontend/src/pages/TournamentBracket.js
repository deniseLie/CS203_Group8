import React from 'react';
import ahri from '../assets/champions/103.png';
import sett from '../assets/champions/875.png';
import diamondRank from '../assets/rankIcon/diamond.png';
import background from '../assets/cherrybackground_dark.png';
import playerIcon from '../assets/4895.jpg';
import { Box, Typography } from '@mui/material';
import PlayerIcon from '../components/PlayerIcon'; // Import PlayerIcon

// Dummy data for players with win/lose/pending status
const playerData = [
  { playerName: "hide on bush", rank: "Diamond I", playerIcon: playerIcon, champion: "Ahri", championImg: ahri, status: "win" },
  { playerName: "Rodan", rank: "Diamond I", playerIcon: playerIcon, champion: "Sett", championImg: sett, status: "lose" },
  { playerName: "hide on bush", rank: "Diamond I", playerIcon: playerIcon, champion: "Ahri", championImg: ahri, status: "pending" },
  { playerName: "Rodan", rank: "Diamond I", playerIcon: playerIcon, champion: "Sett", championImg: sett, status: "win" },
  // Add more players as needed, ensuring the count is even
];

const PlayerCard = ({ name, rankIcon, playerIcon, championImg, rank, status }) => {
  // Determine the border color based on win/lose/pending status
  let borderColor;
  if (status === "win") borderColor = "#0AC1DC";
  else if (status === "lose") borderColor = "#7B0C21";
  else borderColor = "transparent"; // No border for pending

  return (
    <Box display="flex" alignItems="center" spacing={1}>
      {/* Square Champion Image with Player Icon overlay */}
      <Box sx={{ position: 'relative', width: 75, height: 75, border: `2px solid ${borderColor}` }}>
        <Box
          sx={{
            width: '100%',
            height: '100%',
            backgroundImage: `url(${championImg})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            borderRadius: 1,
          }}
        />
        {/* Overlayed Player Icon */}
        <Box sx={{ position: 'absolute', bottom: -10, right: -10 }}>
          <PlayerIcon src={playerIcon} width={-3.5} height={-3.5} clickable={false} />
        </Box>
      </Box>
      
      {/* Player Info */}
      <Box display="flex" flexDirection="column" ml={1}>
        <Typography variant="body1" className='headerPrimary' sx={{ fontWeight: 'bold', color: "#F0E6D2" }}>
          {name}
        </Typography>
        <Box display="flex" alignItems="center">
          <Box component="img" src={rankIcon} sx={{ width: 18, height: 18, marginRight: 0.5 }} />
          <Typography variant="caption" className='bodySecondary' sx={{ color: '#949083' }}>
            {rank}
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

const MatchUp = ({ leftPlayer, rightPlayer }) => (
  <Box
    sx={{
      width: '100%',
      padding: 2,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      my: 1,
      borderBottom: 1.5,
      borderBottomColor: '#A3C7C7',
    }}
  >
    <PlayerCard name={leftPlayer.playerName} rankIcon={diamondRank} playerIcon={leftPlayer.playerIcon} championImg={leftPlayer.championImg} rank={leftPlayer.rank} status={leftPlayer.status} />
    <Typography variant="h5" sx={{ color: 'white', fontWeight: 'bold', mx: 2 }}>
      VS
    </Typography>
    <PlayerCard name={rightPlayer.playerName} rankIcon={diamondRank} playerIcon={rightPlayer.playerIcon} championImg={rightPlayer.championImg} rank={rightPlayer.rank} status={rightPlayer.status} />
  </Box>
);

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
