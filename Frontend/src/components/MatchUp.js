import React, { useState } from 'react';
import { Box, Typography, Modal, Button, Select, MenuItem, Grid } from "@mui/material";
import PlayerCard from "./PlayerCard";
import axios from 'axios'; // Ensure axios is imported
import diamondRank from '../assets/rankIcon/diamond.png';

const MatchUp = ({ 
  leftPlayer, 
  rightPlayer, 
  updatePlayerStatus, 
  tournamentId, 
  roundNumber 
}) => {
  const [openModal, setOpenModal] = useState(false);
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [selectedOption, setSelectedOption] = useState("lose");
  const [matchTime, setMatchTime] = useState("");

  const handlePlayerClick = (clickedPlayer) => {
    setSelectedPlayer(clickedPlayer);
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedOption("lose");
    setMatchTime("");
  };

  // determine who win or lose
  const handleSubmit = async () => {
    if (selectedPlayer) {
      const isLeftPlayer = selectedPlayer.playerName === leftPlayer.playerName;
      const winnerPlayer = isLeftPlayer ? rightPlayer : leftPlayer;
      const loserPlayer = isLeftPlayer ? leftPlayer : rightPlayer;

      const newStatus = selectedOption === "afk" ? "AFK" : "lose";
      const deathTime = newStatus === "lose" ? parseInt(matchTime, 10) : 0;

      // Update local state
      updatePlayerStatus(loserPlayer.playerName, newStatus, deathTime);

      // Call the API
      try {
        await axios.post('/admin/tournaments/round', {
          tournamentId,
          firstPlayerId: leftPlayer.playerId, // Replace with actual IDs
          secondPlayerId: rightPlayer.playerId, // Replace with actual IDs
          winnerPlayerId: winnerPlayer.playerId, // Replace with actual ID
          roundNumber,
          status: newStatus,
          deathTime,
        });
      } catch (error) {
        console.error("Failed to submit match result:", error);
      }
    }
    handleCloseModal();
  };

  return (
    <Box
      sx={{
        width: '100%',
        padding: 2,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        my: 1,
        borderBottom: 1.5,
        borderBottomColor: '#A3C7C7',
      }}
    >
      <PlayerCard 
        name={leftPlayer.playerName}
        rankIcon={diamondRank}
        playerIcon={leftPlayer.playerIcon}
        championImg={leftPlayer.championImg}
        rank={leftPlayer.rank}
        status={leftPlayer.status}
        onClick={() => handlePlayerClick(leftPlayer)}
      />
      <Typography variant="h5" sx={{ color: 'white', fontWeight: 'bold', mx: 2 }}>
        VS
      </Typography>
      <PlayerCard 
        name={rightPlayer.playerName}
        rankIcon={diamondRank}
        playerIcon={rightPlayer.playerIcon}
        championImg={rightPlayer.championImg}
        rank={rightPlayer.rank}
        status={rightPlayer.status}
        onClick={() => handlePlayerClick(rightPlayer)}
      />

      {/* Modal */}
      <Modal
        open={openModal}
        onClose={handleCloseModal}
        aria-labelledby="modal-title"
        aria-describedby="modal-description"
      >
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            width: 320,
            bgcolor: '#0f1a24',
            color: 'white',
            boxShadow: 24,
            p: 4,
            borderRadius: 2,
            border: '1px solid #7B5F2D'
          }}
        >
          <Typography className="headerPrimary" id="modal-title" sx={{ mb: 2 }}>
            UPDATE STATUS FOR {selectedPlayer ? selectedPlayer.playerName : ""}
          </Typography>
          <Typography className="headerPrimary" sx={{ mb: 1, fontWeight: 'bold' }}>STATUS</Typography>
          <Select
            value={selectedOption}
            onChange={(e) => setSelectedOption(e.target.value)}
            displayEmpty
            sx={{
              width: '100%',
              mb: 2,
              bgcolor: '#0f1a24',
              color: '#D4AF37',
              border: '1px solid #7B5F2D',
            }}
          >
            <MenuItem value="lose">LOSE</MenuItem>
            <MenuItem value="afk">AFK</MenuItem>
          </Select>

          {selectedOption === "lose" && (
            <>
              <Typography sx={{ mb: 1, fontWeight: 'bold' }}>DEATH TIME</Typography>
              <Grid container spacing={1} sx={{ mb: 2 }}>
                {['30', '60', '90', '120', '150'].map((time) => (
                  <Grid item xs={4} key={time}>
                    <Button
                      variant="outlined"
                      onClick={() => setMatchTime(time)}
                      sx={{
                        width: '100%',
                        color: matchTime === time ? 'white' : '#D4AF37',
                        borderColor: '#7B5F2D',
                        backgroundColor: matchTime === time ? '#1a2732' : 'transparent'
                      }}
                    >
                      {`${Math.floor(time / 60)}:${(time % 60).toString().padStart(2, '0')}`}
                    </Button>
                  </Grid>
                ))}
              </Grid>
            </>
          )}

          <Button
            variant="contained"
            onClick={handleSubmit}
            sx={{
              width: '100%',
              bgcolor: '#0F171D',
              border: 1,
              borderColor: '#D4AF37',
              color: '#D4AF37',
              fontWeight: 'bold',
              mt: 2
            }}
          >
            CONFIRM
          </Button>
        </Box>
      </Modal>
    </Box>
  );
};

export default MatchUp;
