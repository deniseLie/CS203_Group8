import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Box, Typography, Popover, Button } from '@mui/material';
import logo from '../assets/logo.png'; // Your logo image here
import playButton from '../assets/play-button-disabled.png'; // Your play button image here
import profileAvatar from '../assets/summonerIcon/4895.jpg'; // Profile avatar image
import PlayerIcon from './PlayerIcon';

function Navbar({ logout, activePage }) {
  const [anchorEl, setAnchorEl] = useState(null);

  // Handle click to open the popover
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget); // Set the anchor element (the clicked Box)
  };

  const handleClose = () => {
    setAnchorEl(null); // Close the popover
  };

  const open = Boolean(anchorEl); // Determine if the popover is open
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
        zIndex: 100,
      }}
    >
      {/* Left side: Logo and Play Button */}
      <Box display="flex" alignItems="center">
        <Box display="flex" alignItems="center">
          {/* Logo */}
          <Link to="/">
            <Box component="img" src={logo} alt="Logo" sx={{ marginLeft: 3, width: 45, marginRight: -1.5 }} />
          </Link>

          {/* Play Button */}
          <Link to="/">
            <Box component="img" src={playButton} alt="Play" sx={{ width: 150, marginRight: 3 }} />
          </Link>
        </Box>

        {/* History link */}
        <Typography
          component={Link}
          to="/history"
          className="headerPrimary"
          sx={{
            marginRight: 3,
            ...(activePage === 'history' && { color: '#d4b106' }), // Highlight if active
            '&:hover': {
              color: '#d4b106',
            },
            marginLeft: '20px', // Add some spacing if needed
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
      {/* Right side: Profile Avatar, Name, and Rank */}
      <Box
        display="flex"
        alignItems="center"
        
        onClick={handleClick} // Trigger the popover when this section is clicked
        sx={{ cursor: 'pointer',
        textDecoration: 'none', // Remove any text decoration
        outline: 'none', // Remove any outline that might appear on click
        backgroundColor: 'transparent',}} // Make sure no background color is added on click }} // Make the profile section clickable
        
      >
        <PlayerIcon
          alt="Hide on bush"
          src={profileAvatar}
          width={2}
          height={2}
          link="/profile"
          clickable={false} // Disable direct linking since the whole section is clickable
        />

        <Box display="flex" flexDirection="column" sx={{ marginLeft: 3, marginRight: 2 }}>
          <Typography
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
          className='headerPrimary'
          component={Link}
          to={"/profile"}
          sx={{textDecoration:'none', color:''}}
          >
           Profile
          </Typography>
          <Typography
            className="headerPrimary"
            sx={{
              '&:hover': {
                color: '#d4b106',
              },
              cursor: 'pointer',
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
