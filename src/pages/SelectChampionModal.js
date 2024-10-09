import React from 'react';
import { Box, Typography, Modal } from '@mui/material';

// Manually importing all the role icons
import assassinIcon from '../assets/championClass/roleicon-assassin.png';
import fighterIcon from '../assets/championClass/roleicon-fighter.png';
import mageIcon from '../assets/championClass/roleicon-mage.png';
import marksmanIcon from '../assets/championClass/roleicon-marksman.png';
import supportIcon from '../assets/championClass/roleicon-support.png';
import tankIcon from '../assets/championClass/roleicon-tank.png';

const roleIcons = [
  { src: assassinIcon, alt: 'Assassin' },
  { src: fighterIcon, alt: 'Fighter' },
  { src: mageIcon, alt: 'Mage' },
  { src: marksmanIcon, alt: 'Marksman' },
  { src: supportIcon, alt: 'Support' },
  { src: tankIcon, alt: 'Tank' },
];

const SelectChampionModal = ({ open, handleClose }) => {
  return (
    <Modal
      open={open} // Modal is open based on prop
      onClose={handleClose} // Close the modal when clicking outside or pressing ESC
      aria-labelledby="champion-modal-title"
      aria-describedby="champion-modal-description"
    >
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: 500,
          bgcolor: '#010A13',
          border: '2px solid #775A27',
          boxShadow: 24,
          p: 4,
        }}
      >
        <Typography id="champion-modal-title" className="headerPrimary" component="h2">
          CHOOSE YOUR CHAMPION
        </Typography>

        {/* Iterate through role icons */}
        <Box
          sx={{
            display: 'flex',
            flexDirection:'row',
            marginTop: 4,
          }}
        >
          {roleIcons.map((icon, index) => (
            <Box
              key={index}
              component="img"
              src={icon.src}
              alt={icon.alt}
              sx={{
                width: '4vh',
                height: '4vh',
                cursor: 'pointer',
                transition: 'all 0.1s ease-in-out', // Smooth transition effect
                '&:hover': {
                  filter: 'brightness(0) invert(1)', // Change color to white on hover
                  borderColor: '#F0E6D2', // Highlight on hover
                },
                marginRight: 3
              }}
            />
          ))}
        </Box>
{/* 
        <Box sx={{border:1, borderColor:'#775A27'}}>
            <Typography className='bodyPrimary'>Sort by Name</Typography>
        </Box> */}
      </Box>
    </Modal>
  );
};

export default SelectChampionModal;
