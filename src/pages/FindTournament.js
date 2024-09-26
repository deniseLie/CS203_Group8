import React from 'react';
import Navbar from '../components/Navbar';
import { Box, Typography } from '@mui/material';
import backgroundImage from '../assets/arena-background.jpg'; // Correct import
import arenaIcon from '../assets/arena-icon.png'; // Correct import
import diamondBanner from '../assets/rankedBorder/diamond.png'; // Import the diamond banner image

const FindTournament = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column', // Vertical layout
        height: '100vh', // Take up full screen height
        overflow: 'hidden', // Prevent scrollbar from appearing
      }}
    >
      {/* Navbar */}
      <Box
        sx={{
          flexShrink: 0, // Prevent navbar from shrinking
          zIndex: 100,
        }}
      >
        <Navbar />
      </Box>

      {/* Main content area */}
      <Box
        sx={{
          flexGrow: 1, // Allow this area to take up the remaining space
          position: 'relative', // Required for positioning elements
          backgroundImage: `url(${backgroundImage})`, // Set background image
          backgroundSize: 'cover', // Ensure image covers the entire area
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        {/* Arena Icon and Arena Ranked */}
        <Box
          sx={{
            position: 'absolute',
            top: 16,
            left: 16,
            zIndex: 2,
            display: 'flex',
            alignItems: 'center',
          }}
        >
          <Box display="flex" alignItems="center">
            <Box component="img" src={arenaIcon} alt="Arena" sx={{ marginLeft: 3, width: 30, marginTop: '1px', marginRight: 1 }} />
            <Typography
              variant="h1"
              className="headerPrimary"
              sx={{
                display: 'inline-flex',
                alignItems: 'center',
              }}
            >
              ARENA
              <Box
                component="span"
                sx={{
                  width: '4px',
                  height: '4px',
                  backgroundColor: '#F0E6D2',
                  borderRadius: '50%',
                  display: 'inline-block',
                  marginX: '8px',
                }}
              />
              RANKED
            </Typography>
          </Box>
        </Box>

        {/* Centered Banner */}
        <Box
          component="img"
          src={diamondBanner}
          alt="Diamond Banner"
          sx={{
            width: '300px', // Adjust size as needed
            transform: 'translate(0%,-35%)',
            zIndex: 2,
          }}
        />
      </Box>
    </Box>
  );
};

export default FindTournament;
