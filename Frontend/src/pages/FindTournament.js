import React, { useState, useEffect } from 'react';
import { Box, Typography, IconButton } from '@mui/material';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import backgroundImage from '../assets/background-with-banner.png';
import arenaIcon from '../assets/arena-icon.png';
import PlayerIcon from '../components/PlayerIcon';
import profileAvatar from '../assets/4895.jpg';
import findMatchDisabled from '../assets/button-accept-disabled.png';
import findMatch from '../assets/button-accept-default.png';
import inQueue from '../assets/button-accept-in-queue.png';
import championSelected from '../assets/champions/0.png';
import SelectChampionModal from './SelectChampionModal';
import diamondRank from '../assets/rankIcon/diamond.png';
import Navbar from '../components/Navbar';
import SpeedUpModal from '../components/SpeedupModal';
import speedupQueueIcon from '../assets/speedQueue.png';
import redWarning from '../assets/red-warning.png'; // Import the red warning icon
import LowPriorityQueue from '../components/LowPriorityQueue';

const FindTournament = ({ logout }) => {
  const [open, setOpen] = useState(false); 
  const [selectedChampion, setSelectedChampion] = useState(null); 
  const [inQueueState, setInQueueState] = useState(false); 
  const [timer, setTimer] = useState(0); 
  const [showSpeedUpModal, setShowSpeedUpModal] = useState(false); 
  const [inSpeedUpQueue, setInSpeedUpQueue] = useState(false);
  const [showLowPriority, setShowLowPriority] = useState(false); // Track if low priority queue should be shown
  
  // DUMMY: if user is lowpriority
  const IS_LOW_PRIORITY = false;
  // constant : how many seconds to show speed up q
  const SPEED_UP_SECONDS = 5;

  // Function : when user starts the queue
  const handleFindMatchClick = () => {
    if (selectedChampion) {
      setInQueueState(true); 
      setTimer(0); 
      if (IS_LOW_PRIORITY){
        setShowLowPriority(true); // Show low priority box when entering queue
      }
    }
  };

  // Functions: Open and close champion modal
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  // Function: Set selected champion from champion modal
  const handleChampionSelect = (champion) => {
    setSelectedChampion(champion); 
    handleClose(); 
  };

  // Function: Cancel queue -> Reset timer, hide all modals, timer
  const handleCancelQueue = () => {
    setInQueueState(false);
    setTimer(0);
    setShowSpeedUpModal(false); 
    setInSpeedUpQueue(false); 
    setShowLowPriority(false); // Hide low priority queue box when exiting queue
  };

  // Function: Render timer text every second, and if timer > speed up seconds show speedup modal
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

  // Function: Format Time to minutes:seconds
  const formatTime = (timeInSeconds) => {
    const minutes = Math.floor(timeInSeconds / 60);
    const seconds = timeInSeconds % 60;
    return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
  };

  // Function: When speed up, don't show speedup modal
  const handleSpeedUpQueue = () => {
    setInSpeedUpQueue(true); 
    setShowSpeedUpModal(false); 
  };

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column', 
        height: '100vh',
        overflow: 'hidden', 
      }}
    >
      {/* Navbar */}
      <Box sx={{ flexShrink: 0, zIndex: 100 }}>
        <Navbar logout={logout} />
      </Box>

      {/* Main content area */}
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
        {/* Top content: Arena Icon and Arena Ranked */}
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

        {/* Centered content: profile pic, rank, etc. */}
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginTop: 10
          }}
        >
          <PlayerIcon
            alt="Hide on bush"
            src={profileAvatar}
            width={6}
            height={6}
            clickable={false}
          />
          <Typography className="headerPrimary" fontSize={'1.25em'}>
            hide on bush
          </Typography>
          <Box display={'flex'} alignItems={'center'}>
            <Box component="img" src={diamondRank} alt="Rank" sx={{ width: '3vh', height: '3vh', marginRight: 1 }} />
            <Typography className="bodySecondary" fontSize={'1em'}>
              Diamond I
            </Typography>
          </Box>

          {/* Choose champion and select a champion text */}
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

        {/* Bottom content: Find Match button and optional cancel icon */}
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

        {/* Low Priority Queue and Timer section */}
        {inQueueState && (
          <Box
            sx={{
              position: 'absolute',
              bottom: '4vh',
              left: '2vw',
              display: 'flex',
              flexDirection: 'column', // Stack elements vertically
              alignItems: 'flex-start',
            }}
          >
            {/* Low Priority Queue Warning - shows only when in queue */}
            {showLowPriority && (
              <LowPriorityQueue/>
            )}

            {/* FINDING MATCH */}
            <Typography variant="h6" className='findMatch' sx={{ marginBottom: -2.5 }}>
              FINDING MATCH
            </Typography>

            {/* Timer with speed-up icon next to it */}
            <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: -2.5 }}>
              <Typography variant="h6" className="timer" sx={{ marginRight: '0.5rem' }}>
                {formatTime(timer)}
              </Typography>

              {/* Show speed-up icon next to timer if user is in the speed-up queue */}
              {inSpeedUpQueue && (
                <Box
                  component="img"
                  src={speedupQueueIcon}
                  alt="Speed Up Queue"
                  sx={{ width: '1.5rem', height: '1.5rem', marginLeft: '0.5rem' }}
                />
              )}
            </Box>

            {/* Estimated Time */}
            <Typography className='estimated' sx={{ color: "#0AC1DC", marginTop: '0.5rem' }}>
              Estimated: 1:12
            </Typography>

            {/* Speed up modal above the timer */}
            {showSpeedUpModal && (
              <SpeedUpModal 
                show={showSpeedUpModal} 
                onClose={handleSpeedUpQueue} 
              />
            )}
          </Box>
        )}

        {/* Pass the handleChampionSelect function to the modal */}
        <SelectChampionModal open={open} handleClose={handleClose} onChampionSelect={handleChampionSelect} />
      </Box>
    </Box>
  );
};

export default FindTournament;
