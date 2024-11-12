import React, { useState } from 'react';
import { Box, Typography } from '@mui/material';
import PlayerIcon from '../components/PlayerIcon'; // Ensure this path is correct based on your project structure
import editIcon from '../assets/button-edit.png'; // Adjust the path to your edit icon
import EditProfileModal from './EditProfileModal'; // Import the modal component

function ProfileBanner({ profile, displayType }) {
  const [hover, setHover] = useState(false);
  const [openModal, setOpenModal] = useState(false); // State to manage the modal visibility

  // State for form fields
  const [username, setUsername] = useState(profile.username);
  const [playerName,setPlayerName] = useState(profile.playerName);
  const [email, setEmail] = useState(profile.email);
  const [password, setPassword] = useState('');
  const [avatar, setAvatar] = useState(profile.playerIcon);

  // Open and close modal functions
  const handleOpenModal = () => setOpenModal(true);
  const handleCloseModal = () => setOpenModal(false);

  // Handle Save Changes
  const handleSaveChanges = () => {
    // Save the changes logic (for now just logging the values)
    console.log("Changes Saved:", { username, email, password, avatar });
    handleCloseModal(); // Close the modal after saving
  };

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
            {profile.lp}
          </Typography>
        </>
      ) : (
        <Box marginTop={'20vh'}>
          {/* Player Icon with hover effect */}
          <Box
            onMouseEnter={() => setHover(true)} // Set hover state to true on hover
            onMouseLeave={() => setHover(false)} // Set hover state to false when not hovered
            sx={{
              position: 'relative',
              display: 'inline-block',
            }}
          >
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

            {/* Edit Button */}
            {hover && (
              <Box
                component="img"
                src={editIcon}
                alt="Edit Icon"
                sx={{
                  position: 'absolute',
                  top: 0,
                  right: 0,
                  width: '2.5vw',
                  height: '2.5vw',
                  cursor: 'pointer',
                  zIndex: 1000,
                }}
                onClick={handleOpenModal} // Open modal on click
              />
            )}
          </Box>

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

      {/* Modal for Editing Profile */}
      <EditProfileModal
        open={openModal}
        handleClose={handleCloseModal}
        username={username}
        setUsername={setUsername}
        playerName = {playerName}
        setPlayername = {setPlayerName}
        email={email}
        setEmail={setEmail}
        password={password}
        setPassword={setPassword}
        avatar={avatar}
        setAvatar={setAvatar}
        avatars={profile.avatars} // Assuming profile.avatars contains a list of avatar images
        handleSaveChanges={handleSaveChanges}
      />
    </Box>
  );
}

export default ProfileBanner;
