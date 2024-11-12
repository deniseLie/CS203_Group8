import React, { useState } from 'react';
import { Box, Typography, Button } from '@mui/material';
import speedupQueueIcon from '../assets/icons/speedQueue.png'; // Import the icon
import checkMark from '../assets/icons/icon-checkmark.png'; // Import the checkmark icon
import cancelIcon from '../assets/icons/cancel.png'; // Import cancel icon

const SpeedUpModal = ({ show, onClose }) => {
  const [inSpeedUpQueue, setInSpeedUpQueue] = useState(false); // New state to track if in Speed Up Queue

  if (!show) return null; // If not showing, return null to hide the component

  const handleJoinQueue = () => {
    setInSpeedUpQueue(true); // User joins the Speed Up Queue
  };

  const handleCancelQueue = () => {
    setInSpeedUpQueue(false); // User cancels the Speed Up Queue
    // onClose(); // Close the modal
  };

  return (
    <Box
      display={"flex"} 
      flexDirection={"column"} 
      alignItems={'center'} 
      justifyContent={'center'}
      sx={{
        position: 'absolute',
        top: '-35vh', // Adjust the position based on your layout
        backgroundColor: 'rgba(0, 0, 0, 0.8)', // Dark background with transparency
        padding: '1rem',
        borderRadius: '5px',
        border: '2px solid #0AC1DC',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.5)',
        width: '20vw', // Adjust the width to your preference
        maxWidth: '600px',
        textAlign: 'center', // Center the text inside the modal
      }}
    >
      {/* If not in the Speed Up Queue */}
      {!inSpeedUpQueue ? (
        <>
          {/* Icon and Text */}
          <Box display="flex" alignItems="center" justifyContent="center" sx={{ marginBottom: '0.5rem' }}>
            {/* Icon */}
            <Box component="img" src={speedupQueueIcon} alt="Speed Up Queue" sx={{ width: '2rem', height: '2rem', marginRight: '0.5rem' }} />
            
            {/* Text */}
            <Typography className='bodyPrimary' sx={{ color: '#F0E6D2', fontSize: '1.2rem' }}>
              Join Speed Up Queue?
            </Typography>
          </Box>

          <Typography className='bodyPrimary' sx={{ color: '#949083', fontSize: '1rem', marginBottom: '1rem' }}>
            You may be matched with a wider rank range of players.
          </Typography>
          
          {/* OK Button with Checkmark */}
          <Button
            onClick={handleJoinQueue} // On click, join Speed Up Queue
            className='headerPrimary'
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              border: '2px solid #0AC1DC',
              backgroundColor: '#000',
              borderColor:"#B68C34",
              padding: '0.3rem 1rem',
              color: '#F0E6D2',
              fontSize: '1rem',
              fontWeight: 'bold',
              textTransform: 'none',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.5)',
              ':hover': {
                backgroundColor: '#333333',
              }
            }}
          >
            {/* Checkmark icon */}
            <Box component="img" src={checkMark} alt="Check Mark" sx={{ width: '1rem', height: '1rem', marginRight: '0.5rem' }} />
            OK
          </Button>
        </>
      ) : (
        <>
          {/* If in the Speed Up Queue */}
          <Box display="flex" alignItems="center" justifyContent="center" sx={{ marginBottom: '0.5rem' }}>
            <Box component="img" src={speedupQueueIcon} alt="In Speed Up Queue" sx={{ width: '2rem', height: '2rem', marginRight: '0.5rem' }} />
            <Typography className='bodyPrimary' sx={{ color: '#F0E6D2', fontSize: '1.2rem' }}>
              In Speed Up Queue
            </Typography>
          </Box>

          <Typography className='bodyPrimary' sx={{ color: '#949083', fontSize: '1rem', marginBottom: '1rem' }}>
            You may be matched with a wider rank range of players.
          </Typography>

          {/* CANCEL Button with Cross Icon */}
          <Button
            onClick={handleCancelQueue} // On click, cancel Speed Up Queue
            className='headerPrimary'
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              border: '2px solid #0AC1DC',
              backgroundColor: '#000',
              borderColor:"#B68C34",
              padding: '0.3rem 1rem',
              color: '#F0E6D2',
              fontSize: '1rem',
              fontWeight: 'bold',
              textTransform: 'none',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.5)',
              ':hover': {
                backgroundColor: '#333333',
              }
            }}
          >
            {/* Cancel icon */}
            <Box component="img" src={cancelIcon} alt="Cancel Icon" sx={{ width: '1rem', height: '1rem', marginRight: '0.5rem' }} />
            CANCEL
          </Button>
        </>
      )}
    </Box>
  );
};

export default SpeedUpModal;
