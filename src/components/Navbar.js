import React from 'react';
import { Link } from 'react-router-dom';
import { Box, Typography, Avatar } from '@mui/material';

// Import your assets
import logo from '../assets/logo.png'; // Your logo image here
import playButton from '../assets/play-button-disabled.png'; // Your play button image here
import profileAvatar from '../assets/4895.jpg'; // Profile avatar image

function Navbar({ activePage }) {
  return (
    <Box
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      sx={{
        backgroundColor: '#010B13', // dark background similar to your image
        padding: '10px 20px',
        borderBottom: '2px solid #464F4D', // slight bottom border
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

        {/* HISTORY Link */}
        <Typography
          component={Link}
          to="/history"
          className="headerPrimary" // Use the headerPrimary class for default styles
          sx={{
            marginRight: 3,
            ...(activePage === 'history' && { color: '#d4b106' }), // Highlight if active
            '&:hover': {
              color: '#d4b106', // Hover color
            },
          }}
        >
          HISTORY
        </Typography>

        {/* LEADERBOARD Link */}
        <Typography
          component={Link}
          to="/leaderboard"
          className="headerPrimary" // Use the headerPrimary class for default styles
          sx={{
            ...(activePage === 'leaderboard' && { color: '#d4b106' }), // Highlight if active
            '&:hover': {
              color: '#d4b106', // Hover color
            },
          }}
        >
          LEADERBOARD
        </Typography>
      </Box>

      {/* Right side: Profile Avatar and Username */}
      <Box display="flex" alignItems="center">
        <div className="avatarWithGradientBorder">
          <Link to="/profile">
            <Avatar
              alt="Hide on bush"
              src={profileAvatar}
              sx={{ width: 50, height: 50, borderRadius: '50%' }} // Avatar remains circular
            />
          </Link>
        </div>

        <Box display={'flex'} flexDirection={'column'} sx={{ marginLeft: 3, marginRight: 2 }}>
          <Typography
            component={Link}
            to="/profile"
            className="headerPrimary" // Use the headerPrimary class for default styles
            sx={{
              '&:hover': {
                color: '#d4b106', // hover effect for username
              },
            }}
          >
            hide on bush
          </Typography>
          <Typography className="bodyPrimary">Diamond I</Typography>
        </Box>
      </Box>
    </Box>
  );
}

export default Navbar;
