import React from 'react';
import { Box, Typography } from '@mui/material';

function ProfileBanner({ profile }) {
  return (
    <Box
      sx={{
        backgroundImage: `url(${profile.banner})`, // Use the uploaded banner as the background
        backgroundSize: 'cover', // Cover the entire box
        backgroundPosition: 'bottom',
        padding: 3,
        borderRadius: '8px',
        position: 'relative', // Needed for positioning the rank symbol
        minHeight: '500px', // Make the box longer
        width: '250px', // Make the box thinner
        margin: '0 auto', // Center the whole box horizontally in its parent container
      }}
    >
      {/* Rank Symbol - Dynamically based on profile data */}
      {profile.rankSymbol && (
        <Box
          component="img"
          src={profile.rankSymbol} // Dynamically load the rank symbol
          alt="Rank Symbol"
          sx={{
            position: 'absolute',
            top: '20%', // Adjust the vertical positioning
            left: '50%', // Center horizontally
            transform: 'translateX(-50%)', // Correct for centering alignment
            width: '130px', // Adjust the size of the rank symbol
          }}
        />
      )}

      {/* Rank */}
      <Typography align="center" sx={{ marginTop: '85%' }} className="headerPrimary">
        {profile.rank}
      </Typography>

      {/* Rank ELO */}
      <Typography align="center" className="headerPrimary">
        {profile.elo}
      </Typography>
    </Box>
  );
}

export default ProfileBanner;
