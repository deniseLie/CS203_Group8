import React from 'react';
import { Stack, Avatar, TextField } from '@mui/material';
import adminProfile from '../assets/adminpfp.png'; // Import the admin profile image

/**
 * TopBar Component
 *
 * This component serves as the top bar of the application, typically used for navigation,
 * search functionality, and user profile display.
 *
 * Features:
 * - A search bar for filtering or navigating content.
 * - A user avatar for profile or account management.
 */
const TopBar = () => (
  <Stack
    direction="row" // Arrange elements in a horizontal row
    alignItems="center" // Vertically align items in the center
    justifyContent="space-between" // Space items evenly across the width
    sx={{ mb: 2 }} // Add margin below the top bar
  >
    {/* Search Field */}
    <TextField
      placeholder="Search" // Placeholder text for the input
      variant="outlined" // Outlined style for the text field
      size="small" // Compact size for the text field
      sx={{ width: '300px' }} // Set a fixed width for the search bar
    />

    {/* User Profile Section */}
    <Stack direction="row" alignItems="center" spacing={2}>
      {/* User Avatar */}
      <Avatar
        src={adminProfile} // Source of the avatar image
        alt="User" // Alternative text for accessibility
        sx={{ width: 35, height: 35 }} // Set size for the avatar
      />
    </Stack>
  </Stack>
);

export default TopBar;
