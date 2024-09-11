import React, { useState } from "react";
import { Box, Checkbox, TextField, Typography, IconButton } from "@mui/material";
import { ArrowForward } from "@mui/icons-material";
import loginSplash from "../assets/login_splash.jpg";
import logo from "../assets/riot_logo.png";

function Login() {
  const [username, setUsername] = useState(""); // State to track username
  const [password, setPassword] = useState(""); // State to track password

  // Check if both username and password are filled
  const isFormFilled = username.length > 0 && password.length > 0;

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
        sx={{ background: "white", padding: 5, width: "33%", height: "60vh" }}
      >
        <Typography component="h1" variant="h5" sx={{ mb: 2, mt: 1, fontWeight: "600" }}>
          Sign in
        </Typography>
        <Box component="form" noValidate sx={{ mt: 1, width: "90%" }}>
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
          <Box display={"flex"} alignContent={"center"}>
            <Checkbox />
            <Typography alignSelf={"center"} fontSize={'0.9em'}>Stay signed in</Typography>
          </Box>

          {/* Center the IconButton */}
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
          >
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
              disabled={!isFormFilled} // Disable the button if form is not filled
            >
              <ArrowForward fontSize="large" /> {/* Arrow icon */}
            </IconButton>
          </Box>
        </Box>

        <Typography sx={{ letterSpacing: "0.08em", fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700", marginTop: "2%" }}>
          CAN'T SIGN IN?
        </Typography>
        <Typography sx={{ letterSpacing: "0.08em", fontSize: "0.65em", color: "rgb(74, 74, 74)", fontWeight: "700" }}>
          CREATE ACCOUNT
        </Typography>
      </Box>
    </Box>
  );
}

export default Login;
