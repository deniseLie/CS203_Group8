import React, { useState } from "react";
import {
  Box,
  TextField,
  Typography,
  IconButton,
  InputAdornment,
  Alert,
} from "@mui/material";
import { ArrowForward, ArrowBack, Visibility, VisibilityOff } from "@mui/icons-material";
import { Link, useNavigate } from "react-router-dom"; // Import useNavigate
import loginSplash from "../assets/login_splash.jpg";
import logo from "../assets/riot_logo.png";
import axios from "axios";
import env from "react-dotenv";

function Register() {

  // Constants: Register fields - Email, Username, Password, Playername, confirm password (check if same)
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [playername, setPlayername] = useState("");

  // Constants: Error for fields
  const [playernameError, setPlayernameError] = useState(false);
  const [emailError, setEmailError] = useState(false);

  // Constants: Show/Hide passwords
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  // Constant: Error message for login form
  const [errorMessage, setErrorMessage] = useState(""); // State to store the error message

  const navigate = useNavigate(); // Use navigate for redirecting

  // Constant: Check if form is filled to enable register button
  const isFormFilled =
    username.length > 0 &&
    password.length > 0 &&
    password === confirmPassword &&
    email.length > 0 && !emailError;

  // Function : Handle Register
  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage(""); // Clear any previous error message
    if (isFormFilled) {
      try {
        const response = await axios.post(`${env.LOGIN_SERVER_URL}/auth/register`, {
          username: username,
          email: email,
          playername: playername,
          password: password,
        });
  
        if (response.status === 200) {
          navigate("/login"); // Redirect to login page if registration is successful
        }
      } catch (error) {
        console.error("Registration error:", error);
        // Set the error message to be displayed
        setErrorMessage("Registration failed. Please try again.");
      }
    }
  };

  // Function: Set email constant and check email if valid
  const handleEmailChange = (e) => {
    const value = e.target.value;
    setEmail(value);

    // Validate email format with regex
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    setEmailError(!emailRegex.test(value)); // Set error if email format is invalid
  };

  // Function: Set playername and check if playername is not more than 20 characters
  const handlePlayernameChange = (e) => {
    const value = e.target.value;
    setPlayername(value);
    setPlayernameError(playername.length > 20);
  };

  // Functions: Toggle show password constant
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
        {errorMessage && (
          <Alert severity="error" sx={{ mb: 2, width: "100%" }}>
            {errorMessage}
          </Alert>
        )}
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

          {/* playername Field with Validation */}
          <TextField
            margin="normal"
            required
            fullWidth
            id="playername"
            label="Player Name"
            name="playername"
            autoComplete="playername"
            autoFocus
            value={playername}
            onChange={handlePlayernameChange}
            error={playernameError} // Show error state if playername is invalid
            helperText={playernameError ? "Please enter a valid playername" : ""} // Error message for invalid playername
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
