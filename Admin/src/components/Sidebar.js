import React from 'react';
import { Box, Typography, Avatar, Stack, List } from '@mui/material';
import { Dashboard, SportsEsports, People, Settings } from '@mui/icons-material';
import SidebarItem from './SidebarItem';
import logo from '../assets/logo.png';

const Sidebar = () => (
  <Box sx={{ width: '250px', backgroundColor: '#0D1B2A', color: '#ffffff', minHeight: '100vh', p: 2 }}>
    <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 3 }}>
      <Avatar src={logo} alt="Tournament Logo" variant="square" sx={{ width: 35, height: 35 }} />
      <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#b3985e' }}>
        Tournament
      </Typography>
    </Stack>
    <List>
      <SidebarItem text="Dashboard" icon={<Dashboard />} />
      <SidebarItem text="Tournaments" subItems={['Ongoing', 'Completed']} icon={<SportsEsports />} />
      <SidebarItem text="Matches" subItems={['Ongoing', 'Completed']} icon={<SportsEsports />} />
      <SidebarItem text="Players" subItems={['Player Dataset', 'Leaderboards']} icon={<People />} />
      <SidebarItem text="Settings" subItems={['Login Attempts', 'Admin Registration', 'Activity Logs', 'Profile']} icon={<Settings />} />
    </List>
  </Box>
);

export default Sidebar;
