import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Box, Typography, Avatar, Popover } from '@mui/material';
import logo from '../assets/logo.png'; // Your logo image here
import playButton from '../assets/play-button-disabled.png'; // Your play button image here
import profileAvatar from '../assets/4895.jpg'; // Profile avatar image

function Navbar({ logout }) {
  const [anchorEl, setAnchorEl] = useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? 'profile-popover' : undefined;

  return (
    <Box
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      sx={{
        backgroundColor: '#010B13',
        padding: '10px 20px',
        borderBottom: '2px solid #464F4D',
      }}
    >
      {/* Left side: Logo and Play Button */}
      <Box display="flex" alignItems="center">
        <Box display="flex" alignItems="center">
          {/* Logo */}
          <Link to="/">
            <Box component="img" src={logo} alt="Logo" sx={{ width: 45, marginRight: -1.5 }} />
          </Link>

          {/* Play Button */}
          <Link to="/play">
            <Box component="img" src={playButton} alt="Play" sx={{ width: 150, marginRight: 3 }} />
          </Link>
        </Box>

        <Typography
          component={Link}
          to="/history"
          className="headerPrimary"
          sx={{
            '&:hover': {
              color: '#d4b106',
            },
          }}
        >
          HISTORY
        </Typography>
      </Box>

      {/* Right side: Profile Avatar and Username */}
      <Box display="flex" alignItems="center" onClick={handleClick}>
        <div className="avatarWithGradientBorder">
          <Avatar
            alt="Hide on bush"
            src={profileAvatar}
            sx={{ width: 50, height: 50, borderRadius: '50%', cursor: 'pointer' }}
          />
        </div>

        <Box display="flex" flexDirection="column" sx={{ marginLeft: 3, marginRight: 2 }}>
          <Typography
            component={Link}
            className="headerPrimary"
            sx={{
              '&:hover': {
                color: '#d4b106',
              },
            }}
          >
            hide on bush
          </Typography>
          <Typography className="bodyPrimary">Diamond I</Typography>
        </Box>
      </Box>

      {/* Popover for profile options */}
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'center',
        }}
      >
        <Box
          sx={{
            padding: 2,
            display: 'flex',
            flexDirection: 'column',
            gap: 1,
            backgroundColor: '#010b13',
          }}
        >
          <Typography
            className="headerPrimary"
            sx={{
              '&:hover': {
                color: '#d4b106',
              },
              cursor: 'pointer', // Make it clickable
            }}
            onClick={() => {
              handleClose(); // Close the popover first
              logout(); // Call the logout function
            }}
          >
            Sign Out
          </Typography>
        </Box>
      </Popover>
    </Box>
  );
}

export default Navbar;
