import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material';
import { AuthProvider, useAuth } from './auth/AuthProvider';
import Login from './pages/Login';
import Register from './pages/Register';
import FindTournament from './pages/FindTournament';
import Profile from './pages/Profile';
import History from './pages/History';
import Leaderboard from './pages/Leaderboard';
import PostGame from './pages/PostGame';
import TournamentBracket from './pages/TournamentBracket';
import LoginSuccess from './pages/LoginSuccess';

const theme = createTheme({
  typography: {
    fontFamily: 'Mark, sans-serif',
  },
});

function AppRoutes() {
  const { user, login, logout } = useAuth();
  const isAuthenticated = !!user;

  return (
    <Routes>
      <Route path="/register" element={<Profile />} />
      <Route path="/login" element={isAuthenticated ? <Navigate to="/" /> : <Login login={login} />} />
      <Route path="/" element={isAuthenticated ? <FindTournament  /> : <Navigate to="/login" />} />
      <Route path="/profile" element={isAuthenticated ? <Profile  /> : <Navigate to="/login" />} />
      <Route path="/login-success" element={<LoginSuccess />} />
      <Route path="/postgame" element={isAuthenticated ? <PostGame  /> : <Navigate to="/login" />} />
      <Route path="/history" element={isAuthenticated ? <History  /> : <Navigate to="/login" />} />
      <Route path="/tournamentBracket" element={isAuthenticated ? <TournamentBracket  /> : <Navigate to="/login" />} />
      <Route path="/leaderboard" element={isAuthenticated ? <Leaderboard  /> : <Navigate to="/login" />} />
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
}

function App() {
  return (
    <ThemeProvider theme={theme}>
      <AuthProvider>
        <Router>
          <AppRoutes />
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
