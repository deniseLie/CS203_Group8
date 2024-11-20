import React, { useState } from 'react';
import { Box, Typography, TextField, Button, Avatar, Stack } from '@mui/material';
import { useNavigate } from 'react-router-dom'; // For programmatic navigation
import { useAuth } from '../../context/AuthContext'; // Access authentication context
import api from '../../services/api'; // Custom API service for handling requests
import logo from '../../assets/logo.png'; // Application logo

/**
 * Login Component
 * 
 * A functional component that handles user login functionality.
 * Allows users to enter their credentials, validates them via an API call,
 * and redirects to the dashboard upon successful login.
 */
const Login = () => {
  // State to manage input fields and errors
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  
  const navigate = useNavigate(); // Hook for navigation
  const { login } = useAuth(); // Access the login function from AuthContext

  /**
   * Handles the login form submission.
   * Sends the username and password to the server for authentication.
   * On success, stores the token and redirects to the dashboard.
   * 
   * @param {object} e - The form submission event.
   */
  const handleLogin = async (e) => {
    e.preventDefault(); // Prevent default form submission
    setError(''); // Clear previous errors

    try {
      // Make a POST request to the login endpoint with username and password
      const response = await api.post('/admin/user/login', {
        username,
        password,
      });

      // If login is successful, save the token and navigate to the dashboard
      if (response.status === 200 && response.data.token) {
        const token = response.data.token;

        // Save the token in local storage for session persistence
        localStorage.setItem('authToken', token);

        // Call the login function to update the authentication state
        login(token);

        // Redirect to the dashboard
        navigate('/dashboard');
      } else {
        // If response is not successful, show an error message
        setError('Invalid credentials.');
      }
    } catch (error) {
      // Handle errors (e.g., invalid credentials or server issues)
      if (error.response && error.response.status === 401) {
        setError('Incorrect Username or Password.');
      } else {
        setError('An error occurred. Please try again.');

        // Uncomment below code for testing without backend integration
        const token = 'randomsetofcharacters';
        localStorage.setItem('authToken', token);
        login(token);
        navigate('/dashboard');
      }
    }
  };

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#2d3142', // Background color for the page
      }}
    >
      <Box
        sx={{
          backgroundColor: '#ffffff',
          padding: '30px',
          width: '320px',
          textAlign: 'center',
          borderRadius: '8px',
          boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', // Box shadow for card appearance
        }}
      >
        {/* Logo and Application Title */}
        <Stack direction="row" alignItems="center" justifyContent="center" spacing={1} mb={2}>
          <Avatar src={logo} alt="Tournament Logo" variant="square" sx={{ width: 35, height: 35 }} />
          <Typography variant="h5" sx={{ color: '#b3985e', fontWeight: 'bold' }}>
            Tournament
          </Typography>
        </Stack>
        
        <Typography variant="body2" color="textSecondary" mb={3}>
          Please enter your user information.
        </Typography>
        
        {/* Login Form */}
        <form onSubmit={handleLogin}>
          <TextField
            label="Username"
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)} // Update username state
            autoComplete="username" // Autocomplete for username
          />
          <TextField
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)} // Update password state
            autoComplete="current-password" // Autocomplete for password
          />

          {/* Display error message, if any */}
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
              backgroundColor: '#b3985e', // Button color
              color: '#ffffff',
              mt: 2,
              '&:hover': {
                backgroundColor: '#a58752', // Hover effect
              },
            }}
          >
            Sign-in
          </Button>
        </form>

        {/* Forgot Password Placeholder */}
        <Typography variant="body2" color="textSecondary" mt={2} sx={{ cursor: 'pointer' }}>
          Forgot Password
        </Typography>
      </Box>
    </Box>
  );
};

export default Login;
