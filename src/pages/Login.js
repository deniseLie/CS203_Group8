import React, { useState } from "react";
import { Box, Checkbox, TextField, Typography, IconButton } from "@mui/material";
import { ArrowForward } from "@mui/icons-material";
import loginSplash from "../assets/login_splash.jpg";
import logo from "../assets/riot_logo.png";

function Login({ login }) { // Receive the login function as a prop
  const [username, setUsername] = useState(""); // State to track username
  const [password, setPassword] = useState(""); // State to track password
  const [error, setError] = useState(""); // State to track authentication error
  const [isLoading, setIsLoading] = useState(false); // State to track loading

  // Check if both username and password are filled
  const isFormFilled = username.length > 0 && password.length > 0;

  // Authentication logic
  const authenticateUser = async () => {
    setIsLoading(true);
    setError("");

    // Simulate a small delay to mimic a real API request
    setTimeout(() => {
      if (username === "username" && password === "password") {
        login();
      } else {
        setError("Invalid username or password");
      }

      setIsLoading(false); // Stop loading after checking credentials
    }, 500); // Simulated delay of 0.5 second

    // // Simulate a network request (USE THIS WHEN API READY)
    // try {
    //   const response = await fetch("https://api.example.com/login", {
    //     method: "POST",
    //     headers: {
    //       "Content-Type": "application/json",
    //     },
    //     body: JSON.stringify({ username, password }),
    //   });

    //   if (response.ok) {
    //     const data = await response.json();

    //     // Simulate token received from server
    //     const token = data.token;

    //     // Store the token in localStorage for session persistence
    //     localStorage.setItem("authToken", token);

    //     // Simulate successful login by calling the login prop
    //     login();
    //   } else {
    //     // Handle different types of errors
    //     setError("Invalid username or password");
    //   }
    // } catch (error) {
    //   setError("An error occurred. Please try again.");
    // } finally {
    //   setIsLoading(false); // Stop loading after the request is finished
    // }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (isFormFilled) {
      authenticateUser(); // Call the function to authenticate the user
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

        {/* Display error message if authentication fails */}
        {error && (
          <Typography color="error" sx={{ mb: 2 }}>
            {error}
          </Typography>
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
            onChange={(e) => setUsername(e.target.value)} // Update state on input change
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)} // Update state on input change
          />

          <Box display={"flex"} alignContent={"center"} marginTop={1}>
            <Checkbox
              sx={{
                color: "#d53235", // Default color
                '&.Mui-checked': {
                  color: "#d53235", // Color when checked
                },
                '&:hover': {
                  backgroundColor: "transparent", // Prevents the hover background effect
                },
                '&:focus': {
                  outline: "none", // Removes the default focus outline
                },
                margin: 0, // Removes margin
                padding: 0, // Removes padding
              }}
            />
            <Typography alignSelf={"center"} fontSize={"0.9em"} marginLeft={1}>
              Stay signed in
            </Typography>
          </Box>

          {/* Center the IconButton */}
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
                  backgroundColor: isFormFilled ? "rgb(191, 34, 36)" : "#d53235", // Darker red on hover
                },
              }}
              disabled={!isFormFilled || isLoading} // Disable the button if form is not filled or during loading
            >
              {isLoading ? (
                <Typography variant="body2" color="inherit">Loading...</Typography>
              ) : (
                <ArrowForward fontSize="large" /> // Arrow icon
              )}
            </IconButton>
          </Box>
        </Box>

        <Typography sx={{ letterSpacing: -0.2, fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700", marginTop: "2%" }}>
          CAN'T SIGN IN?
        </Typography>
        <Typography sx={{ letterSpacing: -0.2, fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700" }}>
          CREATE ACCOUNT
        </Typography>
      </Box>
    </Box>
  );
}

export default Login;
