import React, { useState, useEffect } from 'react';
import { Box, Typography, IconButton, Modal } from '@mui/material';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import backgroundImage from '../assets/backgrounds/background-with-banner.png';
import arenaIcon from '../assets/icons/arena-icon.png';
import PlayerIcon from '../components/PlayerIcon';
import profileAvatar from '../assets/summonerIcon/1.jpg';
import findMatchDisabled from '../assets/buttons/button-accept-disabled.png';
import findMatch from '../assets/buttons/button-accept-default.png';
import inQueue from '../assets/buttons/button-accept-in-queue.png';
import championSelected from '../assets/champions/0.png';
import SelectChampionModal from './SelectChampionModal';
import diamondRank from '../assets/rankIcon/diamond.png';
import Navbar from '../components/Navbar';
import SpeedUpModal from '../components/SpeedupModal';
import speedupQueueIcon from '../assets/icons/speedQueue.png';
import LowPriorityQueue from '../components/LowPriorityQueue';
import { useAuth } from '../auth/AuthProvider';
import axios from 'axios';
import env from 'react-dotenv';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';

const FindTournament = ({ logout }) => {
  const [open, setOpen] = useState(false); 
  const [selectedChampion, setSelectedChampion] = useState(null); 
  const [inQueueState, setInQueueState] = useState(false); 
  const [timer, setTimer] = useState(0); 
  const [showSpeedUpModal, setShowSpeedUpModal] = useState(false); 
  const [inSpeedUpQueue, setInSpeedUpQueue] = useState(false);
  const [showLowPriority, setShowLowPriority] = useState(false); 
  const [matchFoundModal, setMatchFoundModal] = useState(false); // New state for match found modal
  const navigate = useNavigate();
  
  const { user } = useAuth();

  // Data for the current user to be passed to TournamentBracket
  const userRank = "Diamond I"; // Replace with the actual rank if it's dynamic
  const userData = {
    playerName: user?.playername || "Player",
    rank: userRank,
    playerIcon: profileAvatar,
    champion: selectedChampion ? selectedChampion.name : "No Champion",
    championImg: selectedChampion ? selectedChampion.src : championSelected,
    status: "pending",
  };
  const IS_LOW_PRIORITY = false;
  const SPEED_UP_SECONDS = 5;

  const startQueueUI = () => {
    setInQueueState(true);
    setTimer(0);
    if (IS_LOW_PRIORITY) setShowLowPriority(true);
  };

  const handleFindMatchClick = async () => {
    if (selectedChampion){
      startQueueUI();
    }
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleChampionSelect = (champion) => {
    setSelectedChampion(champion); 
    handleClose(); 
  };

  const handleCancelQueue = () => {
    setInQueueState(false);
    setTimer(0);
    setShowSpeedUpModal(false); 
    setInSpeedUpQueue(false); 
    setShowLowPriority(false); 
  };

  useEffect(() => {
    let interval;
    if (inQueueState) {
      interval = setInterval(() => {
        setTimer((prevTime) => {
          const newTime = prevTime + 1;
          if (newTime > SPEED_UP_SECONDS && !showLowPriority) {
            setShowSpeedUpModal(true);
          }
          return newTime;
        });
      }, 1000);
    } else {
      clearInterval(interval);
    }

    return () => clearInterval(interval);
  }, [inQueueState]);

  const formatTime = (timeInSeconds) => {
    const minutes = Math.floor(timeInSeconds / 60);
    const seconds = timeInSeconds % 60;
    return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
  };

  const handleSpeedUpQueue = () => {
    setInSpeedUpQueue(true); 
    setShowSpeedUpModal(false); 
  };

  // New useEffect to listen for the "Q" key press
  useEffect(() => {
    const handleKeyPress = (event) => {
      if (event.key.toLowerCase() === 'q') {
        setMatchFoundModal(true);
        setTimeout(() => navigate('/tournamentBracket', { state: { userData } }), 1000); // Auto-navigate after 5 seconds
      }
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [navigate, userData]);

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column', 
        height: '100vh',
        overflow: 'hidden', 
      }}
    >
      <Box sx={{ flexShrink: 0, zIndex: 100 }}>
        <Navbar logout={logout} />
      </Box>

      <Box
        sx={{
          flexGrow: 1,
          position: 'relative', 
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
          display: 'flex',
          justifyContent: 'space-between', 
          flexDirection: 'column', 
        }}
      >
        <Box
          sx={{
            position: 'absolute',
            top: 16,
            left: 16,
            zIndex: 2,
            display: 'flex',
            alignItems: 'center',
          }}
        >
          <Box display="flex" alignItems="center">
            <Box component="img" src={arenaIcon} alt="Arena" sx={{ marginLeft: 3, width: '3vw', marginTop: '1px', marginRight: 1 }} />
            <Typography className="headerPrimary" sx={{ fontSize: '1.25em', display: 'inline-flex', alignItems: 'center' }}>
              ARENA
              <Box
                component="span"
                sx={{
                  width: '0.5vw',
                  height: '0.5vw',
                  backgroundColor: '#F0E6D2',
                  borderRadius: '50%',
                  display: 'inline-block',
                  marginX: '1vw',
                }}
              />
              RANKED
            </Typography>
          </Box>
        </Box>

        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginTop: 10
          }}
        >
          <PlayerIcon
            alt={user ? user.playername : ""}
            src={profileAvatar}
            width={6}
            height={6}
            clickable={false}
          />
          <Typography className="headerPrimary" fontSize={'1.25em'}>
            {user ? user.playername : ""}
          </Typography>
          <Box display={'flex'} alignItems={'center'}>
            <Box component="img" src={diamondRank} alt="Rank" sx={{ width: '3vh', height: '3vh', marginRight: 1 }} />
            <Typography className="bodySecondary" fontSize={'1em'}>
              Diamond I
            </Typography>
          </Box>

          <Box alignSelf={'center'} display={'flex'} flexDirection={'column'} marginTop={4} alignItems={'center'}>
            <Box
              component="img"
              src={selectedChampion ? selectedChampion.src : championSelected}
              alt="Select Champion"
              alignSelf={'center'}
              justifyContent={'center'}
              sx={{
                border: 2,
                borderColor: '#775A27',
                width: '10vh',
                height: '10vh',
                cursor: 'pointer',
              }}
              onClick={handleOpen} 
            />
            <Typography className="bodySecondary" sx={{ marginTop: 1 }}>
              {selectedChampion ? selectedChampion.name : 'Select a Champion'}
            </Typography>
          </Box>
        </Box>

        <Box
          sx={{
            position: 'relative', 
            display: 'flex',
            justifyContent: 'center', 
            marginBottom: '2vh',
            alignItems: 'center', 
          }}
        >
          {inQueueState && (
            <IconButton
              onClick={handleCancelQueue} 
              sx={{
                position: 'absolute',
                left: 'calc(50% - 11vw)', 
              }}
            >
              <HighlightOffIcon fontSize="large" sx={{ color: "#D8A13A" }} />
            </IconButton>
          )}

          <Box
            component="img"
            src={selectedChampion ? (inQueueState ? inQueue : findMatch) : findMatchDisabled}
            alt="Find Match"
            sx={{ width: '14vw', cursor: selectedChampion ? 'pointer' : 'not-allowed' }}
            onClick={selectedChampion && !inQueueState ? handleFindMatchClick : null} 
          />
        </Box>

        {inQueueState && (
          <Box
            sx={{
              position: 'absolute',
              bottom: '4vh',
              left: '2vw',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'flex-start',
            }}
          >
            {showLowPriority && (
              <LowPriorityQueue/>
            )}

            <Typography variant="h6" className='findMatch' sx={{ marginBottom: -2.5 }}>
              FINDING MATCH
            </Typography>

            <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: -2.5 }}>
              <Typography variant="h6" className="timer" sx={{ marginRight: '0.5rem' }}>
                {formatTime(timer)}
              </Typography>

              {inSpeedUpQueue && (
                <Box
                  component="img"
                  src={speedupQueueIcon}
                  alt="Speed Up Queue"
                  sx={{ width: '1.5rem', height: '1.5rem', marginLeft: '0.5rem' }}
                />
              )}
            </Box>

            <Typography className='estimated' sx={{ color: "#0AC1DC", marginTop: '0.5rem' }}>
              Estimated: 1:12
            </Typography>

            {showSpeedUpModal && (
              <SpeedUpModal 
                show={showSpeedUpModal} 
                onClose={handleSpeedUpQueue} 
              />
            )}
          </Box>
        )}

        <SelectChampionModal open={open} handleClose={handleClose} onChampionSelect={handleChampionSelect} />
      </Box>

      {/* Match Found Modal */}
      <Modal
        open={matchFoundModal}
        onClose={() => setMatchFoundModal(false)}
        sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
      >
        <Box
          sx={{
            bgcolor: 'background.paper',
            borderRadius: 2,
            p: 4,
            boxShadow: 24,
            textAlign: 'center',
          }}
        >
          <Typography variant="h4" gutterBottom>
            Match Found!
          </Typography>
          <Typography variant="body1">
            Redirecting to the tournament bracket in 5 seconds...
          </Typography>
        </Box>
      </Modal>
    </Box>
  );
};

export default FindTournament;
