import React, { useState } from 'react';
import {
  Box,
  Typography,
  Button,
  MenuItem,
  Stack,
  FormControl,
  Select,
  InputLabel,
  TextField,
  Avatar,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from '@mui/material';
import MatchesPopup from './MatchesPopup'; // Component to show and modify tournament matches
import playerProfile from '../assets/playerpfp.jpg'; // Default player profile image

/**
 * TournamentTable Component
 *
 * This component displays a table of tournaments with filtering options and an option
 * to view tournament matches in a popup. Users can filter tournaments by status, player name,
 * or tournament ID.
 *
 * @param {object[]} data - The list of tournament data to display.
 */
const TournamentTable = ({ data }) => {
  const [selectedTournament, setSelectedTournament] = useState(null); // Selected tournament for viewing matches
  const [showPopup, setShowPopup] = useState(false); // Controls popup visibility
  const [filteredData, setFilteredData] = useState(data); // State for filtered tournaments
  const [statusFilter, setStatusFilter] = useState(''); // Status filter value
  const [playerFilter, setPlayerFilter] = useState(''); // Player name filter value
  const [tournamentIdFilter, setTournamentIdFilter] = useState(''); // Tournament ID filter value

  /**
   * Handle changes to the status filter.
   *
   * @param {object} e - Event object from the Select component.
   */
  const handleStatusChange = (e) => {
    setStatusFilter(e.target.value);
  };

  /**
   * Handle changes to the player filter.
   *
   * @param {object} e - Event object from the TextField component.
   */
  const handlePlayerChange = (e) => {
    setPlayerFilter(e.target.value);
  };

  /**
   * Handle changes to the tournament ID filter.
   *
   * @param {object} e - Event object from the TextField component.
   */
  const handleTournamentIdChange = (e) => {
    setTournamentIdFilter(e.target.value);
  };

  /**
   * Filter the tournaments based on the current filter values.
   */
  const handleFilter = () => {
    let filtered = data;

    // Filter by status
    if (statusFilter) {
      filtered = filtered.filter((tournament) => tournament.status === statusFilter);
    }

    // Filter by player name
    if (playerFilter) {
      filtered = filtered.filter((tournament) =>
        tournament.players.some((player) =>
          player.toLowerCase().includes(playerFilter.toLowerCase())
        )
      );
    }

    // Filter by tournament ID
    if (tournamentIdFilter) {
      filtered = filtered.filter((tournament) => tournament.id.includes(tournamentIdFilter));
    }

    setFilteredData(filtered); // Update the filtered data
  };

  /**
   * Handle clicking on a tournament ID to open the matches popup.
   *
   * @param {object} tournament - The selected tournament.
   */
  const handleTournamentClick = (tournament) => {
    setSelectedTournament(tournament); // Set the selected tournament
    setShowPopup(true); // Show the matches popup
  };

  /**
   * Close the matches popup.
   */
  const handleClosePopup = () => {
    setShowPopup(false); // Hide the matches popup
  };

  /**
   * Placeholder function for modifying a match (stub for future implementation).
   *
   * @param {string} matchId - The ID of the match to modify.
   */
  const handleModifyMatch = (matchId) => {
    console.log(`Modify match with ID: ${matchId}`);
    // Add logic for modifying match details here
  };

  return (
    <Box
      sx={{
        backgroundColor: '#ffffff', // White background
        borderRadius: '8px', // Rounded corners
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', // Subtle shadow for depth
        p: 2, // Padding around the content
      }}
    >
      {/* Filter Bar */}
      <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
        {/* Status Filter */}
        <FormControl size="small">
          <InputLabel>Status</InputLabel>
          <Select value={statusFilter} onChange={handleStatusChange}>
            <MenuItem value="">All</MenuItem>
            <MenuItem value="Ongoing">Ongoing</MenuItem>
            <MenuItem value="Completed">Completed</MenuItem>
          </Select>
        </FormControl>

        {/* Player Name Filter */}
        <TextField
          label="Search Player"
          variant="outlined"
          size="small"
          value={playerFilter}
          onChange={handlePlayerChange}
        />

        {/* Tournament ID Filter */}
        <TextField
          label="Search Tournament Id"
          variant="outlined"
          size="small"
          value={tournamentIdFilter}
          onChange={handleTournamentIdChange}
        />

        {/* Apply Filters Button */}
        <Button
          variant="text"
          sx={{ color: '#d32f2f', textTransform: 'none' }}
          onClick={handleFilter}
        >
          Filter
        </Button>
      </Stack>

      {/* Tournament Table */}
      <Typography variant="h6" sx={{ mb: 2 }}>
        Most Recent Tournaments
      </Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Tournament Id</TableCell>
            <TableCell>Round No.</TableCell>
            <TableCell>Matches Completed</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Players</TableCell>
            <TableCell>Start Date-Time</TableCell>
            <TableCell>End Date-Time</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {filteredData.map((tournament, index) => (
            <TableRow key={index}>
              <TableCell>
                <Button onClick={() => handleTournamentClick(tournament)}>{tournament.id}</Button>
              </TableCell>
              <TableCell>{tournament.round}</TableCell>
              <TableCell>{tournament.matchesCompleted}</TableCell>
              <TableCell>
                <Button
                  size="small"
                  variant="contained"
                  color={tournament.status === 'Ongoing' ? 'warning' : 'success'}
                >
                  {tournament.status}
                </Button>
              </TableCell>
              <TableCell>
                <Stack direction="row" spacing={1}>
                  {tournament.players.map((_, i) => (
                    <Avatar key={i} src={playerProfile} sx={{ width: 24, height: 24 }} />
                  ))}
                  <Typography>+{tournament.players.length}</Typography>
                </Stack>
              </TableCell>
              <TableCell>{tournament.startDateTime}</TableCell>
              <TableCell>{tournament.endDateTime}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Typography sx={{ mt: 2, fontSize: '0.875rem', color: 'text.secondary' }}>
        Showing {filteredData.length} of {data.length} tournaments
      </Typography>

      {/* Matches Popup */}
      {showPopup && selectedTournament && (
        <MatchesPopup
          tournament={selectedTournament} // Pass the selected tournament object
          onClose={handleClosePopup}
          onModify={handleModifyMatch}
        />
      )}
    </Box>
  );
};

export default TournamentTable;
