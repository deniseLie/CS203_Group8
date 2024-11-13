import React, { useState } from 'react';
import { Box, Typography, Button } from '@mui/material';
import speedupQueueIcon from '../assets/icons/speedQueue.png'; // Import the icon
import checkMark from '../assets/icons/icon-checkmark.png'; // Import the checkmark icon
import cancelIcon from '../assets/icons/cancel.png'; // Import cancel icon

const SpeedUpModal = ({ show, onClose, onJoinQueue, onLeaveQueue }) => {
  const [inSpeedUpQueue, setInSpeedUpQueue] = useState(false);

  if (!show) return null; // Hide the modal if `show` is false

  const handleJoinQueue = () => {
    setInSpeedUpQueue(true);
    onJoinQueue(); // Notify `FindTournament` that the user wants to join the Speed Up Queue
  };

  const handleCancelQueue = () => {
    setInSpeedUpQueue(false);
    onLeaveQueue(); // Notify `FindTournament` that the user wants to leave the Speed Up Queue
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      sx={{
        position: 'absolute',
        top: '-35vh',
        backgroundColor: 'rgba(0, 0, 0, 0.8)',
        padding: '1rem',
        borderRadius: '5px',
        border: '2px solid #0AC1DC',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.5)',
        width: '20vw',
        maxWidth: '600px',
        textAlign: 'center',
      }}
    >
      {/* If not in the Speed Up Queue */}
      {!inSpeedUpQueue ? (
        <>
          <Box display="flex" alignItems="center" justifyContent="center" sx={{ marginBottom: '0.5rem' }}>
            <Box component="img" src={speedupQueueIcon} alt="Speed Up Queue" sx={{ width: '2rem', height: '2rem', marginRight: '0.5rem' }} />
            <Typography className="bodyPrimary" sx={{ color: '#F0E6D2', fontSize: '1.2rem' }}>
              Join Speed Up Queue?
            </Typography>
          </Box>

          <Typography className="bodyPrimary" sx={{ color: '#949083', fontSize: '1rem', marginBottom: '1rem' }}>
            You may be matched with a wider rank range of players.
          </Typography>

          <Button
            onClick={handleJoinQueue}
            className="headerPrimary"
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              border: '2px solid #0AC1DC',
              backgroundColor: '#000',
              borderColor: "#B68C34",
              padding: '0.3rem 1rem',
              color: '#F0E6D2',
              fontSize: '1rem',
              fontWeight: 'bold',
              textTransform: 'none',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.5)',
              ':hover': { backgroundColor: '#333333' },
            }}
          >
            <Box component="img" src={checkMark} alt="Check Mark" sx={{ width: '1rem', height: '1rem', marginRight: '0.5rem' }} />
            OK
          </Button>
        </>
      ) : (
        <>
          <Box display="flex" alignItems="center" justifyContent="center" sx={{ marginBottom: '0.5rem' }}>
            <Box component="img" src={speedupQueueIcon} alt="In Speed Up Queue" sx={{ width: '2rem', height: '2rem', marginRight: '0.5rem' }} />
            <Typography className="bodyPrimary" sx={{ color: '#F0E6D2', fontSize: '1.2rem' }}>
              In Speed Up Queue
            </Typography>
          </Box>

          <Typography className="bodyPrimary" sx={{ color: '#949083', fontSize: '1rem', marginBottom: '1rem' }}>
            You may be matched with a wider rank range of players.
          </Typography>

          <Button
            onClick={handleCancelQueue}
            className="headerPrimary"
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              border: '2px solid #0AC1DC',
              backgroundColor: '#000',
              borderColor: "#B68C34",
              padding: '0.3rem 1rem',
              color: '#F0E6D2',
              fontSize: '1rem',
              fontWeight: 'bold',
              textTransform: 'none',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.5)',
              ':hover': { backgroundColor: '#333333' },
            }}
          >
            <Box component="img" src={cancelIcon} alt="Cancel Icon" sx={{ width: '1rem', height: '1rem', marginRight: '0.5rem' }} />
            CANCEL
          </Button>
        </>
      )}
    </Box>
  );
};

export default SpeedUpModal;
