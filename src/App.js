import './App.css';
import Login from './pages/Login';
import { createTheme, ThemeProvider } from '@mui/material';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react';
import FindTournament from './pages/FindTournament';

// Create a custom theme
const theme = createTheme({
  typography: {
    fontFamily: 'Mark, sans-serif', // Define custom font
  },
});

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false); // Dummy authentication state

  // Dummy function to simulate login (You can integrate this with real auth later)
  const login = () => {
    setIsAuthenticated(true);
  };

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
            element={isAuthenticated ? <FindTournament /> : <Navigate to="/login" />}
          />
          
          {/* Catch-all route to redirect to login if no route matches */}
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
