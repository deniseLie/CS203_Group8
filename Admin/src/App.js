import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';  // Context provider for authentication
import PrivateRoute from './context/PrivateRoute';  // Component to protect private routes

// Importing pages
import Login from './pages/Login/Login';  // Login page
import Dashboard from './pages/Dashboard/Dashboard';  // Main dashboard
import TournamentsPage from './pages/Tournaments/TournamentDatasetPage'; // Page for listing tournaments
import ConfigureTournamentPage from './pages/Tournaments/ConfigureTournamentPage'; // Page for configuring tournaments
import PlayerDatasetPage from './pages/Players/PlayerDatasetPage'; // Page for listing players
import CreatePlayerPage from './pages/Players/CreatePlayerPage'; // Page for creating a new player
import EditPlayerPage from './pages/Players/EditPlayerPage'; // Page for editing player details
import NotFoundPage from './pages/NotFound/NotFoundPage'; // Page for handling 404 errors

/**
 * Main App component that defines the application's routing and structure.
 */
function App() {
  return (
    // Wrapping the application in AuthProvider to manage authentication context
    <AuthProvider>
      {/* Router for managing client-side navigation */}
      <Router>
        <Routes>
          {/* Public route for login */}
          <Route path="/" element={<Login />} />

          {/* Private route for dashboard, accessible only to authenticated users */}
          <Route 
            path="/dashboard" 
            element={
              <PrivateRoute> {/* Protecting route using PrivateRoute */}
                <Dashboard />
              </PrivateRoute>
            }
          />

          {/* Private route for viewing the tournaments dataset */}
          <Route 
            path="/tournaments/dataset" 
            element={
              <PrivateRoute>
                <TournamentsPage />
              </PrivateRoute>
            }
          />

          {/* Private route for configuring tournament settings */}
          <Route 
            path="/tournaments/configure" 
            element={
              <PrivateRoute>
                <ConfigureTournamentPage />
              </PrivateRoute>
            }
          />

          {/* Private route for viewing the player dataset */}
          <Route 
            path="/players/dataset" 
            element={
              <PrivateRoute>
                <PlayerDatasetPage />
              </PrivateRoute>
            }
          />

          {/* Private route for creating a new player */}
          <Route 
            path="/players/create" 
            element={
              <PrivateRoute>
                <CreatePlayerPage />
              </PrivateRoute>
            }
          />

          {/* Private route for editing an existing player */}
          <Route 
            path="/players/edit" 
            element={
              <PrivateRoute>
                <EditPlayerPage />
              </PrivateRoute>
            }
          />

          {/* Fallback route for undefined paths */}
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
