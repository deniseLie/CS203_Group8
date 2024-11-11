import React from 'react';
import { Box, Typography } from '@mui/material';
import PlayerIcon from '../components/PlayerIcon'; // Ensure this path is correct based on your project structure

function ProfileBanner({ profile, displayType }) {
  return (
    <Box
      sx={{
        backgroundImage: `url(${profile.banner})`,
        backgroundSize: 'cover',
        backgroundPosition: 'bottom',
        padding: 3,
        borderRadius: '8px',
        position: 'relative',
        minHeight: '80vh', // Adjust height if needed to make it more compact
        width: '250px',
        margin: '0 auto',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'flex-start', // Align items towards the top
        paddingTop: 4, // Adjust padding to shift content up
      }}
    >
      {displayType === 'rank' ? (
        <>
          {/* Rank Symbol - Only show in 'rank' displayType */}
          {profile.rankSymbol && (
            <Box
              component="img"
              src={profile.rankSymbol}
              alt="Rank Symbol"
              sx={{
                width: '130px',
                marginBottom: 2,
              }}
            />
          )}

          {/* Rank Title */}
          <Typography align="center" className="headerPrimary">
            {profile.rank}
          </Typography>

          {/* Rank LP */}
          <Typography align="center" className="headerPrimary">
            {profile.lp} LP
          </Typography>
        </>
      ) : (
        <Box marginTop={'20vh'}>
          {/* Player Icon - Only show in 'player' displayType */}
          <PlayerIcon
            src={profile.playerIcon}
            alt={profile.playerName}
            width={6}
            height={6}
            clickable={false}
            sx={{
              marginBottom: 1, // Reduce margin to bring it closer to the name
            }}
          />

          {/* Player Name */}
          <Typography align="center" className="headerPrimary" sx={{ mt: 1 }}>
            {profile.playerName}
          </Typography>

          {/* Rank Symbol and Title */}
          <Box display={'flex'} alignItems={'center'} mt={1}>
            <Box
              component="img"
              src={profile.rankSymbol}
              alt="Rank Symbol"
              sx={{
                width: '2vw', // Adjust size as needed
                height: '2vw',
                marginRight: 1,
              }}
            />
            <Typography className="bodySecondary" fontSize={'1em'}>
              {profile.rank}
            </Typography>
          </Box>
        </Box>
      )}
    </Box>
  );
}

export default ProfileBanner;
