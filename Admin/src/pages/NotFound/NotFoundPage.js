import React from 'react';
import { Box, Typography, Button, Stack } from '@mui/material'; // Material-UI components for layout and styling
import { useNavigate } from 'react-router-dom'; // Hook for navigation

/**
 * NotFoundPage Component
 *
 * A functional component that displays a 404 error page when the user navigates
 * to a route that does not exist. Provides options to return to the dashboard
 * or go back to the previous page.
 */
const NotFoundPage = () => {
  const navigate = useNavigate(); // Initialize the navigate function for programmatic navigation

  /**
   * Redirects the user to the home/dashboard page.
   */
  const handleGoHome = () => {
    navigate('/dashboard'); // Redirects to the dashboard
  };

  /**
   * Takes the user back to the previous page.
   */
  const handleGoBack = () => {
    navigate(-1); // Goes back to the previous route in the browser history
  };

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column', // Aligns items in a column layout
        alignItems: 'center', // Centers content horizontally
        justifyContent: 'center', // Centers content vertically
        height: '100vh', // Takes up full viewport height
        backgroundColor: '#f4f5f7', // Light background color for better contrast
      }}
    >
      {/* Main content area */}
      <Box sx={{ textAlign: 'center', padding: 3 }}>
        {/* 404 Error Code */}
        <Typography variant="h3" color="primary" sx={{ fontWeight: 'bold' }}>
          404
        </Typography>

        {/* Error message heading */}
        <Typography variant="h5" sx={{ mb: 2 }}>
          Oops! Page Not Found.
        </Typography>

        {/* Detailed error message */}
        <Typography variant="body1" sx={{ mb: 3 }}>
          The page you're looking for doesn't exist or has been moved.
        </Typography>

        {/* Action buttons */}
        <Stack direction="row" spacing={2} sx={{ justifyContent: 'center' }}>
          {/* Button to go to the home/dashboard */}
          <Button variant="contained" color="primary" onClick={handleGoHome}>
            Go to Home
          </Button>

          {/* Button to go back to the previous page */}
          <Button variant="outlined" color="secondary" onClick={handleGoBack}>
            Go Back
          </Button>
        </Stack>
      </Box>
    </Box>
  );
};

export default NotFoundPage;
