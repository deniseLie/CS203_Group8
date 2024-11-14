import React, { useState } from 'react';
import { Stack, Avatar, TextField, Menu, MenuItem } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import adminProfile from '../assets/adminpfp.png';
import { useAuth } from '../context/AuthContext';

const TopBar = () => {
  const [anchorEl, setAnchorEl] = useState(null);
  const navigate = useNavigate();
  const { logout } = useAuth();

  // Open menu on avatar click
  const handleAvatarClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  // Close menu
  const handleClose = () => {
    setAnchorEl(null);
  };

  // Handle logout action
  const handleLogout = () => {
    logout(); // call logout from auth context
    navigate('/'); // redirect to login page
    handleClose();
  };

  return (
    <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 2 }}>
      <TextField placeholder="Search" variant="outlined" size="small" sx={{ width: '300px' }} />
      <Stack direction="row" alignItems="center" spacing={2}>
        <Avatar 
          src={adminProfile} 
          alt="User" 
          sx={{ width: 35, height: 35, cursor: 'pointer' }} 
          onClick={handleAvatarClick} // Open menu on click
        />
      </Stack>

      {/* Menu for Logout */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <MenuItem onClick={handleLogout}>Logout</MenuItem>
      </Menu>
    </Stack>
  );
};

export default TopBar;
