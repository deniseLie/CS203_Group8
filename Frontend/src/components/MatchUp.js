import { Box, Typography } from "@mui/material";
import PlayerCard from "./PlayerCard";
import diamondRank from '../assets/rankIcon/diamond.png';
const MatchUp = ({ leftPlayer, rightPlayer }) => (
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
      <PlayerCard name={leftPlayer.playerName} rankIcon={diamondRank} playerIcon={leftPlayer.playerIcon} championImg={leftPlayer.championImg} rank={leftPlayer.rank} status={leftPlayer.status} />
      <Typography variant="h5" sx={{ color: 'white', fontWeight: 'bold', mx: 2 }}>
        VS
      </Typography>
      {/* Second player */}
      <PlayerCard name={rightPlayer.playerName} rankIcon={diamondRank} playerIcon={rightPlayer.playerIcon} championImg={rightPlayer.championImg} rank={rightPlayer.rank} status={rightPlayer.status} />
    </Box>
  );

export default MatchUp;