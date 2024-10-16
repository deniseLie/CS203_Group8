import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { Box, Typography, IconButton } from '@mui/material';
import HighlightOffIcon from '@mui/icons-material/HighlightOff'; // Import the icon
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


const FindTournament = ({ logout }) => {
  const [open, setOpen] = useState(false); // State to control modal open/close
  const [selectedChampion, setSelectedChampion] = useState(null); // State for selected champion
  const [inQueueState, setInQueueState] = useState(false); // New state to track if the user is in queue
  const [timer, setTimer] = useState(0); // Timer state for tracking seconds

  // Function to handle Find Match button click
  const handleFindMatchClick = () => {
    if (selectedChampion) {
      setInQueueState(true); // Set the state to inQueue when clicked
      setTimer(0); // Reset the timer when entering the queue
    }
  };

  const handleOpen = () => setOpen(true); // Function to open modal
  const handleClose = () => setOpen(false); // Function to close modal

  // Function to update the selected champion
  const handleChampionSelect = (champion) => {
    setSelectedChampion(champion); // Update the selected champion
    handleClose(); // Close the modal after selection
  };

  // Function to handle canceling the queue (reset inQueueState)
  const handleCancelQueue = () => {
    setInQueueState(false); // Reset the queue state
    setTimer(0); // Reset timer when leaving the queue
  };

  // Timer Effect: Start the timer when inQueueState is true
  useEffect(() => {
    let interval;
    if (inQueueState) {
      interval = setInterval(() => {
        setTimer(prevTime => prevTime + 1); // Increment timer by 1 second
      }, 1000);
    } else {
      clearInterval(interval); // Clear the interval if not in queue
    }

    return () => clearInterval(interval); // Cleanup interval on component unmount
  }, [inQueueState]);

  // Helper function to format time as mm:ss
  const formatTime = (timeInSeconds) => {
    const minutes = Math.floor(timeInSeconds / 60);
    const seconds = timeInSeconds % 60;
    return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
  };

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column', // Vertical layout
        height: '100vh', // Take up full screen height
        overflow: 'hidden', // Prevent scrollbar from appearing
      }}
    >
      {/* Navbar */}
      <Box
        sx={{
          flexShrink: 0, // Prevent navbar from shrinking
          zIndex: 100,
        }}
      >
        <Navbar logout={logout} />
      </Box>

      {/* Main content area */}
      <Box
        sx={{
          flexGrow: 1,
          position: 'relative', // Required for positioning elements
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
          display: 'flex',
          justifyContent: 'space-between', // Adjusted for bottom positioning
          flexDirection: 'column', // Layout as column
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
            <Typography
              variant="h1"
              className="headerPrimary"
              sx={{
                fontSize: '3vw',
                display: 'inline-flex',
                alignItems: 'center',
              }}
            >
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
          {/* replace with API later */}
          <Typography className="headerPrimary">
            hide on bush
          </Typography>
          <Box display={'flex'} alignItems={'center'}>
            <Box component="img" src={diamondRank} alt="Rank" sx={{ width: '3vh', height: '3vh', marginRight: 1 }} />

            <Typography className="bodySecondary">
              Diamond I
            </Typography>
          </Box>
          {/* Choose champion and select a champion text */}
          <Box alignSelf={'center'} display={'flex'} flexDirection={'column'} marginTop={4} alignItems={'center'}>
            {/* Add onClick to open modal */}
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
                cursor: 'pointer', // Make it clear this is clickable
              }}
              onClick={handleOpen} // Open the modal on click
            />
            <Typography className="bodySecondary" sx={{ marginTop: 1 }}>
              {selectedChampion ? selectedChampion.name : 'Select a Champion'}
            </Typography>
          </Box>
        </Box>

        {/* Bottom content: Find Match button and optional cancel icon */}
        <Box
          sx={{
            position: 'relative', // Position the container relatively
            display: 'flex',
            justifyContent: 'center', // Center the button horizontally
            marginBottom: '2vh', // Slight bottom margin
            alignItems: 'center', // Vertically align the cancel icon
          }}
        >
          {inQueueState && (
            <IconButton
              onClick={handleCancelQueue} // Cancel the queue when clicked
              sx={{
                position: 'absolute',
                left: 'calc(50% - 11vw)', // Adjust left position of the icon relative to the center
              }}
            >
              <HighlightOffIcon fontSize="large" sx={{ color: "#D8A13A" }} />
            </IconButton>
          )}

          <Box
            component="img"
            src={selectedChampion ? (inQueueState ? inQueue : findMatch) : findMatchDisabled}
            alt="Find Match"
            sx={{ width: '14vw', cursor: selectedChampion ? 'pointer' : 'not-allowed' }} // Change cursor when clickable
            onClick={selectedChampion && !inQueueState ? handleFindMatchClick : null} // Only handle click if not in queue and a champion is selected
          />
        </Box>

        {/* Timer and "Finding Match" Text */}
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
            <Typography variant="h6" className='findMatch'>
              FINDING MATCH
            </Typography>
            <Typography variant="h6" className="timer" sx={{marginTop:-2}}>
              {formatTime(timer)}
            </Typography>
            <Typography className='estimated' sx={{color:"#0AC1DC", marginTop: -1.5}}>
              Estimated: 1:12
            </Typography>
          </Box>
        )}

        {/* Pass the handleChampionSelect function to the modal */}
        <SelectChampionModal open={open} handleClose={handleClose} onChampionSelect={handleChampionSelect} />
      </Box>
    </Box>
  );
};

export default FindTournament;
