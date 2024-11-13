import React, { useEffect, useState } from 'react';
import { Dialog, DialogContent, TextField, Button, Box, Typography, IconButton, InputAdornment } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import { summonerIcons } from '../util/importAssets'; // Import the centralized icons object
import axios from 'axios';
import { useAuth } from '../auth/AuthProvider';
import env from 'react-dotenv';
import Cookies from 'js-cookie';

const EditProfileModal = ({
  open,
  handleClose,
  handleSaveChanges // callback after successful save
}) => {
  const [username, setUsername] = useState('');
  const [playerName, setPlayerName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [avatar, setAvatar] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [passwordError, setPasswordError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [avatarNum, setAvatarNumber] = useState(1);
  const [avatarChanged, setAvatarChanged] = useState(false);
  const {user} = useAuth();
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const validateEmail = (email) => emailRegex.test(email);
  const playerId = user.sub;
  const canSaveChanges = isEditing &&
    username &&
    playerName &&
    email &&
    validateEmail(email) &&
    password === confirmPassword;

  const canUpdateProfilePicture = avatarChanged;

  // Fetch user details when the modal opens
  useEffect(() => {
    if (open && playerId) {
      const token = Cookies.get('jwtToken');
      axios
        .get(`${env.LOGIN_SERVER_URL}/auth/user/${playerId}`, {headers: {
          Authorization: `Bearer ${token}`,
        }})
        .then((response) => {
          console.log("got ",response)
          const { username, playername, email } = response.data;
          setUsername(username);
          setPlayerName(playername);
          setEmail(email);
        })
        .catch((error) => {
          console.error("Failed to fetch user details:", error);
        });
    }
  }, [open, playerId]);
  // Handler for Save Changes or Update Profile Picture button
  const handleSave = async () => {
    if (isEditing || avatarChanged) {
      if (isEditing) {

        if (!validateEmail(email)) {
          setEmailError("Invalid email format");
          return;
        }
        setEmailError('');
      }

      // Prepare data to send to backend
      const profileData = {
        username,
        playerName,
        email,
        password,
        profilePicture: avatarNum + ".jpg", // Send the filename instead of the full path
      };

      console.log(profileData);

      // Uncomment for actual request
      // try {
      //   const response = await fetch(`/account/${playerId}/profile`, {
      //     method: 'PUT',
      //     headers: { 'Content-Type': 'application/json' },
      //     body: JSON.stringify(profileData),
      //   });

      //   if (response.ok) {
      //     console.log("Profile updated successfully");
      //     handleSaveChanges();
      //   } else {
      //     console.error("Failed to update profile");
      //   }
      // } catch (error) {
      //   console.error("Error updating profile:", error);
      // }
    }

    setIsEditing(false);
    setAvatarChanged(false); // Reset avatar changed state after saving
  };

  return (
    <Dialog fullWidth maxWidth="md" open={open} onClose={handleClose}>
      <DialogContent sx={{ background: '#010A13', border: 2, borderColor: "#775A27", display: 'flex', padding: '16px 32px', flexDirection: 'column' }}>
        <Box display={'flex'} justifyContent={'flex-end'}>
          <IconButton onClick={handleClose} sx={{ color: '#f0e6d2' }}>
            <CloseIcon />
          </IconButton>
        </Box>
        <Box sx={{ display: 'flex' }}>
          <Box sx={{ width: '50%', paddingRight: 2, borderRight: 1.5, borderRightColor: '#464F4D' }}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <Box
                sx={{
                  width: '5vw',
                  height: '5vw',
                  borderRadius: '50%',
                  overflow: 'hidden',
                  border: avatar ? '2px solid #C8AA6C' : 'none',
                  cursor: 'pointer'
                }}
              >
                <img
                  src={avatar ? avatar: summonerIcons[0]}
                  alt="Avatar"
                  style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                  }}
                />
              </Box>
              <Button onClick={() => setIsEditing(!isEditing)} sx={{ color: '#B68C34', borderColor: '#B68C34' }}>
                <Typography className='headerPrimary'>{isEditing ? "Cancel Edit Profile" : "Edit Profile"}</Typography>
              </Button>
              
              <TextField label="Player Name" fullWidth value={playerName} onChange={(e) => setPlayerName(e.target.value)} disabled={!isEditing} sx={{ mb: 1, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' }, "& .MuiInputBase-input.Mui-disabled": { WebkitTextFillColor: "gray" } }} />
              <TextField label="Username" fullWidth value={username} onChange={(e) => setUsername(e.target.value)} disabled={!isEditing} sx={{ mb: 1, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' }, "& .MuiInputBase-input.Mui-disabled": { WebkitTextFillColor: "gray" } }} />
              <TextField 
                label="Email" 
                fullWidth 
                value={email} 
                onChange={(e) => {
                  setEmail(e.target.value);
                  setEmailError(validateEmail(e.target.value) ? "" : "Invalid email format");
                }} 
                disabled={!isEditing} 
                sx={{ mb: 1, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' }, "& .MuiInputBase-input.Mui-disabled": { WebkitTextFillColor: "gray" } }} 
                error={isEditing && !!emailError}
                helperText={isEditing ? emailError : ""}
              />
              {/* <TextField label="Password" type={showPassword ? "text" : "password"} fullWidth value={password} onChange={(e) => setPassword(e.target.value)} disabled={!isEditing} sx={{ mb: 1, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' }, "& .MuiInputBase-input.Mui-disabled": { WebkitTextFillColor: "gray" } }} InputProps={{ endAdornment: (<InputAdornment position="end"><IconButton onClick={() => setShowPassword(!showPassword)} edge="end" sx={{ color: '#949083' }}>{showPassword ? <VisibilityOff /> : <Visibility />}</IconButton></InputAdornment>) }} />
              <TextField label="Confirm Password" type={showConfirmPassword ? "text" : "password"} fullWidth value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} disabled={!isEditing} sx={{ mb: 1, background: '#0F171D', borderRadius: 1, label: { color: '#949083' }, input: { color: 'white' }, "& .MuiInputBase-input.Mui-disabled": { WebkitTextFillColor: "gray" } }} InputProps={{ endAdornment: (<InputAdornment position="end"><IconButton onClick={() => setShowConfirmPassword(!showConfirmPassword)} edge="end" sx={{ color: '#949083' }}>{showConfirmPassword ? <VisibilityOff /> : <Visibility />}</IconButton></InputAdornment>) }} error={isEditing && !!passwordError} helperText={isEditing ? passwordError : ""} */}
              />
            </Box>
          </Box>

          <Box sx={{ width: '50%', paddingLeft: 3 }}>
            <Typography sx={{ mb: 1, color: '#f0e6d2' }} className="headerPrimary">ACQUIRED 2024</Typography>
            <Box display="flex" flexWrap="wrap">
              {Object.values(summonerIcons).map((avatarSrc, index) => (
                <Box
                  key={index}
                  sx={{
                    width: '6vw',
                    height: '6vw',
                    margin: '5px',
                    cursor: 'pointer',
                    overflow: 'hidden',
                    border: avatar === avatarSrc ? '2px solid #B68C34' : 'none'
                  }}
                  onClick={() => { 
                    setAvatar(avatarSrc); 
                    setAvatarNumber(index + 1); 
                    setAvatarChanged(true); // Mark avatar as changed
                  }}
                >
                  <img
                    src={avatarSrc}
                    alt={`avatar-${index}`}
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover',
                    }}
                  />
                </Box>
              ))}
            </Box>
          </Box>
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: 3 }}>
          <Button 
            className="headerPrimary" 
            onClick={handleSave} 
            sx={{ borderColor: '#B68C34', border: 1, borderRadius: 0, color: '#B68C34' }} 
            disabled={!canSaveChanges && !canUpdateProfilePicture}
          >
            <Typography className="headerPrimary" fontSize="1em">
              {canUpdateProfilePicture && !canSaveChanges ? "Update Profile Picture" : "Save Changes"}
            </Typography>
          </Button>
        </Box>
      </DialogContent>
    </Dialog>
  );
};

export default EditProfileModal;
