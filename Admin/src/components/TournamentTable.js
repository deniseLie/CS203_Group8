import React, { useState } from 'react';
import { Box, Typography, Button, MenuItem, Stack, FormControl, Select, InputLabel, TextField, Avatar, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import MatchesPopup from './MatchesPopup';
import playerProfile from '../assets/playerpfp.jpg'; // Make sure this image path is correct

const TournamentTable = ({ data }) => {
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [showPopup, setShowPopup] = useState(false);
  const [filteredData, setFilteredData] = useState(data);
  const [statusFilter, setStatusFilter] = useState('');
  const [playerFilter, setPlayerFilter] = useState('');
  const [tournamentIdFilter, setTournamentIdFilter] = useState('');

  // Handle status filter change
  const handleStatusChange = (e) => {
    setStatusFilter(e.target.value);
  };

  // Handle player filter change
  const handlePlayerChange = (e) => {
    setPlayerFilter(e.target.value);
  };

  // Handle tournament ID filter change
  const handleTournamentIdChange = (e) => {
    setTournamentIdFilter(e.target.value);
  };

  // Filter the tournaments based on filter criteria
  const handleFilter = () => {
    let filtered = data;

    // Apply status filter
    if (statusFilter) {
      filtered = filtered.filter((tournament) => tournament.status === statusFilter);
    }

    // Apply player filter (filtering by player name in the players array)
    if (playerFilter) {
      filtered = filtered.filter((tournament) =>
        tournament.players.some(player => player.toLowerCase().includes(playerFilter.toLowerCase()))
      );
    }

    // Apply tournament ID filter
    if (tournamentIdFilter) {
      filtered = filtered.filter((tournament) => tournament.id.includes(tournamentIdFilter));
    }

    setFilteredData(filtered); // Update filtered data
  };

  const handleTournamentClick = (tournament) => {
    setSelectedTournament(tournament); // Set the selected tournament
    setShowPopup(true); // Show the popup
  };

  const handleClosePopup = () => {
    setShowPopup(false); // Close the popup
  };

  const handleModifyMatch = (matchId) => {
    console.log(`Modify match with ID: ${matchId}`);
    // Logic for modifying match details can go here
  };

  return (
    <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
      {/* Filter Bar */}
      <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
        <FormControl size="small">
          <InputLabel>Status</InputLabel>
          <Select value={statusFilter} onChange={handleStatusChange}>
            <MenuItem value="">All</MenuItem>
            <MenuItem value="Ongoing">Ongoing</MenuItem>
            <MenuItem value="Completed">Completed</MenuItem>
          </Select>
        </FormControl>

        <TextField
          label="Search Player"
          variant="outlined"
          size="small"
          value={playerFilter}
          onChange={handlePlayerChange}
        />

        <TextField
          label="Search Tournament Id"
          variant="outlined"
          size="small"
          value={tournamentIdFilter}
          onChange={handleTournamentIdChange}
        />

        <Button variant="text" sx={{ color: '#d32f2f', textTransform: 'none' }} onClick={handleFilter}>
          Filter
        </Button>
      </Stack>

      {/* Tournament Table */}
      <Typography variant="h6" sx={{ mb: 2 }}>Most Recent Tournaments</Typography>
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
                <Button size="small" variant="contained" color={tournament.status === "Ongoing" ? "warning" : "success"}>
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
