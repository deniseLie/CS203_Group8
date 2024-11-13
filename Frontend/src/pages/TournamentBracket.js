import React, { useState, useEffect } from 'react';
import { Box, Typography, Modal, Button } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import background from '../assets/backgrounds/cherrybackground_dark.png';
import MatchUp from '../components/MatchUp';
import { championsAssets, summonerIcons } from '../util/importAssets'; // Import assets

// Helper function to get a random champion from championsAssets
const getRandomChampion = () => {
  const championKeys = Object.keys(championsAssets).filter(key => key !== 'noChampion');
  const randomKey = championKeys[Math.floor(Math.random() * championKeys.length)];
  return {
    champion: randomKey.charAt(0).toUpperCase() + randomKey.slice(1), // Capitalize name for display
    championImg: championsAssets[randomKey],
  };
};

// Helper function to get a random summoner icon from summonerIcons
const getRandomSummonerIcon = () => {
  const randomIndex = Math.floor(Math.random() * summonerIcons.length);
  return summonerIcons[randomIndex];
};

// Static player data with randomized champions and summoner icons
const staticPlayerData = [a
];

const TournamentBracket = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const userData = location.state?.userData;

  const initialPlayerData = userData
    ? [{ ...userData, status: "pending" }, ...staticPlayerData.slice(0, 7)]
    : staticPlayerData;

  const shuffleArray = (array) => array.sort(() => Math.random() - 0.5);
  const [players, setPlayers] = useState(shuffleArray([...initialPlayerData]));
  const [round, setRound] = useState(1);
  const [showNextRoundModal, setShowNextRoundModal] = useState(false);
  const [finalWinner, setFinalWinner] = useState(null);
  const [eliminatedPlayers, setEliminatedPlayers] = useState([]);
  const [temporaryEliminations, setTemporaryEliminations] = useState([]); // Track eliminations temporarily

  const updatePlayerStatus = (playerName, newStatus) => {
    setPlayers((prevPlayers) =>
      prevPlayers.map((player) =>
        player.playerName === playerName ? { ...player, status: newStatus } : player
      )
    );

    if (newStatus === "lose") {
      const eliminatedPlayer = players.find((player) => player.playerName === playerName);
      setTemporaryEliminations((prev) => [eliminatedPlayer, ...prev]);
    }
  };

  const matchups = [];
  for (let i = 0; i < players.length; i += 2) {
    matchups.push({
      leftPlayer: players[i],
      rightPlayer: players[i + 1],
    });
  }

  useEffect(() => {
    const allMatchesCompleted = matchups.every(
      (match) => match.leftPlayer.status === "lose" || match.rightPlayer.status === "lose"
    );

    if (allMatchesCompleted) {
      const activePlayers = players.filter((player) => player.status !== "lose");

      if (activePlayers.length === 1) {
        setFinalWinner(activePlayers[0]);
        setTemporaryEliminations((prev) => [...prev, activePlayers[0]]);
      } else {
        setShowNextRoundModal(true);
      }
    }
  }, [players, matchups]);

  const handleNextRound = () => {
    const winners = players.filter((player) => player.status !== "lose");
    setPlayers(winners.map((player) => ({ ...player, status: "pending" })));
    setRound(round + 1);

    // Update the eliminatedPlayers list only when "Next Round" is confirmed
    setEliminatedPlayers((prev) => [...prev, ...temporaryEliminations]);
    setTemporaryEliminations([]); // Clear temporary eliminations after updating

    setShowNextRoundModal(false);
  };

  const handleGoToPostgame = () => {
    // Generate final rankings
    const rankings = generateFinalRankings();
  
    // Check if the current player is already in the rankings
    const isCurrentPlayerInRankings = rankings.some(player => player.playerName === userData.playerName);
  
    // If the current player isn't already in the rankings, add them at the correct position
    if (!isCurrentPlayerInRankings) {
      const currentPlayerRanking = {
        standing: `${rankings.length + 1}`, 
        champion: userData.champion,
        playerName: userData.playerName,
        playerIcon: userData.playerIcon,
        kd: "8/0", 
        kda: "8.0 KDA"
      };
      
      rankings.push(currentPlayerRanking);  
    }
  
    // Navigate to postgame with the final rankings and current player data
    navigate("/postgame", { state: { rankings, currentPlayer: userData } });
  };
  

  const generateFinalRankings = () => {
    // Combine eliminated and temporary eliminations, ensuring no duplicates by playerName
    const allEliminatedPlayers = [...eliminatedPlayers, ...temporaryEliminations];
    
    // Use a Set to track unique player names and filter duplicates
    const uniqueEliminatedPlayers = [];
    const seenPlayerNames = new Set();
    
    allEliminatedPlayers.reverse().forEach((player) => {
      if (!seenPlayerNames.has(player.playerName)) {
        seenPlayerNames.add(player.playerName);
        uniqueEliminatedPlayers.push(player);
      }
    });
    
    // Ensure current player is included in the rankings, even if they weren't eliminated
    const isCurrentPlayerInRankings = uniqueEliminatedPlayers.some(player => player.playerName === userData.playerName);
    if (!isCurrentPlayerInRankings) {
      uniqueEliminatedPlayers.push({
        playerName: userData.playerName,
        champion: userData.champion,
        playerIcon: userData.playerIcon,
        kd: "8/0",
        kda: "8.0 KDA",
      });
    }
  
    // Generate the rankings based on unique eliminated players
    const rankings = uniqueEliminatedPlayers.map((player, index) => {
      const rankSuffix = ["ST", "ND", "RD", "TH"];
      const standing = (index + 1) + (rankSuffix[index] || "TH");
  
      return {
        standing,
        champion: player.champion,
        playerName: player.playerName,
        playerIcon: player.playerIcon,
        kd: player.kd || "8/0", // Placeholder K/D data
        kda: player.kda || "8.0 KDA" // Placeholder KDA data
      };
    });
  
    return rankings;
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
              key={`left-${index}`}
              leftPlayer={matchup.leftPlayer}
              rightPlayer={matchup.rightPlayer}
              updatePlayerStatus={updatePlayerStatus}
            />
          ))}
        </Box>

        <Box display="flex" flexDirection="column" alignItems="flex-start" spacing={3}>
          {matchups.slice(Math.ceil(matchups.length / 2)).map((matchup, index) => (
            <MatchUp
              key={`right-${index}`}
              leftPlayer={matchup.leftPlayer}
              rightPlayer={matchup.rightPlayer}
              updatePlayerStatus={updatePlayerStatus}
            />
          ))}
        </Box>
      </Box>

      <Modal open={showNextRoundModal} onClose={() => setShowNextRoundModal(false)}>
        <Box sx={{
          position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)",
          width: 300, bgcolor: "#0f1a24", color: "white", boxShadow: 24, p: 4, borderRadius: 2,
          border: "1px solid #7B5F2D", textAlign: "center"
        }}>
          <Typography variant="h6" sx={{ mb: 2 }}>Round {round} Complete!</Typography>
          <Typography variant="body2" sx={{ mb: 3 }}>Ready to proceed to the next round?</Typography>
          <Button variant="contained" onClick={handleNextRound} sx={{
            color: "#D4AF37", bgcolor: "#0f1a24", border: 1, borderColor: "#7B5F2D", "&:hover": { bgcolor: "#183449" }
          }}>
            Next Round
          </Button>
        </Box>
      </Modal>

      <Modal open={Boolean(finalWinner)} onClose={handleGoToPostgame}>
        <Box sx={{
          position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)",
          width: 300, bgcolor: "#0f1a24", color: "white", boxShadow: 24, p: 4, borderRadius: 2,
          border: "1px solid #7B5F2D", textAlign: "center"
        }}>
          <Typography variant="h6" sx={{ mb: 2 }}>Tournament Winner!</Typography>
          <Typography variant="body2" sx={{ mb: 3 }}>Congratulations, {finalWinner?.playerName}!</Typography>
          <Button variant="contained" onClick={handleGoToPostgame} sx={{
            color: "#D4AF37", bgcolor: "#0f1a24", border: 1, borderColor: "#7B5F2D", "&:hover": { bgcolor: "#183449" }
          }}>
            Go to Postgame
          </Button>
        </Box>
      </Modal>
    </Box>
  );
};

export default TournamentBracket;
