import React, { useState } from "react";
import { Box, TextField, Typography, IconButton, InputAdornment, Alert, Button } from "@mui/material";
import { ArrowForward, Visibility, VisibilityOff } from "@mui/icons-material";
import loginSplash from "../assets/login_splash.jpg";
import { Link } from 'react-router-dom';
import axios from 'axios';
import logo from "../assets/riot_logo.png";
import Cookies from 'js-cookie'; // Import js-cookie
import env from "react-dotenv";

function Login({ login }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const isFormFilled = username.length > 0 && password.length > 0;
  const handleClickShowPassword = () => setShowPassword(!showPassword);

  const handleGoogleLogin = () => {
    window.location.href = `${env.LOGIN_SERVER_URL}/oauth2/authorization/google`;
  };
  
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (isFormFilled) {
      try {
        const response = await axios.post(`${env.LOGIN_SERVER_URL}/auth/login`, {
          username: username,
          password: password
        });
        
        const token = response.data.jwt;
        login(token);
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
      <Box component="img" src={logo} alt="Logo" sx={{ position: "absolute", top: 25, left: 25, height: 40, width: "auto" }} />

      <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" sx={{
        background: "white",
        padding: 5,
        width: { xs: "80%", sm: "50%", lg: "25%", xl: "33%" },
        height: { xs: "auto", md: "auto" },
      }}>
        <Typography component="h1" variant="h5" sx={{ mb: 2, mt: 3, fontWeight: "600", letterSpacing: -1 }}>Sign in</Typography>
        {errorMessage && (
          <Alert severity="error" sx={{ mb: 2, width: "100%" }}>{errorMessage}</Alert>
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
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            mt={2}
            component={Button}
            onClick={handleGoogleLogin}
            sx={{
              border: '1px solid #4285F4',
              backgroundColor: '#fff',
              padding: '8px 16px',
              borderRadius: '4px',
              color: '#4285F4',
              boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
              textTransform: 'none',
              '&:hover': {
                backgroundColor: '#f5f5f5',
              },
            }}
          >
            <Typography variant="button" color="inherit">Sign in with Google</Typography>
          </Box>
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
          <Box display="flex" justifyContent="center">
            <Link to="/register" style={{ textDecoration: 'none' }}>
              <Typography
                sx={{ letterSpacing: -0.2, fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700", alignSelf: 'center', justifySelf: 'center' }}
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
