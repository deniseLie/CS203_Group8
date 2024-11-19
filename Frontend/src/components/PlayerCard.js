import { Box, Typography } from "@mui/material";
import PlayerIcon from "./PlayerIcon";
import { useState } from "react";

// For tournamentBracket
const PlayerCard = ({ name, rankIcon, playerIcon, championImg, rank, status, onClick }) => {
    // Local state to handle hover
    const [isHovering, setIsHovering] = useState(false);

    // Determine the border color based on win/lose/pending status
    let borderColor;
    if (isHovering && status === "pending") borderColor = "#7B0C21"; // Preview "lose" color on hover if status is pending
    else if (status === "win") borderColor = "#0AC1DC";
    else if (status === "lose") borderColor = "#7B0C21";
    else borderColor = "transparent"; // No border for pending

    // Grayscale filter for "lose" status
    const imageStyle = {
        width: '100%',
        height: '100%',
        backgroundImage: `url(${championImg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        borderRadius: 1,
        filter: status === "lose" || status === "AFK" ? "grayscale(100%)" : "none", // Apply grayscale filter if "lose"
        opacity: status === "lose" || status === "AFK" ? 0.6 : 1, // Optional: lower opacity for a more subdued effect
    };

    return (
      <Box 
        display="flex" 
        alignItems="center" 
        onClick={onClick}
        onMouseEnter={() => status === "pending" && setIsHovering(true)} // Set hover effect if status is pending
        onMouseLeave={() => setIsHovering(false)} // Revert hover effect
        sx={{ cursor: 'pointer' }}
      >
        {/* Square Champion Image with Player Icon overlay */}
        <Box sx={{ position: 'relative', width: 75, height: 75, border: `2px solid ${borderColor}`, borderRadius: 1 }}>
          <Box sx={imageStyle} />

          {/* Overlayed Player Icon */}
          <Box sx={{ position: 'absolute', bottom: -10, right: -10 }}>
            <PlayerIcon src={playerIcon} width={-3.5} height={-3.5} clickable={false} />
          </Box>
        </Box>
        
        {/* Player Info */}
        <Box display="flex" flexDirection="column" ml={1}>
          <Typography variant="body1" className='headerPrimary' sx={{ fontWeight: 'bold', color: "#F0E6D2" }}>
            {name}
          </Typography>
          <Box display="flex" alignItems="center">
            <Box component="img" src={rankIcon} sx={{ width: 18, height: 18, marginRight: 0.5 }} />
            <Typography variant="caption" className='bodySecondary' sx={{ color: '#949083' }}>
              {rank}
            </Typography>
          </Box>
        </Box>
      </Box>
    );
};

export default PlayerCard;
