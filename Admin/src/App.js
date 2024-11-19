import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';  // Import the AuthProvider
import PrivateRoute from './context/PrivateRoute';  // Import the PrivateRoute

import Login from './pages/Login/Login';
import Dashboard from './pages/Dashboard/Dashboard';
import TournamentsPage from './pages/Tournaments/TournamentsPage'
import ConfigureTournamentPage from './pages/Tournaments/ConfigureTournamentPage';
import PlayerDatasetPage from './pages/Players/Dataset';
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
            path="/tournaments/dataset" 
            element={
              <PrivateRoute>
                <TournamentsPage />
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
            path="/players/dataset" 
            element={
              <PrivateRoute>
                <PlayerDatasetPage />
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
