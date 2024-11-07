import React, { useState } from 'react';
import { Box, Typography, TextField, Button, Avatar, Stack } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import logo from '../../assets/logo.png'; // Import the logo image

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();

    // Placeholder for authentication logic
    if (username !== 'username' || password !== 'password') {
      setError('Incorrect Username or Password.');
    } else {
      setError('');
      // Redirect to the dashboard
      navigate('/dashboard');
    }
  };

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#2d3142',
      }}
    >
      <Box
        sx={{
          backgroundColor: '#ffffff',
          padding: '30px',
          width: '320px',
          textAlign: 'center',
          borderRadius: '8px',
          boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
        }}
      >
        <Stack direction="row" alignItems="center" justifyContent="center" spacing={1} mb={2}>
          <Avatar src={logo} alt="Tournament Logo" variant="square" sx={{ width: 35, height: 35 }} />
          <Typography variant="h5" sx={{ color: '#b3985e', fontWeight: 'bold' }}>
            Tournament
          </Typography>
        </Stack>
        <Typography variant="body2" color="textSecondary" mb={3}>
          Please enter your user information.
        </Typography>
        
        <form onSubmit={handleLogin}>
          <TextField
            label="Username"
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoComplete="username"
          />
          <TextField
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
          />

          {error && (
            <Typography
              sx={{
                backgroundColor: '#ff5c5c',
                color: '#ffffff',
                padding: '8px',
                borderRadius: '5px',
                fontSize: '14px',
                marginBottom: '10px',
              }}
            >
              {error}
            </Typography>
          )}

          <Button
            type="submit"
            variant="contained"
            fullWidth
            sx={{
              backgroundColor: '#b3985e',
              color: '#ffffff',
              mt: 2,
              '&:hover': {
                backgroundColor: '#a58752',
              },
            }}
          >
            Sign-in
          </Button>
        </form>
        
        <Typography variant="body2" color="textSecondary" mt={2} sx={{ cursor: 'pointer' }}>
          Forgot Password
        </Typography>
      </Box>
    </Box>
  );
};

export default Login;
