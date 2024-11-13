import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, Dialog, DialogContent, DialogActions } from '@mui/material';
import { Link, useLocation } from 'react-router-dom';
import playAgain from '../assets/buttons/play-again-button.png';
import playerIcon from '../assets/summonerIcon/1.jpg';
import background from '../assets/backgrounds/srbackground.png';
import arenaIcon from '../assets/icons/arena-icon.png';
import rankCircle from '../assets/icons/ranked-frame.png';

const PostGame = () => {
  const location = useLocation();
  const { rankings = [], currentPlayer } = location.state || {}; // rankings from the tournament bracket

  const leaderboardData = rankings;

  // State for showing the AFK modal
  const [isAFKModalOpen, setAFKModalOpen] = useState(false);

  useEffect(() => {
    if (currentPlayer?.isAFK) {
      setAFKModalOpen(true);
    }
  }, [currentPlayer]);

  // Close AFK modal
  const handleAgree = () => setAFKModalOpen(false);

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
      {/* Title Section */}
      <Box
        sx={{
          width: '100%',
          borderBottom: 1,
          borderBottomColor: '#464F4D',
        }}
      >
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
          <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 1 }}>
            <Box component="img" src={arenaIcon} sx={{ width: 40, height: 40, mr: 1 }} />
            <Box>
              {currentPlayer.standing && (
                <Typography className='headerPrimary' fontWeight="bold">
                  {currentPlayer.standing} PLACE
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
          <Typography sx={{ color: '#949083', mb: 1 }} className='headerSecondary'>
            STANDING
          </Typography>
          {leaderboardData.map((entry, index) => (
            <Box key={index} display="flex" alignItems="center" sx={{ height: '50px' }}>
              <Typography
                variant="body2"
                sx={{
                  width: '50px',
                  fontWeight: 'bold',
                  color: entry.playerName === currentPlayer.playerName ? '#D8A13A' : '#F0E6D2',
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
                  borderLeft: entry.playerName === currentPlayer.playerName ? '5px solid #D8A13A' : 'none',
                }}
              />
              <Typography
                className='headerPrimary'
                variant="body2"
                sx={{ color: entry.playerName === currentPlayer.playerName ? '#D8A13A' : '#F0E6D2' }}
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
                sx={{ color: entry.playerName === currentPlayer.playerName ? '#D8A13A' : '#F0E6D2', fontWeight: entry.playerName === currentPlayer.playerName ? 'bold' : 'normal' }}
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
                sx={{ color: entry.playerName === currentPlayer.playerName ? '#D8A13A' : '#F0E6D2' }}
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

      {/* AFK Warning Modal */}
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
