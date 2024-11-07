import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Dashboard from './pages/Dashboard/Dashboard';
// import Users from './pages/Users';
// import Reports from './pages/Reports';
import Login from './pages/Login/Login';
import OngoingTournamentsPage from './pages/Tournaments/OngoingTournamentsPage';
import CompletedTournamentsPage from './pages/Tournaments/CompletedTournamentsPage';
import PlayerDatasetPage from './pages/Players/Dataset';
import LeaderboardsPage from './pages/Players/Leaderboards';
import ConfigureTournamentPage from './pages/Tournaments/ConfigureTournamentPage';
import AddTournamentPage from './pages/Tournaments/AddTournamentPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/tournaments/ongoing" element={<OngoingTournamentsPage />} />
        <Route path="/tournaments/completed" element={<CompletedTournamentsPage />} />
        <Route path="/tournaments/configure" element={<ConfigureTournamentPage />} />
        <Route path="/tournaments/add" element={<AddTournamentPage />} />
        <Route path="/players/dataset" element={<PlayerDatasetPage />} />
        <Route path="/players/leaderboards" element={<LeaderboardsPage />} />
        {/* <Route path="*" element={<NotFound />} /> */}
        {/* <Route path="/users" element={<Users />} />
        <Route path="/reports" element={<Reports />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
