import React, { useState } from "react";
import {
  Box,
  TextField,
  Typography,
  IconButton,
  InputAdornment,
} from "@mui/material";
import { ArrowForward, ArrowBack, Visibility, VisibilityOff } from "@mui/icons-material";
import { Link } from "react-router-dom";
import loginSplash from "../assets/login_splash.jpg";
import logo from "../assets/riot_logo.png";

function Register() {
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [emailError, setEmailError] = useState(false); // State to track email validation error

  const [showPassword, setShowPassword] = useState(false); // Toggle for password visibility
  const [showConfirmPassword, setShowConfirmPassword] = useState(false); // Toggle for confirm password visibility

  const isFormFilled =
    username.length > 0 &&
    password.length > 0 &&
    password === confirmPassword &&
    email.length > 0 && !emailError; // Ensure email is filled and valid

  const handleSubmit = (event) => {
    event.preventDefault();
    if (isFormFilled) {
      // Submit logic here
    }
  };

  const handleEmailChange = (e) => {
    const value = e.target.value;
    setEmail(value);

    // Validate email format with regex
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    setEmailError(!emailRegex.test(value)); // Set error if email format is invalid
  };

  const handleClickShowPassword = () => setShowPassword(!showPassword);
  const handleClickShowConfirmPassword = () => setShowConfirmPassword(!showConfirmPassword);

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
      {/* Logo */}
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
            xl: "33%",
          },
          height: {
            xs: "auto", // For small screens, auto adjusts based on content
            md: "auto", // For large screens
          },
          position: "relative", // Required for absolute positioning inside
        }}
      >
        {/* ArrowBack positioned at the top left */}
        <IconButton
          component={Link}
          to="/login"
          sx={{
            position: "absolute", // Positioning absolutely inside the box
            top: 10,
            left: 10,
            color: "black", // Set the color of the icon
          }}
        >
          <ArrowBack />
        </IconButton>

        {/* Title */}
        <Typography
          component="h1"
          variant="h5"
          sx={{ mb: 2, mt: 3, fontWeight: "600", letterSpacing: -1 }}
        >
          Create Account
        </Typography>

        {/* Form */}
        <Box
          component="form"
          noValidate
          sx={{ mt: 1, width: "90%" }}
          onSubmit={handleSubmit}
        >
          {/* Email Field with Validation */}
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email"
            name="email"
            autoComplete="email"
            autoFocus
            value={email}
            onChange={handleEmailChange}
            error={emailError} // Show error state if email is invalid
            helperText={emailError ? "Please enter a valid email" : ""} // Error message for invalid email
          />

          <TextField
            margin="normal"
            required
            fullWidth
            id="username"
            label="Username"
            name="username"
            autoComplete="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          {/* Password Field */}
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

          {/* Confirm Password Field */}
          <TextField
            margin="normal"
            required
            fullWidth
            name="confirmPassword"
            label="Confirm Password"
            type={showConfirmPassword ? "text" : "password"}
            id="confirmPassword"
            autoComplete="current-password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={handleClickShowConfirmPassword} edge="end">
                    {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />

          {/* Center the IconButton */}
          <Box display="flex" justifyContent="center" alignItems="center">
            <IconButton
              type="submit"
              sx={{
                mt: 3,
                mb: 2,
                border: "2px solid",
                borderColor: isFormFilled
                  ? "rgb(191, 34, 36)"
                  : "rgba(173, 173, 173, 0.3)",
                backgroundColor: isFormFilled
                  ? "#d53235"
                  : "rgba(173, 173, 173, 0.3)",
                padding: "20px",
                borderRadius: "30px",
                color: isFormFilled ? "white" : "rgba(173, 173, 173, 0.3)",
                "&:hover": {
                  backgroundColor: isFormFilled
                    ? "rgb(191, 34, 36)"
                    : "#d53235", // Darker red on hover
                },
              }}
              disabled={!isFormFilled}
            >
              <ArrowForward fontSize="large" />
            </IconButton>
          </Box>
        </Box>
      </Box>
    </Box>
  );
}

export default Register;
