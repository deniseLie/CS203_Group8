import React, { useState, useEffect } from 'react';
import { Box, Typography, Tabs, Tab, CircularProgress, Alert } from '@mui/material';
import Sidebar from '../../components/Sidebar'; // Sidebar component for navigation
import TopBar from '../../components/TopBar'; // Top bar component for page title and actions
import TournamentTable from '../../components/TournamentTable'; // Component to display the tournament list
import EditMatchModal from '../../components/EditMatchModal'; // Modal component for editing match details

/**
 * TournamentsPage Component
 *
 * This component displays a list of tournaments with options to filter by status (Ongoing or Completed),
 * edit matches within a tournament, and manage tournament data using API calls.
 */
const TournamentsPage = () => {
  const [tournaments, setTournaments] = useState([]); // List of all tournaments
  const [selectedStatus, setSelectedStatus] = useState('Ongoing'); // Current status filter
  const [loading, setLoading] = useState(true); // Indicates whether the data is being fetched
  const [error, setError] = useState(null); // Stores error messages, if any
  const [selectedMatch, setSelectedMatch] = useState(null); // Match being edited
  const [editingTournament, setEditingTournament] = useState(null); // Tournament being edited

  /**
   * Fetch tournament data from the API on component mount.
   */
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setLoading(true); // Start loading indicator
        setError(null); // Clear any previous errors
        const response = await fetch('/admin/tournaments/getAllTournaments'); // API endpoint to fetch tournaments

        if (!response.ok) {
          throw new Error(`Failed to fetch: ${response.statusText}`);
        }

        const data = await response.json(); // Parse the JSON response
        setTournaments(data); // Store fetched tournaments in state
      } catch (error) {
        setError('Failed to fetch tournament data. Please try again later.');
        console.error('Error fetching tournament data:', error);
      } finally {
        setLoading(false); // Stop loading indicator
      }
    };

    fetchTournaments(); // Fetch tournaments on component mount
  }, []);

  /**
   * Filter tournaments based on the selected status.
   */
  const filteredTournaments = tournaments.filter(
    (tournament) => tournament.status === selectedStatus
  );

  /**
   * Handle the edit match action.
   *
   * @param {object} tournament - The tournament object.
   * @param {object} match - The match object to edit.
   */
  const handleEditMatch = (tournament, match) => {
    setEditingTournament(tournament); // Set the tournament being edited
    setSelectedMatch(match); // Set the match to be edited
  };

  /**
   * Save the updated match details.
   *
   * @param {object} updatedMatch - The updated match details.
   */
  const handleSaveMatch = async (updatedMatch) => {
    try {
      // Send a POST request to save the match details
      const response = await fetch('/admin/tournaments/round', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          tournamentId: editingTournament.tournamentId,
          firstPlayerId: updatedMatch.firstPlayerId,
          secondPlayerId: updatedMatch.secondPlayerId,
          winnerPlayerId: updatedMatch.winnerPlayerId,
          roundNumber: updatedMatch.roundNumber,
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to update match.');
      }

      const result = await response.json();
      console.log('Match updated:', result.message);

      // Update the local tournament state with the updated match details
      setTournaments((prev) =>
        prev.map((tournament) =>
          tournament.tournamentId === editingTournament.tournamentId
            ? {
                ...tournament,
                rounds: tournament.rounds.map((round) =>
                  round.roundNumber === updatedMatch.roundNumber
                    ? {
                        ...round,
                        matches: round.matches.map((match) =>
                          match.id === updatedMatch.id ? updatedMatch : match
                        ),
                      }
                    : round
                ),
              }
            : tournament
        )
      );

      setEditingTournament(null); // Reset the tournament being edited
      setSelectedMatch(null); // Reset the match being edited
    } catch (error) {
      console.error('Error saving match:', error);
      setError('Failed to save match details. Please try again.');
    }
  };

  /**
   * Close the match editing modal.
   */
  const handleCloseModal = () => {
    setEditingTournament(null);
    setSelectedMatch(null);
  };

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Sidebar for navigation */}
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
          value={selectedStatus} // Controlled value for the selected tab
          onChange={(e, newValue) => setSelectedStatus(newValue)} // Handle tab change
          sx={{ mb: 3 }}
        >
          <Tab label="Ongoing" value="Ongoing" /> {/* Tab for Ongoing tournaments */}
          <Tab label="Completed" value="Completed" /> {/* Tab for Completed tournaments */}
        </Tabs>

        {/* Loading State */}
        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <CircularProgress /> {/* Loading spinner */}
          </Box>
        )}

        {/* Error State */}
        {error && (
          <Box sx={{ mt: 3 }}>
            <Alert severity="error">{error}</Alert> {/* Display error message */}
          </Box>
        )}

        {/* Tournament Table */}
        {!loading && !error && (
          <TournamentTable
            data={filteredTournaments} // Pass filtered tournaments to the table
            onEditMatch={handleEditMatch} // Pass the edit match handler
          />
        )}

        {/* Edit Match Modal */}
        {selectedMatch && editingTournament && (
          <EditMatchModal
            match={selectedMatch} // Pass the match to edit
            onClose={handleCloseModal} // Close the modal
            onSave={handleSaveMatch} // Save the updated match
          />
        )}
      </Box>
    </Box>
  );
};

export default TournamentsPage;
