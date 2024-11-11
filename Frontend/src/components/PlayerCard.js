import { Box, Typography } from "@mui/material";
import PlayerIcon from "./PlayerIcon";

const PlayerCard = ({ name, rankIcon, playerIcon, championImg, rank, status }) => {

    // Determine the border color based on win/lose/pending status
    let borderColor;
    if (status === "win") borderColor = "#0AC1DC";
    else if (status === "lose") borderColor = "#7B0C21";
    else borderColor = "transparent"; // No border for pending
  
    return (
      <Box display="flex" alignItems="center" spacing={1}>
        {/* Square Champion Image with Player Icon overlay */}
        <Box sx={{ position: 'relative', width: 75, height: 75, border: `2px solid ${borderColor}` }}>
          <Box
            sx={{
              width: '100%',
              height: '100%',
              backgroundImage: `url(${championImg})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              borderRadius: 1,
            }}
          />
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