import React, { useState, useEffect } from 'react';
import { Box, Typography, Tabs, Tab, CircularProgress, Alert } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import TournamentTable from '../../components/TournamentTable';

const TournamentsPage = () => {
  const [tournaments, setTournaments] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState('Ongoing');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch tournaments data from API
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await fetch('/admin/tournaments'); // Adjust API endpoint as needed
        if (!response.ok) {
          throw new Error(`Failed to fetch: ${response.statusText}`);
        }
        const data = await response.json();
        setTournaments(data); // Assuming API returns an array of tournaments
      } catch (error) {
        setError('Failed to fetch tournament data. Please try again later.');
        console.error('Error fetching tournament data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTournaments();
  }, []);

  // Filter tournaments based on the selected status
  const filteredTournaments = tournaments.filter(
    (tournament) => tournament.status === selectedStatus
  );

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        {/* Top Bar */}
        <TopBar />

        {/* Page Title */}
        <Typography variant="h4" sx={{ mb: 3 }}>
          Tournaments
        </Typography>

        {/* Filter Tabs */}
        <Tabs
          value={selectedStatus}
          onChange={(e, newValue) => setSelectedStatus(newValue)}
          sx={{ mb: 3 }}
        >
          <Tab label="Ongoing" value="Ongoing" />
          <Tab label="Completed" value="Completed" />
        </Tabs>

        {/* Loading and Error States */}
        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <CircularProgress />
          </Box>
        )}
        {error && (
          <Box sx={{ mt: 3 }}>
            <Alert severity="error">{error}</Alert>
          </Box>
        )}

        {/* Tournament Table */}
        {!loading && !error && (
          <TournamentTable data={filteredTournaments} status={selectedStatus} />
        )}
      </Box>
    </Box>
  );
};

export default TournamentsPage;
