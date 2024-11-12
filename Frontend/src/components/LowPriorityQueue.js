// LowPriorityQueue.js
import React from 'react';
import { Box, Typography } from '@mui/material';
import redWarning from '../assets/icons/red-warning.png'; // Adjust the path if needed

const LowPriorityQueue = () => {
  return (
    <Box
      sx={{
        padding: '1rem',
        backgroundColor: 'rgba(0,0,0,0.7)',
        border: '1px solid #D0342C',
        borderRadius: '8px',
        color: '#F0E6D2',
        maxWidth: '20vw',
        marginBottom: '1rem', // Add space above timer
        textAlign: 'center',
      }}
    >
      <Box display={'flex'} alignItems={'center'} justifyContent={'center'}>
        <Box
          component="img"
          src={redWarning}
          alt="Warning"
          sx={{ width: '1rem', height: '1rem', marginBottom: '0.5rem', marginRight: '0.5rem' }}
        />
        <Typography sx={{ fontWeight: 'bold', color: '#D0342C', mb: 1 }} className='headerPrimary'>
          LOW PRIORITY QUEUE
        </Typography>
      </Box>

      <Typography variant="body2" className='bodyPrimary'>
        Abandoning a match (or going AFK) makes the match unfair for your teammates and carries a penalty in League.
        You've been placed in a lower priority queue.
      </Typography>
    </Box>
  );
};

export default LowPriorityQueue;
