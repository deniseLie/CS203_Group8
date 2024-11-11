import React, { useEffect, useState } from 'react';
import { Box, Typography, Button, Dialog, DialogContent, DialogActions } from '@mui/material';
import { Link } from 'react-router-dom';
import playAgain from '../assets/play-again-button.png';
import playerIcon from '../assets/4895.jpg';
import background from '../assets/srbackground.png';
import arenaIcon from '../assets/arena-icon.png';
import rankCircle from '../assets/ranked-frame.png';

// Sample data with an AFK flag for demonstration
// TO DO: Replace leaderboard data and current player dummy data  
const leaderboardData = [
  { standing: '1ST', champion: 'Ahri', playerName: 'ARAMLOVER', kd: '8/0', kda: '8.0 KDA' },
  { standing: '2ND', champion: 'Sett', playerName: 'Rodan', kd: '8/0', kda: '8.0 KDA' },
  { standing: '3RD', champion: 'Bel\'Veth', playerName: 'xDivineSword', kd: '8/0', kda: '8.0 KDA' },
  { standing: '4TH', champion: 'Miss Fortune', playerName: 'lilWanton', kd: '8/0', kda: '8.0 KDA' },
  { standing: '5TH', champion: 'Galio', playerName: 'GodanRoose', kd: '8/0', kda: '8.0 KDA' },
  { standing: '6TH', champion: 'Zac', playerName: 'Radon', kd: '8/0', kda: '8.0 KDA' },
  { standing: '7TH', champion: 'Jinx', playerName: 'LikeFromArcane', kd: '1/1', kda: '1.0 KDA' },
  { standing: '8TH', champion: 'Vi', playerName: 'hide on bush', kd: '0/1', kda: '0.0 KDA', isAFK: true },
];

const currentPlayer = 'hide on bush';

const PostGame = () => {
  // Find the current player's standing
  const currentPlayerData = leaderboardData.find(entry => entry.playerName === currentPlayer);
  const currentPlayerStanding = currentPlayerData ? currentPlayerData.standing : null;

  // State for showing the AFK modal
  const [isAFKModalOpen, setAFKModalOpen] = useState(false);

  // Check if the current player is marked as AFK
  useEffect(() => {
    if (currentPlayerData?.isAFK) {
      setAFKModalOpen(true);
    }
  }, [currentPlayerData]);

  // Function: Close AFK Modal
  const handleAgree = () => {
    setAFKModalOpen(false); // Close the modal on clicking "I AGREE"
  };
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
            <Box component="img" src={arenaIcon} sx={{ width: 40, height: 40, mr: 1 }} />
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
            <Box key={index} display="flex" alignItems="center" sx={{ height: '50px' }}>
              <Typography
                variant="body2"
                sx={{
                  width: '50px',
                  fontWeight: 'bold',
                  color: entry.playerName === currentPlayer ? '#D8A13A' : '#F0E6D2',
                  fontSize: '1.2rem'
                }}
                className='headerPrimary'
              >
                {entry.standing}
              </Typography>
              <Box
                sx={{
                  width: 40,
                  height: 40,
                  mr: 2,
                  backgroundImage: `url(${require(`../assets/champions/${entry.champion.toLowerCase().replace(/[\s']/g, '')}.png`)})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  borderLeft: entry.playerName === currentPlayer ? '5px solid #D8A13A' : 'none',
                }}
              />
              <Typography
                className='headerPrimary'
                variant="body2"
                sx={{ color: entry.playerName === currentPlayer ? '#D8A13A' : '#F0E6D2' }}
              >
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
              <Box component="img" src={playerIcon} sx={{ width: 30, height: 30, mr: 1 }} />
              <Typography
                variant="body2"
                className='headerPrimary'
                sx={{ color: entry.playerName === currentPlayer ? '#D8A13A' : '#F0E6D2', fontWeight: entry.playerName === currentPlayer ? 'bold' : 'normal' }}
              >
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
              <Typography
                variant="body2"
                sx={{ color: entry.playerName === currentPlayer ? '#D8A13A' : '#F0E6D2' }}
                className='headerPrimary'
              >
                {entry.kd}
              </Typography>
              <Typography variant="caption" color="#949083" className='bodySecondary'>
                {entry.kda}
              </Typography>
            </Box>
          ))}
        </Box>

        {/* LP Section with Ranked Circle */}
        <Box sx={{ flex: 1, textAlign: 'center', position: 'relative', alignContent: 'center'}}>
          <Box
            sx={{
              position: 'relative',
              width: '15vw',
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
              <Typography variant="caption" className='headerPrimary'>
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
        to="/"
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

            {/* AFK Warning Modal (inform user of applied penalty if they afk) */}
      <Dialog
        open={isAFKModalOpen}
        onClose={handleAgree}
        maxWidth="sm"
        fullWidth
        PaperProps={{
          style: {
            backgroundColor: '#1F2326',
            border: '1px solid #775A27',
            boxShadow: 'none',
          },
        }}
      >
        <DialogContent>
          <Typography variant="h6" fontWeight="bold" color="#F0E6D2" align="center" className='headerPrimary'>
            LEAVERBUSTER WARNING
          </Typography>
          <Typography variant="body1" color="#F0E6D2" align="center" sx={{ mt: 2 }} className='bodyPrimary'>
            Abandoning a match (or going AFK) makes the match unfair for your teammates
            and carries a penalty in League. Do you agree not to leave any further games?
          </Typography>
        </DialogContent>
        <DialogActions sx={{ justifyContent: 'center' }}>
          <Button
            onClick={handleAgree}
            sx={{
              backgroundColor: '#775A27',
              color: '#F0E6D2',
              fontWeight: 'bold',
              px: 4,
              py: 1,
              marginBottom:2,
              '&:hover': {
                backgroundColor: '#b88f31',
              },
            }}
          >
            I AGREE
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default PostGame;
