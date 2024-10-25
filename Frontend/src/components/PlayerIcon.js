import React, { useState } from 'react';
import { Avatar, Popover, Box, Typography } from '@mui/material'; 
import { Link } from 'react-router-dom'; 

const PlayerIcon = ({
  alt = "Player Avatar", 
  src, 
  width = 50, 
  height = 50, 
  link = "/profile", 
  clickable = true, 
  className = "",
}) => {
  const [anchorEl, setAnchorEl] = useState(null); // State to control the popover

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget); // Open the popover on avatar click
  };

  const handleClose = () => {
    setAnchorEl(null); // Close the popover
  };

  const open = Boolean(anchorEl); // Determine if the popover is open
  const id = open ? 'player-popover' : undefined;

  const outerStyle = {
    width: `${width + 8}vh`,  // Add 8vh to account for the border/padding
    height: `${height + 8}vh`, // Add 8vh to account for the border/padding
  };

  return (
    <div className={`avatarWithGradientBorder ${className}`} style={outerStyle}>
      {clickable ? (
        <>
          {/* Avatar that triggers the popover */}
          <Avatar
            alt={alt}
            src={src}
            onClick={handleClick} // Open the popover when the avatar is clicked
            sx={{
              width: `${width}vh`, 
              height: `${height}vh`,
              borderRadius: '50%',
              cursor: 'pointer', // Make it clear that the avatar is clickable
            }}
          />

          {/* Popover content */}
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
            <Box sx={{ p: 2, backgroundColor: '#010b13', color: '#fff' }}>
              <Typography variant="body1" sx={{ mb: 1 }}>
                Profile Options
              </Typography>
              <Link to={link} style={{ textDecoration: 'none', color: '#d4b106' }}>
                <Typography sx={{ cursor: 'pointer', '&:hover': { color: '#f0c674' } }}>
                  Go to Profile
                </Typography>
              </Link>
              <Typography
                sx={{
                  mt: 2,
                  cursor: 'pointer',
                  color: '#ff4f4f',
                  '&:hover': { color: '#ff1a1a' },
                }}
                onClick={() => {
                  handleClose();
                  // Add additional logout logic here if needed
                  console.log('Logged out');
                }}
              >
                Log Out
              </Typography>
            </Box>
          </Popover>
        </>
      ) : (
        <Avatar
          alt={alt}
          src={src}
          sx={{
            width: `${width}vh`, 
            height: `${height}vh`,
            borderRadius: '50%',
          }}
        />
      )}
    </div>
  );
};

export default PlayerIcon;
