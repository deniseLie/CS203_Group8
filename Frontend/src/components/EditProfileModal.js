import React, { useState } from 'react';
import { Dialog, DialogContent, TextField, Button, Box, Typography, IconButton, InputAdornment } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import icon28 from '../assets/summonerIcon/28.jpg';
import icon3599 from '../assets/summonerIcon/3599.jpg';
import icon4614 from '../assets/summonerIcon/4614.jpg';
import icon4895 from '../assets/summonerIcon/4895.jpg';
import icon5257 from '../assets/summonerIcon/5257.jpg';
import icon6104 from '../assets/summonerIcon/6104.jpg';
import icon6365 from '../assets/summonerIcon/6365.jpg';
import icon6631 from '../assets/summonerIcon/6631.jpg';

const EditProfileModal = ({
  open,
  handleClose,
  username,
  setUsername,
  email,
  setEmail,
  password,
  setPassword,
  avatar,
  setAvatar,
  handleSaveChanges
}) => {
  const avatars = [
    icon28, icon3599, icon4614, icon4895, icon5257, icon6104, icon6365, icon6631
  ];

  // State for visibility toggling
  const [showPassword, setShowPassword] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  // State for password matching error
  const [passwordError, setPasswordError] = useState('');

  // State for editing mode
  const [isEditing, setIsEditing] = useState(false);

  // Handler for Save Changes button
  const handleSave = () => {
    setIsEditing(false);
    if (password !== confirmPassword) {
      setPasswordError("Passwords do not match");
      return;
    }
    setPasswordError('');
    handleSaveChanges();
  };

  return (
    <Dialog fullWidth maxWidth="md" open={open} onClose={handleClose}>
      <DialogContent sx={{ background: '#010A13', border: 2, borderColor: "#775A27", display: 'flex', padding: '16px 32px', flexDirection: 'column' }}>
        <Box display={'flex'} justifyContent={'flex-end'}>
          <IconButton onClick={handleClose} sx={{ color: '#f0e6d2' }}>
            <CloseIcon />
          </IconButton>
        </Box>
        <Box sx={{ display: 'flex' }}>
          {/* Left Section */}
          <Box sx={{ width: '50%', paddingRight: 2, borderRight: 1.5, borderRightColor: '#464F4D' }}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                {/* Player Avatar */}
                <Box
                  sx={{
                    width: '5w',
                    height: '5vw',
                    borderRadius: '50%',
                    overflow: 'hidden',
                    border: avatar ? '2px solid #C8AA6C' : 'none',
                    cursor: 'pointer'
                  }}
                  onClick={() => setAvatar(avatar)}
                >
                  <img
                    src={avatar}
                    alt="Avatar"
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover',
                    }}
                  />
                </Box>
              </Box>

                {/* Edit Profile Button */}
                <Button
                  onClick={() => setIsEditing(!isEditing)}
                  sx={{
                    color: '#B68C34',
                    borderColor: '#B68C34',

                  }}
                >
                {isEditing ? (<Typography className='headerPrimary'>
                Cancel Edit Profile
                </Typography>) : (                <Typography className='headerPrimary'>
                Edit Profile

                </Typography>)}
                </Button>
              {/* Username Field */}
              <TextField
                label="Player Name"
                fullWidth
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                disabled={!isEditing}
                sx={{ mb: 2, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' },      
                "& .MuiInputBase-input.Mui-disabled": {
      WebkitTextFillColor: "gray",
    },
    }}
              />
              {/* Email Field */}
              <TextField
                label="Email"
                fullWidth
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                disabled={!isEditing}
                sx={{ mb: 2, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' },                "& .MuiInputBase-input.Mui-disabled": {
      WebkitTextFillColor: "gray",
    }, }}
              />
              {/* Password Field */}
              <TextField
                label="Password"
                type={showPassword ? "text" : "password"}
                fullWidth
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                disabled={!isEditing}
                sx={{ mb: 2, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' },                 "& .MuiInputBase-input.Mui-disabled": {
      WebkitTextFillColor: "gray",
    }, }}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowPassword(!showPassword)}
                        edge="end"
                        sx={{ color: '#949083' }}
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  )
                }}
              />
              {/* Confirm Password Field */}
              <TextField
                label="Confirm Password"
                type={showConfirmPassword ? "text" : "password"}
                fullWidth
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                disabled={!isEditing}
                sx={{ mb: 2, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' } ,                 "& .MuiInputBase-input.Mui-disabled": {
      WebkitTextFillColor: "gray",
    },}}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        edge="end"
                        sx={{ color: '#949083',
                              "& .MuiInputBase-input.Mui-disabled": {
      WebkitTextFillColor: "gray",
    }, }}
                      >
                        {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  )
                }}
                error={!!passwordError}
                helperText={passwordError}
              />
            </Box>
          </Box>

          {/* Right Section (Avatar Picker) */}
          <Box sx={{ width: '50%', paddingLeft: 3 }}>
            <Typography sx={{ mb: 1, color: '#f0e6d2' }} className="headerPrimary">
              ACQUIRED 2024
            </Typography>
            <Box display="flex" flexWrap="wrap">
              {avatars.map((avatarSrc, index) => (
                <Box
                  key={index}
                  sx={{
                    width: '6vw',
                    height: '6vw',
                    margin: '5px',
                    cursor: 'pointer',
                    overflow: 'hidden',
                    border: avatar === avatarSrc ? '2px solid #B68C34' : 'none'
                  }}
                  onClick={() => setAvatar(avatarSrc)}
                >
                  <img
                    src={avatarSrc}
                    alt={`avatar-${index}`}
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover',
                    }}
                  />
                </Box>
              ))}
            </Box>
          </Box>
        </Box>

        {/* Save Changes Button */}
        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: 3 }}>
          <Button
            className="headerPrimary"
            onClick={handleSave}
            sx={{ borderColor: '#B68C34', border: 1, borderRadius: 0, color: '#B68C34' }}
            disabled={ isEditing && password !== confirmPassword} // Disable if not editing or passwords don't match
          >
            <Typography className="headerPrimary" fontSize="1em">
              Save Changes
            </Typography>
          </Button>
        </Box>
      </DialogContent>
    </Dialog>
  );
};

export default EditProfileModal;
