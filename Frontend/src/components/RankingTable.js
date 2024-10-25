import React from 'react';
import { Box, Typography, Avatar } from '@mui/material';

function RankingTable({ rankings }) {
  return (
    <Box sx={{ backgroundColor: '#1c1c1c', padding: 3 }}>
      {rankings.map((rank, index) => (
        <Box key={index} display="flex" alignItems="center" justifyContent="space-between" sx={{ borderBottom: '1px solid #333', padding: 2 }}>
          <Box display="flex" alignItems="center">
            <Typography sx={{ color: '#fff', fontWeight: 'bold', marginRight: 2 }}>{`${rank.position}TH`}</Typography>
            <Avatar src={rank.champion.image} alt={rank.champion.name} sx={{ marginRight: 2, width: 40, height: 40 }} />
            <Typography sx={{ color: '#fff' }}>{rank.player}</Typography>
          </Box>
          <Typography sx={{ color: '#fff' }}>{rank.kda} KDA</Typography>
        </Box>
      ))}
    </Box>
  );
}

export default RankingTable;
