import React from 'react';
import Navbar from '../components/Navbar';
import { Box, Typography } from '@mui/material';
import backgroundImage from '../assets/background-with-banner.png';
import arenaIcon from '../assets/arena-icon.png';
import PlayerIcon from '../components/PlayerIcon';
import profileAvatar from '../assets/4895.jpg';
import findMatch from '../assets/button-accept-disabled.png';
import championSelected from '../assets/champions/0.png';

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
          flexGrow: 1,
          position: 'relative', // Required for positioning elements
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
          display: 'flex',
          justifyContent: 'space-between', // Adjusted for bottom positioning
          flexDirection: 'column', // Layout as column
        }}
      >
        {/* Top content: Arena Icon and Arena Ranked */}
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
            <Box component="img" src={arenaIcon} alt="Arena" sx={{ marginLeft: 3, width: '3vw', marginTop: '1px', marginRight: 1 }} />
            <Typography
              variant="h1"
              className="headerPrimary"
              sx={{
                fontSize: '3vw',
                display: 'inline-flex',
                alignItems: 'center',
              }}
            >
              ARENA
              <Box
                component="span"
                sx={{
                  width: '0.5vw',
                  height: '0.5vw',
                  backgroundColor: '#F0E6D2',
                  borderRadius: '50%',
                  display: 'inline-block',
                  marginX: '1vw',
                }}
              />
              RANKED
            </Typography>
          </Box>
        </Box>

        {/* Centered content: profile pic, rank, etc. */}
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginTop:10
          }}
        >
          <PlayerIcon
            alt="Hide on bush"
            src={profileAvatar}
            width={6}
            height={6}
            clickable={false}
          />
          <Typography className="headerPrimary" >
            hide on bush
          </Typography>
          <Typography className="bodySecondary" >
            Diamond I
          </Typography>

          {/* Choose champion and select a champion text */}
          <Box alignSelf={'center'} display={'flex'} flexDirection={'column'} marginTop={4} alignItems={'center'}>
            <Box component="img" src={championSelected} alt="findMatch" alignSelf={'center'} justifyContent={'center'} sx={{ border: 2, borderColor: '#775A27', width: '10vh', height: '10vh' }} />
            <Typography className="bodySecondary" sx={{ marginTop: 1 }}>
              Select a Champion
            </Typography>
          </Box>
        </Box>

        {/* Bottom content: Find Match button */}
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center', // Center the button horizontally
            marginBottom: '2vh', // Slight bottom margin
          }}
        >
          <Box component="img" src={findMatch} alt="Find Match" sx={{ width: '12vw' }} />
        </Box>
      </Box>
    </Box>
  );
};

export default FindTournament;
