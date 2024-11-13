import React, { useState } from 'react';
import {
  Box, Typography, TextField, Button, Stack, Dialog, DialogActions, DialogContent, DialogTitle,
  Avatar, Grid
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
      role: 'USER', // Fixed role as USER
      playerName: playername,
      profilePicture,
    };

    try {
      const response = await axios.post('/admin/user/createUser', userData);
      if (response.status === 201) {
        setSuccess('User created successfully!');
        resetForm();
      }
    } catch (error) {
      setError('Failed to create user. Please try again.');
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
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Page Title */}
        <Typography variant="h4" sx={{ mb: 2 }}>
          Create User
        </Typography>

        {/* User Details Form */}
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
              label="Profile Picture"
              value={profilePicture}
              onClick={handleProfilePictureClick} // Open profile picture dialog on click
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
            <Grid container spacing={2}>
              {predefinedProfilePictures.map((picture) => (
                <Grid item xs={4} key={picture.name}>
                  <Avatar
                    src={picture.src} // Use imported image
                    alt={picture.name}
                    sx={{ width: 80, height: 80, cursor: 'pointer' }}
                    onClick={() => handleSelectProfilePicture(picture.name)}
                  />
                  <Typography variant="body2" align="center">{picture.name}</Typography>
                </Grid>
              ))}
            </Grid>
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
            <Typography>
              Are you sure you want to create this user?
            </Typography>
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
