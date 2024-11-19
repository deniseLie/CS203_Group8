import React from 'react';
import { Box, Typography } from '@mui/material';
import Navbar from '../components/Navbar';
import GameCard from '../components/GameCard';
import ProfileBanner from '../components/ProfileBanner';
import backgroundImage from '../assets/backgrounds/srbackground.png';
import { useAuth } from '../auth/AuthProvider';
import axios from 'axios';
import env from 'react-dotenv';
import notFound from '../assets/icons/not-found.png'
import Cookies from 'js-cookie';
import { rankBannerIconsAssets, rankIconsAssets } from '../util/importAssets';

function History() {
  // const { user } = useAuth();
  // const currentPlayerName = user ? user.sub.playername : "PlayerOne"; // Default to "PlayerOne" if user is null
  const currentPlayerName = "PlayerOne"; // Default to "PlayerOne" if user is null

  const leaderboardData = [
    {
      tournamentId: 1,
      matchDetails: [
        {
          players: [
            { standing: '1ST', champion: 'Bel\'Veth', playerName: currentPlayerName, kd: '8/0', kda: '8.0 KDA', lpChange: 170, time: '31:25', date: '13/11/2024' },
            { standing: '2ND', champion: 'Ahri', playerName: 'ARAMLOVER', kd: '8/0', kda: '8.0 KDA', lpChange: 170, time: '31:25', date: '13/11/2024' },
            { standing: '3RD', champion: 'Sett', playerName: 'Rodan', kd: '8/0', kda: '8.0 KDA', lpChange: 170, time: '31:25', date: '13/11/2024' },
            { standing: '4TH', champion: 'Miss Fortune', playerName: 'lilWanton', kd: '8/0', kda: '8.0 KDA', lpChange: 170, time: '31:25', date: '13/11/2024' },
            { standing: '5TH', champion: 'Galio', playerName: 'GodanRoose', kd: '8/0', kda: '8.0 KDA', lpChange: 170, time: '31:25', date: '13/11/2024' },
            { standing: '6TH', champion: 'Zac', playerName: 'Radon', kd: '8/0', kda: '8.0 KDA', lpChange: 170, time: '31:25', date: '13/11/2024' },
            { standing: '7TH', champion: 'Jinx', playerName: 'LikeFromArcane', kd: '1/1', kda: '1.0 KDA', lpChange: 0, time: '31:25', date: '13/11/2024' },
            { standing: '8TH', champion: 'Vi', playerName: 'xDivineSword', kd: '0/1', kda: '0.0 KDA', lpChange: -170, time: '31:25', date: '13/11/2024', isAFK: true },
          ],
        },
      ],
    },
    {
      tournamentId: 2,
      matchDetails: [
        {
          players: [
            { standing: '1ST', champion: 'Jinx', playerName: 'Rodan', kd: '10/2', kda: '5.0 KDA', lpChange: 200, time: '29:45', date: '09/16/2024' },
            { standing: '2ND', champion: 'Zed', playerName: 'hide on bush', kd: '9/3', kda: '3.0 KDA', lpChange: 150, time: '29:45', date: '09/16/2024' },
            { standing: '3RD', champion: 'Vi', playerName: currentPlayerName, kd: '6/5', kda: '1.2 KDA', lpChange: 100, time: '29:45', date: '09/16/2024' },
            { standing: '4TH', champion: 'Ahri', playerName: 'GodanRoose', kd: '4/5', kda: '0.8 KDA', lpChange: 50, time: '29:45', date: '09/16/2024' },
            { standing: '5TH', champion: 'Zac', playerName: 'Radon', kd: '2/6', kda: '0.3 KDA', lpChange: 0, time: '29:45', date: '09/16/2024' },
            { standing: '6TH', champion: 'Sett', playerName: 'ARAMLOVER', kd: '0/7', kda: '0.0 KDA', lpChange: -50, time: '29:45', date: '09/16/2024' },
            { standing: '7TH', champion: 'Annie', playerName: 'lilWanton', kd: '1/8', kda: '0.1 KDA', lpChange: -100, time: '29:45', date: '09/16/2024' },
            { standing: '8TH', champion: 'Galio', playerName: 'RodanGoose', kd: '0/9', kda: '0.0 KDA', lpChange: -150, time: '29:45', date: '09/16/2024', isAFK: true },
          ],
        },
      ],
    },
  ];

  // fetch user's match history based on id
  const fetchMatchHistory = async () => {
    try {
      const token = Cookies.get('jwtToken');
      const response = await axios.get(
        `${env.ACCOUNT_SERVER_URL}/account/${user.sub}/match-history`,
        {
          playerId: user.sub
        },
        {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      );
      setLeaderboardData(response.data); // Assumes response data is in the same format as leaderboardData
      setLoading(false);
    } catch (error) {
      console.error("Error fetching match history:", error);
      setLoading(false);
    }
  };
  useEffect(() => {
    if (user?.sub) {
      fetchMatchHistory();
    }
  }, [user?.sub]);

  return (
    <Box
      sx={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        minHeight: '100vh',
        width: '100%',
        position: 'relative'
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
          flexDirection: { xs: 'column', md: 'row' },
          gap: 4,
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
          padding: 3,
        }}
      >
        {/* Recent Games Section */}
        <Box sx={{ flex: 2 }}>
          <Typography className="headerPrimary" sx={{ marginBottom: 3 }}>
            RECENT GAMES (LAST 20 PLAYED)
          </Typography>
          {leaderboardData.map((tournament) => (
            <Box key={tournament.tournamentId} sx={{ mb: 2 }}>
              {tournament.matchDetails.map((match, matchIndex) => (
                <Box key={matchIndex} sx={{ mb: 1 }}>
                  <GameCard 
                    players={match.players} // Pass all players to the GameCard
                    currentPlayer={currentPlayerName}
                  />
                </Box>
              ))}
            </Box>
          ))}
        </Box>

        {/* Profile Summary */}
        <Box sx={{ flex: 1, marginTop: -5 }}>
          <ProfileBanner
            displayType="rank"
          />
        </Box>
      </Box>
    </Box>
  );
}

export default History;
