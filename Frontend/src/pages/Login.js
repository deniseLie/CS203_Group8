import React, { useState } from "react";
import { Box, TextField, Typography, IconButton, InputAdornment, Alert, Button } from "@mui/material";
import { ArrowForward, Visibility, VisibilityOff } from "@mui/icons-material";
import loginSplash from "../assets/login_splash.jpg";
import { Link } from 'react-router-dom';
import axios from 'axios'; // Import axios for API requests
import logo from "../assets/riot_logo.png";
import { GoogleOAuthProvider } from '@react-oauth/google'; // Import Google OAuth
import env from "react-dotenv";

function Login({ login }) {
  // Constants: Login form username, password
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // Constant: Toggle show password
  const [showPassword, setShowPassword] = useState(false);

  // Constant: Error Message for failed login
  const [errorMessage, setErrorMessage] = useState("");

  // const: check if username/password is filled in to enable login button
  const isFormFilled = username.length > 0 && password.length > 0;

  // Function: Toggle show password
  const handleClickShowPassword = () => setShowPassword(!showPassword);

  // Function: Redirect to backend link for Google OAuth  
  const handleGoogleLogin = () => {
    window.location.href = `${env.LOGIN_SERVER_URL}/oauth2/authorization/google`;
  };

  // Function: Handle login
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (isFormFilled) {
      try {
        console.log(`${env.LOGIN_SERVER_URL}/auth/login`);
        const response = await axios.post(`${env.LOGIN_SERVER_URL}/auth/login`, {
          username: username,
          password: password
        });

        // Set JWT
        const token = response.data.jwt;
        localStorage.setItem('jwtToken', token);
        login();
      } catch (error) {
        setErrorMessage("Invalid username or password");
      }
    }
  };

  return (
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="100vh"
        sx={{
          border: 1,
          borderColor: "black",
          backgroundImage: `url(${loginSplash})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <Box
          component="img"
          src={logo}
          alt="Logo"
          sx={{
            position: "absolute",
            top: 25,
            left: 25,
            height: 40,
            width: "auto",
          }}
        />

        <Box
          display={"flex"}
          flexDirection={"column"}
          alignItems={"center"}
          justifyContent={"center"}
          sx={{
            background: "white",
            padding: 5,
            width: {
              xs: "80%", // For small screens
              sm: "50%", // For medium screens
              lg: "25%", // For large screens
              xl: "33%"
            },
            height: {
              xs: "auto", // For small screens, auto adjusts based on content
              md: "auto", // For large screens
            },
          }}
        >
          <Typography component="h1" variant="h5" sx={{ mb: 2, mt: 3, fontWeight: "600", letterSpacing: -1 }}>
            Sign in
          </Typography>
          {errorMessage && (
          <Alert severity="error" sx={{ mb: 2, width: "100%" }}>
            {errorMessage}
          </Alert>
        )}
          <Box component="form" noValidate sx={{ mt: 1, width: "90%" }} onSubmit={handleSubmit}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />

            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type={showPassword ? "text" : "password"}
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton onClick={handleClickShowPassword} edge="end">
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />

            {/* Google Login Button */}
      <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      mt={2}
      component={Button}
      onClick={handleGoogleLogin}
      sx={{
        border: '1px solid #4285F4', // Google blue border
        backgroundColor: '#fff', // White background
        padding: '8px 16px',
        borderRadius: '4px',
        color: '#4285F4', // Text color in Google blue
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
        textTransform: 'none',
        '&:hover': {
          backgroundColor: '#f5f5f5',
        },
      }}
    >
      
      <Typography variant="button" color="inherit">
        Sign in with Google
      </Typography>
    </Box>
            {/* </Link> */}

            <Box display="flex" justifyContent="center" alignItems="center">
              <IconButton
                type="submit"
                sx={{
                  mt: 3,
                  mb: 2,
                  border: "2px solid",
                  borderColor: isFormFilled ? "rgb(191, 34, 36)" : "rgba(173, 173, 173, 0.3)",
                  backgroundColor: isFormFilled ? "#d53235" : "rgba(173, 173, 173, 0.3)",
                  padding: "20px",
                  borderRadius: "30px",
                  color: isFormFilled ? "white" : "rgba(173, 173, 173, 0.3)",
                  "&:hover": {
                    backgroundColor: isFormFilled ? "rgb(191, 34, 36)" : "#d53235",
                  },
                }}
                disabled={!isFormFilled}
              >
                <ArrowForward fontSize="large" />
              </IconButton>
              
            </Box>

{/*             
            <Typography sx={{ letterSpacing: -0.2, fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700", marginTop: "2%" }}>
              CAN'T SIGN IN?
            </Typography> */}
            <Box display="flex" justifyContent="center">
              <Link to="/register" style={{ textDecoration: 'none' }}>
                <Typography
                  sx={{ letterSpacing: -0.2, fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700", alignSelf:'center', justifySelf:'center' }}
                >
                  CREATE ACCOUNT
                </Typography>
              </Link>

            </Box>
          </Box>
        </Box>
      </Box>
  );
}

export default Login;
