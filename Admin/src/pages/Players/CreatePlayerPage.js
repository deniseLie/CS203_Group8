import React, { useState } from 'react';
import {
  Box, Typography, TextField, Button, Stack, Dialog, DialogActions, DialogContent, DialogTitle,
  Avatar
} from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import axios from 'axios';

// Importing the profile pictures directly
import profile1 from '../../assets/summonerIcon/1.jpg';
import profile2 from '../../assets/summonerIcon/2.jpg';
import profile3 from '../../assets/summonerIcon/3.jpg';
import profile4 from '../../assets/summonerIcon/4.jpg';
import profile5 from '../../assets/summonerIcon/5.jpg';
import profile6 from '../../assets/summonerIcon/6.jpg';
import profile7 from '../../assets/summonerIcon/7.jpg';
import profile8 from '../../assets/summonerIcon/8.jpg';

// Array of imported profile pictures
const predefinedProfilePictures = [
  { src: profile1, name: '1.jpg' },
  { src: profile2, name: '2.jpg' },
  { src: profile3, name: '3.jpg' },
  { src: profile4, name: '4.jpg' },
  { src: profile5, name: '5.jpg' },
  { src: profile6, name: '6.jpg' },
  { src: profile7, name: '7.jpg' },
  { src: profile8, name: '8.jpg' },
];
const CreatePlayerPage = () => {
  // State hooks
  const [username, setUsername] = useState('');
  const [playername, setPlayername] = useState('');
  const [password, setPassword] = useState('');
  const [profilePicture, setProfilePicture] = useState('');
  const [email, setEmail] = useState('');
  const [openConfirmation, setOpenConfirmation] = useState(false);
  const [openPictureDialog, setOpenPictureDialog] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSave = () => {
    setOpenConfirmation(true);
  };

  const handleConfirmSave = async () => {
    const userData = {
      username,
      password,
      role: 'USER',
      playerName: playername,
      profilePicture,
      email,
    };

    try {
      const response = await axios.post('/admin/user/createUser', userData);
      if (response.status === 201) {
        setSuccess('User created successfully!');
        resetForm();
      } else {
        setError('Failed to create user. Please check the input data.');
      }
    } catch (error) {
      if (error.response) {
        const { status, data } = error.response;
        if (status === 400) {
          setError(data.message || 'Validation error. Please check the input.');
        } else if (status === 409) {
          setError(data.message || 'User already exists.');
        } else {
          setError(data.message || 'An error occurred. Please try again.');
        }
      } else {
        setError('Network error. Please check your connection.');
      }
      console.error('Error creating user:', error);
    } finally {
      setOpenConfirmation(false);
    }
  };

  const handleCloseConfirmation = () => {
    setOpenConfirmation(false);
  };

  const handleProfilePictureClick = () => {
    setOpenPictureDialog(true);
  };

  const handleSelectProfilePicture = (pictureName) => {
    setProfilePicture(pictureName);
    setOpenPictureDialog(false);
  };

  const resetForm = () => {
    setUsername('');
    setPlayername('');
    setPassword('');
    setProfilePicture('');
    setEmail('');
    setError('');
    setSuccess('');
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <TopBar />
        <Typography variant="h4" sx={{ mb: 2 }}>
          Create User
        </Typography>
        <Box sx={{ backgroundColor: '#ffffff', p: 3, borderRadius: 2, boxShadow: 1 }}>
          <Stack spacing={2}>
            <TextField
              label="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Player Name"
              value={playername}
              onChange={(e) => setPlayername(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              fullWidth
              required
            />
            <TextField
              label="Profile Picture"
              value={profilePicture}
              onClick={handleProfilePictureClick}
              fullWidth
              placeholder="Select a profile picture"
              InputProps={{
                readOnly: true,
              }}
            />
            {error && (
              <Typography color="error" sx={{ mt: 1 }}>
                {error}
              </Typography>
            )}
            {success && (
              <Typography color="primary" sx={{ mt: 1 }}>
                {success}
              </Typography>
            )}
            <Button variant="contained" color="primary" onClick={handleSave}>
              Save User
            </Button>
          </Stack>
        </Box>

        {/* Profile Picture Selection Dialog */}
        <Dialog open={openPictureDialog} onClose={() => setOpenPictureDialog(false)}>
          <DialogTitle>Select Profile Picture</DialogTitle>
          <DialogContent>
            <Stack direction="row" spacing={2} flexWrap="wrap" justifyContent="center">
              {predefinedProfilePictures.map((picture) => (
                <Box
                  key={picture.name}
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    margin: '8px',
                    cursor: 'pointer',
                  }}
                  onClick={() => handleSelectProfilePicture(picture.name)}
                >
                  <Avatar
                    src={picture.src}
                    alt={picture.name}
                    sx={{ width: 80, height: 80 }}
                  />
                  <Typography variant="body2" align="center">
                    {picture.name}
                  </Typography>
                </Box>
              ))}
            </Stack>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenPictureDialog(false)} color="primary">
              Cancel
            </Button>
          </DialogActions>
        </Dialog>


        {/* Confirmation Dialog */}
        <Dialog open={openConfirmation} onClose={handleCloseConfirmation}>
          <DialogTitle>Confirm User Creation</DialogTitle>
          <DialogContent>
            <Typography>Are you sure you want to create this user?</Typography>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseConfirmation} color="primary">
              Cancel
            </Button>
            <Button onClick={handleConfirmSave} color="primary">
              Confirm
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Box>
  );
};

export default CreatePlayerPage;
