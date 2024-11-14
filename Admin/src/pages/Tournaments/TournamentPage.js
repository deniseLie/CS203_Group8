// src/pages/TournamentPageWrapper.js
import React, { useState } from 'react';
import { Box, Typography, Button, ButtonGroup } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import TournamentTable from '../../components/TournamentTable';

// Example data for ongoing tournaments
const ongoingTournaments = [...Array(10)].map((_, index) => ({
  id: `#909${index + 3}`,
  round: "2/3",
  matchesCompleted: "5/7",
  status: 'Ongoing',
  startDateTime: "04 Sep 2024 16:40",
  endDateTime: index % 2 === 0 ? "04 Sep 2024 16:58" : "-",
  players: ["Player A", "Player B", "Player C"],
  rounds: [
    {
      roundNumber: 1,
      matches: [
        { id: "#1001", player1: "Player A", player2: "Player B", winner: "Player A" },
        { id: "#1002", player1: "Player C", player2: "Player D", winner: "Player C" },
      ],
    },
    {
      roundNumber: 2,
      matches: [
        { id: "#1003", player1: "Player A", player2: "Player C", winner: "Player A" },
      ],
    },
  ],
}));

// Example data for completed tournaments
const completedTournaments = [...Array(10)].map((_, index) => ({
  id: `#909${index + 3}`,
  round: "2/3",
  matchesCompleted: "5/7",
  status: 'Completed',
  startDateTime: "04 Sep 2024 16:40",
  endDateTime: index % 2 === 0 ? "04 Sep 2024 16:58" : "-",
  players: ["Player A", "Player B", "Player C"],
  rounds: [
    {
      roundNumber: 1,
      matches: [
        { id: "#1001", player1: "Player A", player2: "Player B", winner: null },
        { id: "#1002", player1: "Player C", player2: "Player D", winner: null },
      ],
    },
    {
      roundNumber: 2,
      matches: [
        { id: "#1003", player1: "Player A", player2: "Player C", winner: null },
      ],
    },
  ],
}));

const TournamentPageWrapper = () => {
  const [status, setStatus] = useState('Ongoing');
  const data = status === 'Ongoing' ? ongoingTournaments : completedTournaments;

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
          {status === 'Ongoing' ? "Ongoing Tournaments" : "Completed Tournaments"}
        </Typography>

        {/* Toggle Buttons */}
        <ButtonGroup sx={{ mb: 3 }}>
          <Button onClick={() => setStatus('Ongoing')} variant={status === 'Ongoing' ? 'contained' : 'outlined'}>
            Ongoing
          </Button>
          <Button onClick={() => setStatus('Completed')} variant={status === 'Completed' ? 'contained' : 'outlined'}>
            Completed
          </Button>
        </ButtonGroup>

        {/* Tournament Table */}
        <TournamentTable data={data} status={status} />
      </Box>
    </Box>
  );
};

export default TournamentPageWrapper;
