import React from 'react';
import TournamentPage from './TournamentPage';

// Example data for completed tournaments
const completedTournaments = [...Array(10)].map((_, index) => ({
  id: `#909${index + 3}`,
  round: "2/3",
  matchesCompleted: "5/7",
  startDateTime: "04 Sep 2024 16:40",
  endDateTime: index % 2 === 0 ? "04 Sep 2024 16:58" : "-",
  players: ["Player A", "Player B", "Player C"], // You might want to put actual player names here
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
        // Add more matches for subsequent rounds as necessary
      ],
    },
  ],
}));

const CompletedTournamentsPage = () => (
  <TournamentPage
    title="Completed Tournaments"
    status="Completed"
    data={completedTournaments}
  />
);

export default CompletedTournamentsPage;
