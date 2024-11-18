import React, { useState, useEffect } from 'react';
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
  const { user } = useAuth();
  const [leaderboardData, setLeaderboardData] = useState([]);
  const [loading, setLoading] = useState(true);

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
        position: 'relative',
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
          {loading || !leaderboardData ? (
            <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        alignSelf:'center',
        justifySelf:'center',
        textAlign: 'center',
        marginTop:'20vh',
        marginLeft:'10vw'
      }}
    >
      <Box
        component="img"
        src={notFound}
        alt="No games found"
        sx={{
          width: '10vw', // adjust the size as needed
          height: 'auto',
          marginBottom: 1, // space between image and text
        }}
      />
      <Typography className="headerPrimary">NO GAMES FOUND</Typography>
    </Box>
          ) : (
            leaderboardData.map((tournament, tournamentIndex) => (
              <Box key={tournamentIndex} sx={{ mb: 2 }}>
                <GameCard 
                  players={tournament.players} // Pass all players to the GameCard
                  currentPlayer={user ? user.playername : ''}
                />
              </Box>
            ))
          )}
        </Box>

        {/* Profile Summary */}
        <Box sx={{ flex: 1, marginTop: -5 }}>
          <ProfileBanner
            profile={{
              banner: user ? rankBannerIconsAssets[user.rank.toLowerCase()] :  rankBannerIconsAssets.unranked,
              rankSymbol:  user ? rankIconsAssets[user.rank.toLowerCase()] :  rankIconsAssets.unranked,
              rank: user ? user.rank : "Unranked",
              lp: user ? user.elo : "-",
            }}
            displayType="rank"
          />
        </Box>
      </Box>
    </Box>
  );
}

export default History;
