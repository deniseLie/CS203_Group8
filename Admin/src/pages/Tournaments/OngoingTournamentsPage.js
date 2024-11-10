import React from 'react';
import TournamentPage from './TournamentPage';

// Example data for ongoing tournaments
const ongoingTournaments = [...Array(10)].map((_, index) => ({
  id: `#909${index + 3}`,
  round: "2/3",
  matchesCompleted: "5/7",
  status: 'Ongoing',
  startDateTime: "04 Sep 2024 16:40",
  endDateTime: index % 2 === 0 ? "04 Sep 2024 16:58" : "-",
  players: ["Player A", "Player B", "Player C"], // You might want to put actual player names here
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
        // Add more matches for subsequent rounds as necessary
      ],
    },
  ],
}));

const OngoingTournamentsPage = () => (
  <TournamentPage
    title="Tournaments"
    status="Ongoing"
    data={ongoingTournaments}
  />
);

export default OngoingTournamentsPage;
