import React, { useState } from 'react';
import { Box, Typography, Collapse, List, ListItem, ListItemText, Avatar, Divider } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import PlayerIcon from './PlayerIcon';
import profilePic from '../assets/summonerIcon/4895.jpg'
function GameCard({ players, currentPlayer }) {
  const [open, setOpen] = useState(false); // State to control dropdown visibility

  // Get current player's data
  const currentPlayerData = players.find(player => player.playerName === currentPlayer);

  return (
    <Box 
      display="flex" 
      flexDirection="column"
      sx={{ 
        position: 'relative',
        padding:1,
        cursor: 'pointer',
        paddingBottom:3,
        borderBottom:1,
        borderBottomColor:"#464F4D",
        justifyContent:'center',
        alignContent:'center'
      }}
      onClick={() => setOpen(!open)} // Toggle dropdown on click
    >
    <Box display="flex" justifyContent="space-between" alignItems="center" flexDirection={'row'} width="100%">
  <Box display="flex" alignItems="center">
    {/* Standing */}
    <Typography className="headerPrimary" sx={{ marginRight: 2, fontSize: '1.5em', color: '#fff', fontWeight: 'bold', minWidth:'5vw'}}>
      {currentPlayerData ? currentPlayerData.standing : ''}
    </Typography>

    {/* Champion Icon */}
    {currentPlayerData && (
      <Box
        sx={{
          width: 50,
          height: 50,
          mr: 1,
          backgroundImage: `url(${require(`../assets/champions/${currentPlayerData.champion.toLowerCase().replace(/[\s']/g, '')}.png`)})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          
        }}
      />
    )}

    {/* Champion Name */}
    <Box sx={{minWidth:'12vw'}}>

    <Typography className="headerPrimary" sx={{ color: '#fff', marginRight: 2}}>
      {currentPlayerData ? currentPlayerData.champion : ''}
    </Typography>
    </Box>

    {/* Divider */}
    <Divider orientation="vertical" sx={{ bgcolor: '#464F4D', height: '4vw', width:'0.05vw', margin: '0 10px', alignSelf:'center' }}  flexItem />

    {/* KDA */}
    <Box display={'flex'} minWidth={'10vw'} flexDirection={'column'} alignItems={'center'}>
    <Typography className="headerPrimary" sx={{ color: currentPlayerData && currentPlayerData.kd ? '#fff' : '#dc3545', fontWeight: 'bold', marginLeft:2 }}>
      {currentPlayerData ? `${currentPlayerData.kd}` : ''}
    </Typography>

    <Typography className="bodyPrimary"  sx={{ color: currentPlayerData && currentPlayerData.kd ? '#fff' : '#dc3545', fontWeight: 'bold', marginLeft:2 }}>
      {currentPlayerData ? `${currentPlayerData.kda}` : ''}
    </Typography>

    </Box>

    {/* match time */}
    <Typography className="bodySecondary" minWidth={'5vw'} sx={{ color: currentPlayerData && currentPlayerData.kd ? '#fff' : '#dc3545', fontWeight: 'bold', marginLeft:2 }}>
      31:25
    </Typography>

    
    {/* date */}
    <Typography className="bodySecondary" minWidth={'8vw'} sx={{ color: currentPlayerData && currentPlayerData.kd ? '#fff' : '#dc3545', fontWeight: 'bold', marginLeft:2 }}>
      8/11/2024
    </Typography>
  </Box>
  <ExpandMoreIcon
          sx={{
            color: '#fff',
            fontSize: '2em',
            transition: 'transform 0.3s ease',
            transform: open ? 'rotate(-180deg)' : 'rotate(0deg)',
          }}
        />
</Box>

      {/* Dropdown Player List */}
      <Collapse in={open} timeout="auto" unmountOnExit>
        <Box sx={{ marginTop:3, marginLeft:2, borderRadius: 1, borderTop:1 , borderTopColor:'#464F4D'}}>
        <List>
  {players.map((player, index) => (
    <ListItem key={index} sx={{ padding: 0, display: 'flex', alignItems: 'center' }}>
      
      {/* Standing */}
      <Typography
        className='headerPrimary'
        sx={{ width: '40px', marginRight: 2, color: player.playerName === currentPlayer ? '#FFD700' : '#fff' }}
      >
        {player.standing}
      </Typography>
      
      {/* Champion Icon */}
      <Box
        sx={{
          width: 40,
          height: 40,
          mr: 2,
          backgroundImage: `url(${require(`../assets/champions/${player.champion.toLowerCase().replace(/[\s']/g, '')}.png`)})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          borderLeft: player.playerName === currentPlayer ? '5px solid #D8A13A' : 'none',
        }}
      />
      {/* Player Name */}
      <Typography
        className='headerPrimary'
        sx={{
          minWidth: '120px', // Adjust this width as needed
          color: player.playerName === currentPlayer ? '#FFD700' : '#fff',
          marginRight: 5,
          marginLeft:2,
          minWidth:'15vw'
        }}
      >
        {player.champion}
      </Typography>
      <PlayerIcon src={profilePic} height={-4} width={-4}/>

      {/* Player Name */}
      <Typography
        className='headerPrimary'
        sx={{
          minWidth: '120px', // Adjust this width as needed
          color: player.playerName === currentPlayer ? '#FFD700' : '#fff',
          marginRight: 5,
          marginLeft:2,
          minWidth:'15vw'
        }}
      >
        {player.playerName}
      </Typography>

      {/* KDA Alignment Container */}
      <Box display="flex" alignSelf={'center'} flexDirection={'column'} justifyContent={'center'} sx={{ minWidth: '100px', textAlign: 'right' }}>
        <Typography className='headerPrimary' sx={{ width: '40px', textAlign: 'right', color: player.playerName === currentPlayer ? '#FFD700' : '#fff' }}>
          {player.kd}
        </Typography>
        <Typography className='headerPrimary' sx={{ width: '60px', textAlign: 'center', fontSize: '0.8em' }}>
          {player.kda}
        </Typography>
      </Box>
    </ListItem>
  ))}
</List>

        </Box>
      </Collapse>
    </Box>
  );
}

export default GameCard;
