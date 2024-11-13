import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Box, Typography, Popover } from '@mui/material';
import logo from '../assets/icons/logo.png';
import playButton from '../assets/buttons/play-button-disabled.png';
import profileAvatar from '../assets/summonerIcon/1.jpg';
import PlayerIcon from './PlayerIcon';
import { useAuth } from '../auth/AuthProvider';

function Navbar({  activePage }) {
  const [anchorEl, setAnchorEl] = useState(null);


  const { user, logout } = useAuth();

  // Handle click to open the popover
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
        zIndex: 100,
      }}
    >
      {/* Left side: Logo and Play Button */}
      <Box display="flex" alignItems="center">
        <Box display="flex" alignItems="center">
          <Link to="/">
            <Box component="img" src={logo} alt="Logo" sx={{ marginLeft: 3, width: 45, marginRight: -1.5 }} />
          </Link>
          <Link to="/">
            <Box component="img" src={playButton} alt="Play" sx={{ width: 150, marginRight: 3 }} />
          </Link>
        </Box>

        <Typography
          component={Link}
          to="/history"
          className="headerPrimary"
          sx={{
            marginRight: 3,
            ...(activePage === 'history' && { color: '#d4b106' }),
            '&:hover': {
              color: '#d4b106',
            },
            marginLeft: '20px',
          }}
        >
          HISTORY
        </Typography>

      </Box>

      {/* Right side: Profile Avatar, Name, and Rank */}
      <Box
        display="flex"
        alignItems="center"
        onClick={handleClick}
        sx={{
          cursor: 'pointer',
          textDecoration: 'none',
          outline: 'none',
          backgroundColor: 'transparent',
        }}
      >
        <PlayerIcon
          alt={user? user.playername : "Profile"}
          src={profileAvatar}
          width={2}
          height={2}
          link="/profile"
          clickable={false}
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
            {user ? user.playername : ""}
          </Typography>
          <Typography className="bodyPrimary">{user ? user.rank : "Unranked"}</Typography>
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
            minWidth:'10vw',
            display: 'flex',
            alignItems:'center',
            flexDirection: 'column',
            gap: 1,
            backgroundColor: '#010b13',
          }}
        >
          <Typography
            className='headerPrimary'
            component={Link}
            to={"/profile"}
            sx={{textDecoration:'none', 
              '&:hover': {
                color: '#d4b106',
              },
              cursor: 'pointer',}}
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
              handleClose();
              logout();
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
