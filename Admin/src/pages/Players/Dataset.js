// import React, { useState, useEffect } from 'react';
// import { Box, Typography, Button, TextField, Grid } from '@mui/material';
// import Sidebar from '../../components/Sidebar';
// import TopBar from '../../components/TopBar';
// import PlayerTable from '../../components/PlayerTable';
// import { useNavigate } from 'react-router-dom';

// // Import profile pictures
// import player1Profile from '../../assets/summonerIcon/1.jpg';
// import player2Profile from '../../assets/summonerIcon/2.jpg';
// import player3Profile from '../../assets/summonerIcon/8.jpg';
// import player4Profile from '../../assets/summonerIcon/4.jpg';
// import player5Profile from '../../assets/summonerIcon/5.jpg';
// import player6Profile from '../../assets/summonerIcon/6.jpg';
// import player7Profile from '../../assets/summonerIcon/7.jpg';
// import player8Profile from '../../assets/summonerIcon/8.jpg';

// // Function to simulate getting a random champion (mock data)
// const getRandomChampion = () => {
//   const champions = ['Aatrox', 'Zed', 'Yasuo', 'Lux', 'Thresh'];
//   const randomChampion = champions[Math.floor(Math.random() * champions.length)];
//   return { champion: randomChampion };
// };

// const PlayerDatasetPage = () => {
//   const [playerData, setPlayerData] = useState([]);
//   const [filteredData, setFilteredData] = useState([]); // State for filtered data
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState('');
//   const [filterQuery, setFilterQuery] = useState(''); // State for the filter query
  
//   const navigate = useNavigate();

//   // Mock fetchPlayerData API call
//   const fetchPlayerData = async () => {
//     setLoading(true);
//     // Simulate a delay to mimic fetching from an API
//     setTimeout(() => {
//       try {
//         const mockPlayerData = [
//           { 
//             userId: 1, 
//             id: 1, 
//             username: 'avexx', 
//             playername: 'Avexx', 
//             rank: 'Diamond II', 
//             email: 'avexx@example.com',
//             profilePicture: player1Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 2, 
//             id: 2, 
//             username: 'Rodan', 
//             playername: 'Rodan', 
//             rank: 'Diamond I', 
//             email: 'rodan@example.com',
//             profilePicture: player2Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 3, 
//             id: 3, 
//             username: 'xDivineSword', 
//             playername: 'xDivineSword', 
//             rank: 'Diamond II', 
//             email: 'divinesword@example.com',
//             profilePicture: player3Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 4, 
//             id: 4, 
//             username: 'lilWanton', 
//             playername: 'LilWanton', 
//             rank: 'Diamond III', 
//             email: 'lilwanton@example.com',
//             profilePicture: player4Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 5, 
//             id: 5, 
//             username: 'DarkStar', 
//             playername: 'DarkStar', 
//             rank: 'Diamond I', 
//             email: 'darkstar@example.com',
//             profilePicture: player5Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 6, 
//             id: 6, 
//             username: 'Nebula', 
//             playername: 'Nebula', 
//             rank: 'Diamond III', 
//             email: 'nebula@example.com',
//             profilePicture: player6Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 7, 
//             id: 7, 
//             username: 'VoidWalker', 
//             playername: 'VoidWalker', 
//             rank: 'Diamond II', 
//             email: 'voidwalker@example.com',
//             profilePicture: player7Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//           { 
//             userId: 8, 
//             id: 8, 
//             username: 'WindRider', 
//             playername: 'WindRider', 
//             rank: 'Diamond IV', 
//             email: 'windrider@example.com',
//             profilePicture: player8Profile,
//             status: 'pending',
//             ...getRandomChampion()
//           },
//         ];
//         setPlayerData(mockPlayerData); // Set mock player data
//         setFilteredData(mockPlayerData); // Initially set filtered data to all players
//       } catch (error) {
//         setError('Failed to fetch player data. Please try again later.');
//       } finally {
//         setLoading(false);
//       }
//     }, 1000); // Simulate a delay of 1 second
//   };

//   // Handle delete player
//   const handleDeletePlayer = async (playerId) => {
//     try {
//       setPlayerData(prevData => prevData.filter(player => player.id !== playerId));
//       setFilteredData(prevData => prevData.filter(player => player.id !== playerId)); // Also update filtered data
//     } catch (error) {
//       setError('Failed to delete player. Please try again.');
//     }
//   };

//   // Handle edit player (navigate to the edit page)
//   const handleEditPlayer = (player) => {
//     navigate(`/players/edit`, { state: { player } });
//   };

//   // Handle filter input change
//   const handleFilterChange = (event) => {
//     const query = event.target.value.toLowerCase();
//     setFilterQuery(query);

//     const filtered = playerData.filter((player) => {
//       return (
//         player.id.toString().includes(query) ||
//         player.username.toLowerCase().includes(query) ||
//         player.playername.toLowerCase().includes(query)
//       );
//     });

//     setFilteredData(filtered); // Update the filtered data in real-time
//   };

//   useEffect(() => {
//     fetchPlayerData();
//   }, []);

//   return (
//     <Box sx={{ display: 'flex' }}>
//       <Sidebar />
//       <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
//         <TopBar />
//         <Box display="flex" marginBottom={2} justifyContent="space-between" alignItems="center">
//           <Typography variant="h4">Player Dataset</Typography>
//           <Button variant="outlined" onClick={() => navigate('/players/create')}>
//             Create Player
//           </Button>
//         </Box>

//         {/* Filter Bar */}
//         <Box display="flex" marginBottom={2}>
//           <TextField
//             label="Filter by ID, Username, or Playername"
//             value={filterQuery}
//             onChange={handleFilterChange}
//             fullWidth
//             variant="outlined"
//           />
//         </Box>

//         {loading ? (
//           <Typography>Loading...</Typography>
//         ) : error ? (
//           <Typography color="error">{error}</Typography>
//         ) : (
//           <PlayerTable data={filteredData} onDelete={handleDeletePlayer} onEdit={handleEditPlayer} />
//         )}
//       </Box>
//     </Box>
//   );
// };

// export default PlayerDatasetPage;

import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, TextField } from '@mui/material';
import Sidebar from '../../components/Sidebar';
import TopBar from '../../components/TopBar';
import PlayerTable from '../../components/PlayerTable';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';

// Import profile pictures dynamically
import * as profileImages from '../../assets/summonerIcon'; // assuming all images are in this directory

const PlayerDatasetPage = () => {
  const [playerData, setPlayerData] = useState([]);
  const [filteredData, setFilteredData] = useState([]); // State for filtered data
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filterQuery, setFilterQuery] = useState(''); // State for the filter query

  const navigate = useNavigate();

  // Fetch player data from the API
  const fetchPlayerData = async () => {
    setLoading(true);
    try {
      const response = await api.get('/admin/user/getAllUser');
      const players = response.data; // Assuming the response data contains the player data
      setPlayerData(players); // Set the player data
      setFilteredData(players); // Set filtered data initially to all players
    } catch (error) {
      setError('Failed to fetch player data. Please try again later.' + error);
      console.error('Error fetching player data:', error);
    } finally {
      setLoading(false);
    }
  };

  // Handle delete player
  const handleDeletePlayer = async (playerId) => {
    try {
      setPlayerData(prevData => prevData.filter(player => player.id !== playerId));
      setFilteredData(prevData => prevData.filter(player => player.id !== playerId)); // Also update filtered data
    } catch (error) {
      setError('Failed to delete player. Please try again.');
    }
  };

  // Handle edit player (navigate to the edit page)
  const handleEditPlayer = (player) => {
    navigate(`/players/edit`, { state: { player } });
  };

  // Handle filter input change
  const handleFilterChange = (event) => {
    const query = event.target.value.toLowerCase();
    setFilterQuery(query);

    const filtered = playerData.filter((player) => {
      return (
        player.id.toString().includes(query) ||
        player.username.toLowerCase().includes(query) ||
        player.playername.toLowerCase().includes(query)
      );
    });

    setFilteredData(filtered); // Update the filtered data in real-time
  };

  useEffect(() => {
    fetchPlayerData(); // Fetch data when the component mounts
  }, []);

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flex: 1, p: 3, backgroundColor: '#f4f5f7', minHeight: '100vh' }}>
        <TopBar />
        <Box display="flex" marginBottom={2} justifyContent="space-between" alignItems="center">
          <Typography variant="h4">Player Dataset</Typography>
          <Button variant="outlined" onClick={() => navigate('/players/create')}>
            Create Player
          </Button>
        </Box>

        {/* Filter Bar */}
        <Box display="flex" marginBottom={2}>
          <TextField
            label="Filter by ID, Username, or Playername"
            value={filterQuery}
            onChange={handleFilterChange}
            fullWidth
            variant="outlined"
          />
        </Box>

        {loading ? (
          <Typography>Loading...</Typography>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <PlayerTable data={filteredData} onDelete={handleDeletePlayer} onEdit={handleEditPlayer} profileImages={profileImages} />
        )}
      </Box>
    </Box>
  );
};

export default PlayerDatasetPage;
