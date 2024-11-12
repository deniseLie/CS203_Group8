import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';  // Import the AuthProvider
import PrivateRoute from './components/PrivateRoute';  // Import the PrivateRoute

import Login from './pages/Login/Login';
import Dashboard from './pages/Dashboard/Dashboard';
import OngoingTournamentsPage from './pages/Tournaments/OngoingTournamentsPage'
import CompletedTournamentsPage from './pages/Tournaments/CompletedTournamentsPage';
import ConfigureTournamentPage from './pages/Tournaments/ConfigureTournamentPage';
import AddTournamentPage from './pages/Tournaments/AddTournamentPage';
import PlayerDatasetPage from './pages/Players/Dataset';
import LeaderboardsPage from './pages/Players/Leaderboards';
import CreatePlayerPage from './pages/Players/CreatePlayerPage';
import NotFoundPage from './pages/NotFound/NotFoundPage';
import EditPlayerPage from './pages/Players/EditPlayerPage';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route 
            path="/dashboard" 
            element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            }
          />
          <Route 
            path="/tournaments/ongoing" 
            element={
              <PrivateRoute>
                <OngoingTournamentsPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/tournaments/completed" 
            element={
              <PrivateRoute>
                <CompletedTournamentsPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/tournaments/configure" 
            element={
              <PrivateRoute>
                <ConfigureTournamentPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/tournaments/add" 
            element={
              <PrivateRoute>
                <AddTournamentPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/players/dataset" 
            element={
              <PrivateRoute>
                <PlayerDatasetPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/players/leaderboards" 
            element={
              <PrivateRoute>
                <LeaderboardsPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/players/create" 
            element={
              <PrivateRoute>
                <CreatePlayerPage />
              </PrivateRoute>
            }
          />
          <Route 
            path="/players/edit" 
            element={
              <PrivateRoute>
                <EditPlayerPage />
              </PrivateRoute>
            }
          />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
