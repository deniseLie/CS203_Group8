import React from 'react';
import { Box, Typography, Avatar } from '@mui/material';

function GameCard({ rank, champion, kda, lpChange, time }) {
  return (
    <Box 
      display="flex" 
      alignItems="center" 
      justifyContent="space-between" 
      sx={{ 
        position: 'relative',
        padding: 1,
        '&:before, &:after': {
          content: '""',
          position: 'absolute',
          left: 0,
          right: 0,
          height: '1px',
          background: 'linear-gradient(to right, transparent, rgba(100, 100, 100, 0.8), rgba(100, 100, 100, 1), rgba(100, 100, 100, 0.8), transparent)', // Darker gray
        },
        '&:before': {
          top: 0,  // Top border
        },
        '&:after': {
          bottom: 0,  // Bottom border
        },
      }}
    >
      <Box display="flex" alignItems="center">
        <Typography variant="h6" sx={{ color: rank === 1 ? '#ffd700' : '#fff', fontWeight: 'bold' }}>
          {rank === 1 ? '1ST' : `${rank}TH`}
        </Typography>
        <Avatar src={champion.image} alt={champion.name} sx={{ marginLeft: 2, width: 50, height: 50 }} />
        <Typography sx={{ marginLeft: 2, color: '#fff', fontWeight: 'bold' }}>{champion.name}</Typography>
      </Box>
      <Box display="flex" alignItems="center">
        <Typography sx={{ color: lpChange > 0 ? '#28a745' : '#dc3545', fontWeight: 'bold', marginRight: 2 }}>
          {lpChange > 0 ? `+${lpChange} LP` : `${lpChange} LP`}
        </Typography>
        <Typography sx={{ color: '#fff' }}>{kda} KDA</Typography>
        <Typography sx={{ marginLeft: 2, color: '#bbb' }}>{time}</Typography>
      </Box>
    </Box>
  );
}

export default GameCard;
