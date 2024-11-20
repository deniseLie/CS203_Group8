import React, { useState, useEffect } from 'react';
import { Box, Typography, Avatar, Stack, List } from '@mui/material';
import { Dashboard, SportsEsports, People } from '@mui/icons-material';
import { useLocation } from 'react-router-dom'; // Hook to access the current URL path
import SidebarItem from './SidebarItem'; // Reusable sidebar item component
import logo from '../assets/logo.png'; // Import the logo

/**
 * Sidebar Component
 *
 * This component renders a sidebar navigation menu for the application. It supports:
 * - Dynamic highlighting of the active page.
 * - Expandable/collapsible sub-menus for grouped items (e.g., Tournaments, Players).
 * - Navigation based on the current URL path.
 */
const Sidebar = () => {
  const [openMenus, setOpenMenus] = useState({}); // Tracks the open/closed state of sub-menus
  const location = useLocation(); // Get the current URL location
  const [activePage, setActivePage] = useState("Dashboard"); // Tracks the currently active page

  /**
   * Effect to update the active page based on the current URL path.
   */
  useEffect(() => {
    const path = location.pathname;

    if (path.includes('tournaments/dataset')) {
      setActivePage('Tournament Dataset'); // Highlight "Tournament Dataset" if the path matches
    } else if (path.includes('tournaments/configure')) {
      setActivePage('Configure'); // Highlight "Configure" if the path matches
    } else if (path.includes('players/dataset')) {
      setActivePage('Player Dataset'); // Highlight "Player Dataset" if the path matches
    } else if (path.includes('players/create')) {
      setActivePage('Create'); // Highlight "Create" if the path matches
    } else if (path.includes('settings')) {
      setActivePage('Settings'); // Highlight "Settings" if the path matches
    } else {
      setActivePage('Dashboard'); // Default to "Dashboard" for any other paths
    }
  }, [location]); // Re-run this effect whenever the location changes

  /**
   * Toggle the open/closed state of a sub-menu.
   *
   * @param {string} menu - The name of the menu to toggle.
   */
  const handleToggle = (menu) => {
    setOpenMenus((prev) => ({ ...prev, [menu]: !prev[menu] })); // Toggle the specified menu
  };

  return (
    <Box
      sx={{
        width: '250px', // Fixed width for the sidebar
        backgroundColor: '#0D1B2A', // Dark background color
        color: '#ffffff', // White text color
        minHeight: '100vh', // Full height of the viewport
        p: 2, // Padding inside the sidebar
      }}
    >
      {/* Logo and Application Title */}
      <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 3 }}>
        <Avatar
          src={logo} // Logo image
          alt="Tournament Logo"
          variant="square" // Square-shaped avatar
          sx={{ width: 35, height: 35 }} // Fixed size for the logo
        />
        <Typography
          variant="h5" // Heading style for the title
          sx={{ fontWeight: 'bold', color: '#b3985e' }} // Custom font color
        >
          Tournament
        </Typography>
      </Stack>

      {/* Sidebar Navigation Items */}
      <List>
        {/* Dashboard Item */}
        <SidebarItem
          text="Dashboard" // Main item label
          icon={<Dashboard />} // Icon for the item
          isActive={activePage === "Dashboard"} // Highlight if it's the active page
          onClick={() => setActivePage("Dashboard")} // Update the active page state
        />

        {/* Tournaments Item with Sub-items */}
        <SidebarItem
          text="Tournaments" // Main item label
          icon={<SportsEsports />} // Icon for the item
          subItems={['Dataset', 'Configure']} // Sub-menu items
          isOpen={openMenus['Tournaments']} // Control open/close state of the sub-menu
          onToggle={() => handleToggle('Tournaments')} // Toggle the sub-menu
          onClick={(subPage) => {
            setActivePage(subPage); // Update the active page when a sub-item is clicked
          }}
          activePage={activePage} // Pass the current active page for highlighting
        />

        {/* Players Item with Sub-items */}
        <SidebarItem
          text="Players" // Main item label
          icon={<People />} // Icon for the item
          subItems={['Dataset', 'Create']} // Sub-menu items
          isOpen={openMenus['Players']} // Control open/close state of the sub-menu
          onToggle={() => handleToggle('Players')} // Toggle the sub-menu
          onClick={(subPage) => {
            setActivePage(subPage); // Update the active page when a sub-item is clicked
          }}
          activePage={activePage} // Pass the current active page for highlighting
        />
      </List>
    </Box>
  );
};

export default Sidebar;
