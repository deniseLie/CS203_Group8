import './App.css';
import Login from './pages/Login';
import Home from './pages/Landing'; // Assuming you have a Home component
import History from './pages/History'; // Import the History page
import Leaderboard from './pages/Leaderboard'; // Import the Leaderboard page
import { createTheme, ThemeProvider } from '@mui/material';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react';

// Create a custom theme
const theme = createTheme({
  typography: {
    fontFamily: 'Mark, sans-serif', // Define custom font
  },
});

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false); // Dummy authentication state

  // Dummy function to simulate login (You can integrate this with real auth later)

  // NOT SECURE TO USE localStorage, this is temp
  const login = () => {
    setIsAuthenticated(true);
    localStorage.setItem('isAuthenticated', 'true'); // Store the authentication state
  };

  const logout = () => {
    setIsAuthenticated(false);
    localStorage.removeItem('isAuthenticated'); // Remove the authentication state
  };

  // Check if the user is already authenticated from local storage (to persist across page refreshes)
  useState(() => {
    // To reset login status when npm is started again TEMP
    if (process.env.NODE_ENV === 'development') {
      localStorage.removeItem('isAuthenticated');
    }

    const storedAuth = localStorage.getItem('isAuthenticated');
    if (storedAuth) {
      setIsAuthenticated(true);
    }
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <Router>
        <Routes>
          {/* Redirect to Home if authenticated, otherwise go to Login */}
          <Route
            path="/login"
            element={isAuthenticated ? <Navigate to="/" /> : <Login login={login} />}
          />
          
          {/* Protected route for Home */}
          <Route
            path="/"
            element={isAuthenticated ? <Home /> : <Navigate to="/login" />}
          />

          {/* Protected route for History */}
          <Route
            path="/history"
            element={isAuthenticated ? <History /> : <Navigate to="/login" />}
          />
          
          {/* Protected route for Leaderboard */}
          <Route
            path="/leaderboard"
            element={isAuthenticated ? <Leaderboard /> : <Navigate to="/login" />}
          />
          
          {/* Catch-all route to redirect to login if no route matches */}
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
