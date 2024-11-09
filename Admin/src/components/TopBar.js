import React from 'react';
import { Stack, Avatar, TextField } from '@mui/material';
import adminProfile from '../assets/adminpfp.png'

const TopBar = () => (
  <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 2 }}>
    <TextField placeholder="Search" variant="outlined" size="small" sx={{ width: '300px' }} />
    <Stack direction="row" alignItems="center" spacing={2}>
      <Avatar src={adminProfile} alt="User" sx={{ width: 35, height: 35 }} />
    </Stack>
  </Stack>
);

export default TopBar;
