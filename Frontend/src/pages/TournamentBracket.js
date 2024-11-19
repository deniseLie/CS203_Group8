import React, { useState, useEffect } from 'react';
import { Box, Typography, Modal, Button } from '@mui/material';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import background from '../assets/backgrounds/cherrybackground_dark.png';
import MatchUp from '../components/MatchUp';

const TournamentBracket = () => {
  const { tournamentId } = useParams();
  const navigate = useNavigate();
  const [tournamentData, setTournamentData] = useState(null);
  const [players, setPlayers] = useState([]);
  const [matchups, setMatchups] = useState([]);
  const [round, setRound] = useState(1);
  const [showNextRoundModal, setShowNextRoundModal] = useState(false);

  // Fetch tournament details on mount
  useEffect(() => {
    const fetchTournamentDetails = async () => {
      try {
        const response = await axios.get(`${env.LOGIN_SERVER_URL}/admin/tournaments/${tournamentId}`);
        const data = response.data;
        setTournamentData(data);
        setPlayers(data.players); // Set players from the API response
        setMatchups(generateMatchups(data.players)); // Generate initial matchups
      } catch (error) {
        console.error("Failed to fetch tournament details:", error);
      }
    };

    fetchTournamentDetails();
  }, [tournamentId]);

  // Generate matchups from player data
  const generateMatchups = (players) => {
    const matchups = [];
    for (let i = 0; i < players.length; i += 2) {
      matchups.push({
        leftPlayer: players[i],
        rightPlayer: players[i + 1] || null, // Handle odd number of players
      });
    }
    return matchups;
  };

  // Update player statuses and proceed to the next round
  const handleNextRound = async () => {
    try {
      const response = await axios.get(`${env.LOGIN_SERVER_URL}/admin/tournaments/${tournamentId}`);
      const updatedData = response.data;

      setTournamentData(updatedData);
      const remainingPlayers = updatedData.players.filter(
        (player) => player.standing !== null // Only include players still in the tournament
      );

      if (remainingPlayers.length === 1) {
        // If there's only one player left, navigate to postgame
        navigate("/postgame", { state: { results: updatedData.players } });
      } else {
        // Update players and matchups for the next round
        setPlayers(remainingPlayers);
        setMatchups(generateMatchups(remainingPlayers));
        setRound(round + 1);
        setShowNextRoundModal(false);
      }
    } catch (error) {
      console.error("Failed to fetch updated tournament data:", error);
    }
  };

  // Navigate to postgame screen with final results
  const handleGoToPostgame = () => {
    navigate("/postgame", { state: { results: tournamentData.players } });
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        width: "100%",
        height: "100vh",
        backgroundImage: `url(${background})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <Box sx={{ textAlign: "center", color: "white", mt: 5 }}>
        <Typography variant="h4" fontWeight="bold" color="#F0E6D2" className="headerPrimary">
          ROUND {round}
        </Typography>
      </Box>

      <Box sx={{ display: "flex", justifyContent: "space-between", width: "80%", mt: 4 }}>
        <Box display="flex" flexDirection="column" alignItems="flex-end" spacing={3}>
          {matchups.slice(0, Math.ceil(matchups.length / 2)).map((matchup, index) => (
            <MatchUp
              key={index}
              leftPlayer={matchup.leftPlayer}
              rightPlayer={matchup.rightPlayer}
              updatePlayerStatus={() => {}}
            />
          ))}
        </Box>

        <Box display="flex" flexDirection="column" alignItems="flex-start" spacing={3}>
          {matchups.slice(Math.ceil(matchups.length / 2)).map((matchup, index) => (
            <MatchUp
              key={`right-${index}`}
              leftPlayer={matchup.leftPlayer}
              rightPlayer={matchup.rightPlayer}
              updatePlayerStatus={() => {}}
            />
          ))}
        </Box>
      </Box>

      {/* Next Round Modal */}
      <Modal open={showNextRoundModal} onClose={() => setShowNextRoundModal(false)}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 300,
            bgcolor: "#0f1a24",
            color: "white",
            boxShadow: 24,
            p: 4,
            borderRadius: 2,
            border: "1px solid #7B5F2D",
            textAlign: "center",
          }}
        >
          <Typography variant="h6" sx={{ mb: 2 }}>Round {round} Complete!</Typography>
          <Typography variant="body2" sx={{ mb: 3 }}>Ready to proceed to the next round?</Typography>
          <Button
            variant="contained"
            onClick={handleNextRound}
            sx={{
              color: "#D4AF37",
              bgcolor: "#0f1a24",
              border: 1,
              borderColor: "#7B5F2D",
              "&:hover": { bgcolor: "#183449" },
            }}
          >
            Next Round
          </Button>
        </Box>
      </Modal>

      {/* Tournament Winner Modal */}
      {tournamentData && tournamentData.players.length === 1 && (
        <Modal open={Boolean(tournamentData)} onClose={handleGoToPostgame}>
          <Box
            sx={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              width: 300,
              bgcolor: "#0f1a24",
              color: "white",
              boxShadow: 24,
              p: 4,
              borderRadius: 2,
              border: "1px solid #7B5F2D",
              textAlign: "center",
            }}
          >
            <Typography variant="h6" sx={{ mb: 2 }}>Tournament Winner!</Typography>
            <Typography variant="body2" sx={{ mb: 3 }}>
              Congratulations, {tournamentData.players[0].playerName}!
            </Typography>
            <Button
              variant="contained"
              onClick={handleGoToPostgame}
              sx={{
                color: "#D4AF37",
                bgcolor: "#0f1a24",
                border: 1,
                borderColor: "#7B5F2D",
                "&:hover": { bgcolor: "#183449" },
              }}
            >
              Go to Postgame
            </Button>
          </Box>
        </Modal>
      )}
    </Box>
  );
};

export default TournamentBracket;
