import React from 'react';
import { Box, Typography, Avatar, Button } from '@mui/material';
import playAgain from '../assets/play-again-button.png';
import playerIcon from '../assets/4895.jpg';
import background from '../assets/srbackground.png';
import arenaIcon from '../assets/arena-icon.png';
import rankCircle from '../assets/ranked-frame.png';
import { Link } from 'react-router-dom';

const leaderboardData = [
  { standing: '1ST', champion: 'Ahri', playerName: 'hide on bush', kd: '8/0', kda: '8.0 KDA' },
  { standing: '2ND', champion: 'Sett', playerName: 'Rodan', kd: '8/0', kda: '8.0 KDA' },
  { standing: '3RD', champion: 'Bel\'Veth', playerName: 'xDivineSword', kd: '8/0', kda: '8.0 KDA' },
  { standing: '4TH', champion: 'Miss Fortune', playerName: 'lilWanton', kd: '8/0', kda: '8.0 KDA' },
  { standing: '5TH', champion: 'Galio', playerName: 'GodanRoose', kd: '8/0', kda: '8.0 KDA' },
  { standing: '6TH', champion: 'Zac', playerName: 'Radon', kd: '8/0', kda: '8.0 KDA' },
  { standing: '7TH', champion: 'Jinx', playerName: 'LikeFromArcane', kd: '1/1', kda: '1.0 KDA' },
  { standing: '8TH', champion: 'Vi', playerName: 'ARAMLOVER', kd: '0/1', kda: '0.0 KDA' },
];

const currentPlayer = 'hide on bush';

const PostGame = () => {
  // Find the current player's standing
  const currentPlayerData = leaderboardData.find(entry => entry.playerName === currentPlayer);
  const currentPlayerStanding = currentPlayerData ? currentPlayerData.standing : null;

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        height: '100vh',
        backgroundImage: `url(${background})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        color: '#F0E6D2',
        paddingTop: 1,
        justifyContent: 'space-between',
      }}
    >
      {/* Full-width Border Section */}
      <Box
        sx={{
          width: '100%',
          borderBottom: 1,
          borderBottomColor: '#464F4D',
        }}
      >
        {/* Title Section */}
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            width: '100%',
            maxWidth: '90%',
            margin: '0 auto',
          }}
        >
          <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 1}}>
            <Avatar src={arenaIcon} sx={{ width: 40, height: 40, mr: 1 }} />
            <Box>
              {currentPlayerStanding && (
                <Typography className='headerPrimary' fontWeight="bold">
                  {currentPlayerStanding} PLACE
                </Typography>
              )}
              <Typography variant="subtitle2" color="#949083">
                Arena • 23:10
              </Typography>
            </Box>
          </Box>
        </Box>
      </Box>

      {/* Leaderboard Section */}
      <Box sx={{ display: 'flex', width: '100%', justifyContent: 'space-between', maxWidth: '90%', mt: 1 }}>
        {/* Standing and Champion */}
        <Box sx={{ flex: 1 }}>
          <Typography sx={{ color: '#949083', mb: 1}} className='headerSecondary'>
            STANDING
          </Typography>
          {leaderboardData.map((entry, index) => (
            <Box key={index} display="flex" alignItems="center" sx={{  height: '50px' }}>
              <Typography variant="body2" sx={{ width: '50px', fontWeight: 'bold', color: '#F0E6D2', fontSize: '1.2rem' }} className='headerPrimary'>
                {entry.standing}
              </Typography>
              <Avatar src={require(`../assets/champions/${entry.champion.toLowerCase().replace(/[\s']/g, '')}.png`)} sx={{ width: 40, height: 40, mr: 2 }} />
              <Typography className='headerPrimary' variant="body2" sx={{ color: '#F0E6D2' }}>
                {entry.champion}
              </Typography>
            </Box>
          ))}
        </Box>

        {/* Player Name */}
        <Box sx={{ flex: 1 }}>
          <Typography variant="subtitle2" className='headerSecondary' sx={{ color: '#949083', textAlign: 'left', mb: 1 }}>
            PLAYER
          </Typography>
          {leaderboardData.map((entry, index) => (
            <Box key={index} display="flex" alignItems="center" justifyContent="flex-start" sx={{ height: '50px' }}>
              <Avatar src={playerIcon} sx={{ width: 30, height: 30, mr: 1 }} />
              <Typography variant="body2" className='headerPrimary' sx={{ color: '#F0E6D2', fontWeight: entry.playerName === currentPlayer ? 'bold' : 'normal' }}>
                {entry.playerName}
              </Typography>
            </Box>
          ))}
        </Box>

        {/* K/D and KDA */}
        <Box sx={{ flex: 1, textAlign: 'center' }}>
          <Typography variant="subtitle2" className='headerSecondary' sx={{ color: '#949083', mb:1}}>
            K/D
          </Typography>
          {leaderboardData.map((entry, index) => (
            <Box key={index} display="flex" alignItems="center" flexDirection="column" sx={{ height: '50px' }}>
              <Typography variant="body2" sx={{ color: '#F0E6D2' }} className='headerPrimary'>
                {entry.kd}
              </Typography>
              <Typography variant="caption" color="#949083" className='bodySecondary'>
                {entry.kda}
              </Typography>
            </Box>
          ))}
        </Box>

        {/* LP Section with Ranked Circle */}
        <Box sx={{ flex: 1, textAlign: 'center', position: 'relative' , alignContent: 'center'}}>
          <Box
            sx={{
              position: 'relative',
              width: '15vw', // Adjust the size as needed
              height: '15vw',
              backgroundImage: `url(${rankCircle})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            {/* Overlay Text */}
            <Box sx={{ position: 'absolute', top: '20%', textAlign: 'center' }}>
              <Typography variant="subtitle2" color="#0AC1DC" className='headerPrimary'>
                +170 LP
              </Typography>
            </Box>
            <Box sx={{ position: 'absolute', top: '50%', transform: 'translateY(-50%)', textAlign: 'center' }}>
              <Typography fontSize={'2.5em'} fontWeight="bold" color="#B68C34" className='headerPrimary'>
                70 LP
              </Typography>
            </Box>
            <Box sx={{ position: 'absolute', bottom: '20%', textAlign: 'center' }}>
              <Typography variant="caption"  className='headerPrimary'>
                DIAMOND I
              </Typography>
            </Box>
          </Box>
        </Box>
      </Box>

      {/* Play Again Button */}
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          width: '100%',
        }}
        component={Link}
        to={"/"}
      >
        <Button
          sx={{
            backgroundImage: `url(${playAgain})`,
            backgroundSize: 'contain',
            backgroundRepeat: 'no-repeat',
            width: '12vw',
            height: '12vh',
            padding: 0,
            minWidth: 'unset',
            minHeight: 'unset',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
        </Button>
      </Box>
    </Box>
  );
};

export default PostGame;
