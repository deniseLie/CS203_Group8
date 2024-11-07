import './App.css';
import Login from './pages/Login';
import History from './pages/History'; // Import the History page
import Leaderboard from './pages/Leaderboard'; // Import the Leaderboard page
import { createTheme, ThemeProvider } from '@mui/material';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import FindTournament from './pages/FindTournament';
import Register from './pages/Register';
import PostGame from './pages/PostGame';
import TournamentBracket from './pages/TournamentBracket';

const theme = createTheme({
  typography: {
    fontFamily: 'Mark, sans-serif',
  },
});

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check if the user is authenticated by checking localStorage for JWT
  useEffect(() => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      setIsAuthenticated(true);
    }
  }, []);

  // Login function to set the authenticated state
  const login = () => {
    setIsAuthenticated(true);
    localStorage.setItem('isAuthenticated', 'true'); // Store the authentication state
  };

  // Logout function
  const logout = () => {
    localStorage.removeItem('jwtToken');
    setIsAuthenticated(false);
  };

  return (
    <ThemeProvider theme={theme}>
      <Router>
        <Routes>
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={isAuthenticated ? <Navigate to="/" /> : <Login login={login} />} />
          <Route path="/" element={isAuthenticated ? <FindTournament logout={logout} /> : <Navigate to="/login" />} />
          <Route path="/postgame" element={isAuthenticated ? <PostGame   logout={logout} /> : <Navigate to="/login" />} />
          {/* <Route path="/history" element={isAuthenticated ? <History logout={logout} /> : <Navigate to="/login" />} /> */}
          <Route path="/history" element={<History />} />
          <Route path="/tournamentBracket" element={isAuthenticated ? <TournamentBracket logout={logout} /> : <Navigate to="/login" />} />
          {/* <Route
            path="/leaderboard"
            element={isAuthenticated ? <Leaderboard /> : <Navigate to="/login" />}
          /> */}
          <Route
            path="/leaderboard"
            element={<Leaderboard />}
          />
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
