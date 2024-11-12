import { Box, Typography, Modal, Button, Select, MenuItem, Grid, Input } from "@mui/material";
import PlayerCard from "./PlayerCard";
import diamondRank from '../assets/rankIcon/diamond.png';
import axios from 'axios';
import { useState } from "react";

const MatchUp = ({ leftPlayer, rightPlayer }) => {
  const [openModal, setOpenModal] = useState(false);
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [selectedOption, setSelectedOption] = useState("lose"); // Default to "lose"
  const [matchTime, setMatchTime] = useState(""); // Match time for lose
  const [isCustomInput, setIsCustomInput] = useState(false); // Track if custom input is selected

  const handlePlayerClick = (clickedPlayer) => {
    setSelectedPlayer(clickedPlayer);
    setOpenModal(true); // Open modal when a player is clicked
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setSelectedOption("lose");
    setMatchTime("");
    setIsCustomInput(false);
  };

  const handleSubmit = async () => {
    try {
      const status = selectedOption;
      selectedPlayer.status = status;

      await axios.post('/api/player-status', {
        playerName: selectedPlayer.playerName,
        status: status,
        matchTime: status === "lose" ? matchTime : undefined
      });

      handleCloseModal(); // Close the modal after submission
    } catch (error) {
      console.error("Error updating player status:", error);
    }
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
      {/* First player */}
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
      {/* Second player */}
      <PlayerCard 
        name={rightPlayer.playerName} 
        rankIcon={diamondRank} 
        playerIcon={rightPlayer.playerIcon} 
        championImg={rightPlayer.championImg} 
        rank={rightPlayer.rank} 
        status={rightPlayer.status} 
        onClick={() => handlePlayerClick(rightPlayer)}
      />

      {/* Modal with custom styling */}
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
            className="headerPrimary"
            sx={{
              width: '100%',
              mb: 2,
              bgcolor: '#0f1a24',
              color: '#D4AF37',
              border: '1px solid #7B5F2D',
              '& .MuiSvgIcon-root': {
                color: '#D4AF37'
              }
            }}
          >
            <MenuItem className="headerPrimary" value="lose">LOSE</MenuItem>
            <MenuItem className="headerPrimary" value="afk">AFK</MenuItem>
          </Select>

          {selectedOption === "lose" && (
            <>
              <Typography className="headerPrimary" sx={{ mb: 1, fontWeight: 'bold' }}>DEATH TIME</Typography>
              <Grid container spacing={1} sx={{ mb: 2 }}>
                {['30', '60', '90', '120', '150'].map((time) => (
                  <Grid item xs={4} key={time}>
                    <Button
                      variant="outlined"
                      onClick={() => {
                        setMatchTime(time);
                        setIsCustomInput(false);
                      }}
                      className="headerPrimary"
                      sx={{
                        width: '100%',
                        color: matchTime === time ? 'white' : '#D4AF37',
                        borderColor: '#7B5F2D',
                        '&:hover': {
                          bgcolor: '#1a2732',
                        },
                        backgroundColor: matchTime === time ? '#1a2732' : 'transparent'
                      }}
                    >
                      {`${Math.floor(time / 60)}:${(time % 60).toString().padStart(2, '0')}`}
                    </Button>
                  </Grid>
                ))}

                {/* Custom Input Option */}
                <Grid item xs={4}>
                  {isCustomInput ? (
                    <Input
                      placeholder="0:25"
                      value={matchTime}
                      onChange={(e) => setMatchTime(e.target.value)}
                      onBlur={() => setIsCustomInput(false)}
                      autoFocus
                      className="headerPrimary"
                      sx={{
                        width: '100%',
                        color: 'white',
                        border: '1px solid #7B5F2D',
                        borderRadius: 1,
                        textAlign: 'center',
                        '& input': {
                          padding: '4px 8px',
                          color: '#D4AF37'
                        }
                      }}
                    />
                  ) : (
                    <Button
                      variant="outlined"
                      onClick={() => {
                        setIsCustomInput(true);
                        setMatchTime(""); // Clear previous custom input
                      }}
                      className="headerPrimary"
                      sx={{
                        width: '100%',
                        color: '#D4AF37',
                        borderColor: '#7B5F2D',
                        '&:hover': {
                          bgcolor: '#1a2732',
                        }
                      }}
                    >
                      Custom
                    </Button>
                  )}
                </Grid>
              </Grid>
            </>
          )}

          <Button
            variant="contained"
            onClick={handleSubmit}
            className="headerPrimary"
            sx={{
              width: '100%',
              bgcolor: '#0F171D',
              border: 1,
              borderColor: '#D4AF37',
              color: '#D4AF37',
              '&:hover': {
                bgcolor: '#183449',
              },
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
