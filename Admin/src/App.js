import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
// import Users from './pages/Users';
// import Reports from './pages/Reports';
import Login from './pages/Login';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        {/* <Route path="/users" element={<Users />} />
        <Route path="/reports" element={<Reports />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
