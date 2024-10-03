import React from 'react';
import { Box, Typography } from '@mui/material';
import Navbar from '../components/Navbar'; // Assuming the navbar already exists
import GameCard from '../components/GameCard';
import ProfileBanner from '../components/ProfileBanner';
import RankingTable from '../components/RankingTable';
import backgroundImage from '../assets/srbackground.png'; // Import the PNG file
import avatarImage from '../assets/4895.jpg';
import diamondRankImage from '../assets/ranks/diamond.png';
import diamondBanner from '../assets/rank-banners/diamond.png'

function History() {
  const recentGames = [
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 8,
      champion: { name: 'Vi', image: 'path/to/vi.png' },
      kda: '0/1',
      lpChange: -170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    },
    {
      rank: 1,
      champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' },
      kda: '8/0',
      lpChange: 170,
      time: '31:25 09/15/2024',
    }
    // Add more game data here...
  ];

  const profile = {
    avatar: avatarImage,
    name: 'hide on bush',
    rank: 'DIAMOND I',
    elo: 78 + ' LP',
    banner: diamondBanner,
    rankSymbol: diamondRankImage
  };

  const rankings = [
    { position: 1, champion: { name: 'Ahri', image: 'path/to/ahri.png' }, player: 'hide on bush', kda: '8/0' },
    { position: 2, champion: { name: 'Sett', image: 'path/to/sett.png' }, player: 'Rodan', kda: '8/0' },
    { position: 3, champion: { name: 'Bel\'Veth', image: 'path/to/belveth.png' }, player: 'xDivineSword', kda: '8/0' },
    // Add more player data here...
  ];

  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`, // Set the background image
        backgroundSize: 'cover', // Ensure the background covers the entire container
        backgroundPosition: 'center', // Center the background image
        backgroundRepeat: 'no-repeat', // Prevent the background from repeating
        minHeight: '100vh', // Ensure the container takes the full height of the viewport
        width: '100%', // Full width
        position: 'relative' // Ensure content inside is positioned relative to this container
      }}
    >
      {/* Navbar */}
      <Box sx={{ position: 'relative', zIndex: 1 }}>
        <Navbar activePage="history" />
      </Box>

      {/* Main Content Container */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', md: 'row' }, // Stack vertically on smaller screens, horizontally on larger
          gap: 4, // Space between elements
          backgroundColor: 'rgba(0, 0, 0, 0.5)', // Darker transparent background
          padding: 3, // Padding inside the container
        }}
      >
        {/* Recent Games Section */}
        <Box sx={{ flex: 2 }}>
          <Typography className="headerPrimary" sx={{ marginBottom: 3 }}>
            RECENT GAMES (LAST 20 PLAYED)
          </Typography>
          {recentGames.map((game, index) => (
            <Box key={index} sx={{ mb: 1 }}>
              <GameCard {...game} />
            </Box>
          ))}
        </Box>

        {/* Profile Summary */}
        <Box sx={{ 
          flex: 1,
          marginTop: -5
        }}>
          <ProfileBanner profile={profile} />
        </Box>
      </Box>
    </Box>
  );
}

export default History;
