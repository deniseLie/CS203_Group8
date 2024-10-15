import React, { useState } from 'react';
import Navbar from '../components/Navbar';
import { Box, Typography } from '@mui/material';
import backgroundImage from '../assets/background-with-banner.png';
import arenaIcon from '../assets/arena-icon.png';
import PlayerIcon from '../components/PlayerIcon';
import profileAvatar from '../assets/4895.jpg';
import findMatch from '../assets/button-accept-disabled.png';
import championSelected from '../assets/champions/0.png';
import SelectChampionModal from './SelectChampionModal';
import diamondRank from '../assets/rankIcon/diamond.png';


const FindTournament = ({logout}) => {
  const [open, setOpen] = useState(false); // State to control modal open/close
  const [selectedChampion, setSelectedChampion] = useState(null); // State for selected champion

  const handleOpen = () => setOpen(true); // Function to open modal
  const handleClose = () => setOpen(false); // Function to close modal

  // Function to update the selected champion
  const handleChampionSelect = (champion) => {
    setSelectedChampion(champion); // Update the selected champion
    handleClose(); // Close the modal after selection
  };

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
        <Navbar logout={logout}/>
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
            marginTop: 10
          }}
        >
          <PlayerIcon
            alt="Hide on bush"
            src={profileAvatar}
            width={6}
            height={6}
            clickable={false}
          />
          {/* replace with API later */}
          <Typography className="headerPrimary">
            hide on bush
          </Typography>
          <Box display={'flex'} alignItems={'center'}>
            <Box component="img" src={diamondRank} alt="Rank" sx={{width:'3vh', height:'3vh', marginRight: 1}} />

          <Typography className="bodySecondary">
            Diamond I
          </Typography>
          </Box>
          {/* Choose champion and select a champion text */}
          <Box alignSelf={'center'} display={'flex'} flexDirection={'column'} marginTop={4} alignItems={'center'}>
            {/* Add onClick to open modal */}
            <Box
              component="img"
              src={selectedChampion ? selectedChampion.src : championSelected}
              alt="Select Champion"
              alignSelf={'center'}
              justifyContent={'center'}
              sx={{
                border: 2,
                borderColor: '#775A27',
                width: '10vh',
                height: '10vh',
                cursor: 'pointer', // Make it clear this is clickable
              }}
              onClick={handleOpen} // Open the modal on click
            />
            <Typography className="bodySecondary" sx={{ marginTop: 1 }}>
              {selectedChampion ? selectedChampion.name : 'Select a Champion'}
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
          <Box component="img" src={findMatch} alt="Find Match" sx={{ width: '14vw' }} />
        </Box>

        {/* Pass the handleChampionSelect function to the modal */}
        <SelectChampionModal open={open} handleClose={handleClose} onChampionSelect={handleChampionSelect} />
      </Box>
    </Box>
  );
};

export default FindTournament;
