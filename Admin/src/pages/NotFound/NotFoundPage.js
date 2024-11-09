import React from 'react';
import { Box, Typography, Button, Stack } from '@mui/material';
import { useNavigate } from 'react-router-dom';  // Import useNavigate

const NotFoundPage = () => {
  const navigate = useNavigate();  // Initialize the navigate function

  const handleGoHome = () => {
    navigate('/dashboard');  // Use navigate to redirect to home
  };

  const handleGoBack = () => {
    navigate(-1);  // Go back to the previous page
  };

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh', backgroundColor: '#f4f5f7' }}>
      <Box sx={{ textAlign: 'center', padding: 3 }}>
        <Typography variant="h3" color="primary" sx={{ fontWeight: 'bold' }}>
          404
        </Typography>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Oops! Page Not Found.
        </Typography>
        <Typography variant="body1" sx={{ mb: 3 }}>
          The page you're looking for doesn't exist or has been moved.
        </Typography>

        <Stack direction="row" spacing={2} sx={{ justifyContent: 'center' }}>
          <Button variant="contained" color="primary" onClick={handleGoHome}>
            Go to Home
          </Button>
          <Button variant="outlined" color="secondary" onClick={handleGoBack}>
            Go Back
          </Button>
        </Stack>
      </Box>
    </Box>
  );
};

export default NotFoundPage;
