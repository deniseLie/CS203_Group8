import React, { useState, useEffect } from 'react';
import { Box, Typography, Avatar, Stack, List } from '@mui/material';
import { Dashboard, SportsEsports, People, Settings } from '@mui/icons-material';
import { useLocation } from 'react-router-dom';
import SidebarItem from './SidebarItem';
import logo from '../assets/logo.png';

const Sidebar = () => {
  const [openMenus, setOpenMenus] = useState({});
  const location = useLocation(); // Get the current location
  const [activePage, setActivePage] = useState("Dashboard");

  // Effect to set active page based on the current location
  useEffect(() => {
    const path = location.pathname;
    if (path.includes('tournaments/ongoing')) {
      setActivePage('Ongoing');
    } else if (path.includes('tournaments/completed')) {
      setActivePage('Completed');
    } else if (path.includes('tournaments/configure')) {
        setActivePage('Configure');
    } else if (path.includes('tournaments/add')) {
        setActivePage('Add');
    } else if (path.includes('players/dataset')) {
      setActivePage('Dataset');
    } else if (path.includes('players/leaderboards')) {
      setActivePage('Leaderboards');
    } else if (path.includes('players/create')) {
      setActivePage('Create');
    } else if (path.includes('settings')) {
      setActivePage('Settings');
    } else {
      setActivePage('Dashboard');
    }
  }, [location]);

  const handleToggle = (menu) => {
    setOpenMenus((prev) => ({ ...prev, [menu]: !prev[menu] }));
  };

  return (
    <Box sx={{ width: '250px', backgroundColor: '#0D1B2A', color: '#ffffff', minHeight: '100vh', p: 2 }}>
      <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 3 }}>
        <Avatar src={logo} alt="Tournament Logo" variant="square" sx={{ width: 35, height: 35 }} />
        <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#b3985e' }}>
          Tournament
        </Typography>
      </Stack>
      <List>
        <SidebarItem
          text="Dashboard"
          icon={<Dashboard />}
          isActive={activePage === "Dashboard"}
          onClick={() => setActivePage("Dashboard")}
        />
        <SidebarItem
          text="Tournaments"
          icon={<SportsEsports />}
          subItems={['Ongoing', 'Completed', 'Configure', 'Add']}
          isOpen={openMenus['Tournaments']}
          onToggle={() => handleToggle('Tournaments')}
          onClick={(subPage) => {
            setActivePage(subPage);
            // handle navigation logic here based on subPage
          }}
          activePage={activePage}
        />
        {/* <SidebarItem
          text="Matches"
          icon={<SportsEsports />}
          subItems={['Ongoing', 'Completed']}
          isOpen={openMenus['Matches']}
          onToggle={() => handleToggle('Matches')}
          onClick={(subPage) => {
            setActivePage(`Matches ${subPage}`);
            // handle navigation logic here based on subPage
          }}
          activePage={activePage}
        /> */}
        <SidebarItem
          text="Players"
          icon={<People />}
          subItems={['Dataset', 'Leaderboards', 'Create']}
          isOpen={openMenus['Players']}
          onToggle={() => handleToggle('Players')}
          onClick={(subPage) => {
            setActivePage(subPage);
            // handle navigation logic here based on subPage
          }}
          activePage={activePage}
        />
        <SidebarItem
          text="Settings"
          icon={<Settings />}
          subItems={['Login Attempts', 'Admin Registration', 'Activity Logs', 'Profile']}
          isOpen={openMenus['Settings']}
          onToggle={() => handleToggle('Settings')}
          onClick={(subPage) => {
            setActivePage(subPage);
            // handle navigation logic here based on subPage
          }}
          activePage={activePage}
        />
      </List>
    </Box>
  );
};

export default Sidebar;
