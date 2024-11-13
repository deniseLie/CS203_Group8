import React, { useState, useEffect } from 'react';
import { Box, Typography, Avatar } from "@mui/material";
import Navbar from "../components/Navbar";
import diamondBanner from '../assets/rank-banners/diamond.png';
import playerIcon from '../assets/summonerIcon/1.jpg';
import diamondRankIcon from '../assets/rankIcon/diamond.png';
import arenaIcon from "../assets/icons/arena_big.png";
import crownIcon from "../assets/icons/win.png";
import swordIcon from "../assets/icons/sword.png";
import ProfileBanner from '../components/ProfileBanner';
import { useAuth } from '../auth/AuthProvider';
import getChampionImage from '../util/getChampionImage';
import axios from 'axios';
import env from 'react-dotenv';
import Cookies from 'js-cookie';

const Profile = ({ logout }) => {
    const { user } = useAuth();
    const [profileData, setProfileData] = useState(null);

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const token = Cookies.get('jwtToken'); // Ensure user token is available here
                const response = await axios.get(`${env.ACCOUNT_SERVER_URL}/account/${user.sub}/profile`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setProfileData(response.data);
            } catch (error) {
                console.error("Error fetching profile data:", error);
            }
        };

        if (user?.sub) {
            fetchProfileData();
        }
    }, [user?.sub]);

    if (!profileData) {
        return <Typography>Loading...</Typography>;
    }

    return (
        <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
            <Box sx={{ flexShrink: 0, zIndex: 100 }}>
                <Navbar logout={logout} />
            </Box>

            <Box
                sx={{
                    backgroundImage: `url(${getChampionImage(profileData.topChampions[0].championName, "splash")})`,
                    backgroundSize: 'cover',
                    zIndex: 0,
                    backgroundPosition: 'center -100px',
                }}
            >
                <Box
                    sx={{
                        display: 'flex',
                        flexDirection: 'row',
                        color: '#F0E6D2',
                        px: 2,
                        flexGrow: 1,
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        position: 'relative',
                        zIndex: 2,
                    }}
                >
                    <Box sx={{ marginLeft: '5%', display: 'flex', alignItems: 'center', zIndex: 2 }}>
                        <ProfileBanner
                            profile={{
                                banner: diamondBanner,
                                playerIcon: playerIcon,
                                playerName: user?.playername || 'Player',
                                rankSymbol: diamondRankIcon,
                                rank: 'Diamond I',
                            }}
                            displayType="player"
                        />
                    </Box>

                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            width: '100%',
                            paddingBottom: '2%',
                            gap: 4,
                            marginTop: 'auto',
                            zIndex: 3,
                        }}
                    >
                        <Box
                            sx={{
                                display: 'flex',
                                width: '80%',
                                alignContent: 'flex-start',
                            }}
                        >
                            <Box
                                sx={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                    width: '45%',
                                    p: 3,
                                    alignSelf: 'flex-end',
                                    justifyItems: 'flex-start',
                                }}
                            >
                                <Typography className='headerPrimary' sx={{ mb: 2, fontSize: '1.2em', alignSelf: 'flex-start' }}>ARENA</Typography>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                                    <Box sx={{ textAlign: 'center', width: '33%' }}>
                                        <Box component="img" src={arenaIcon} alt="Arena Icon" sx={{ width: '50px', mb: 1 }} />
                                        <Typography className='headerPrimary' sx={{ fontSize: '1.2em', color: '#B68C34' }}>{profileData.totalMatches}</Typography>
                                        <Typography className='headerPrimary' sx={{ color: '#949083' }}>Games Played</Typography>
                                    </Box>

                                    <Box sx={{ textAlign: 'center', width: '33%' }}>
                                        <Box component="img" src={swordIcon} alt="Sword Icon" sx={{ width: '50px', mb: 1, backgroundColor: "rgba(0,0,0,0.5)", borderRadius: 2 }} />
                                        <Typography className="headerPrimary" sx={{ fontSize: '1.2em', color: '#B68C34' }}>#{profileData.averagePlace}</Typography>
                                        <Typography className='headerPrimary' sx={{ color: '#949083' }}>Avg Place</Typography>
                                    </Box>

                                    <Box sx={{ textAlign: 'center', width: '33%' }}>
                                        <Box component="img" src={crownIcon} alt="Crown Icon" sx={{ width: '50px', mb: 1 }} />
                                        <Typography className="headerPrimary" sx={{ fontSize: '1.2em', color: '#B68C34' }}>{(profileData.firstPlacePercentage * 100).toFixed(1)}%</Typography>
                                        <Typography className="headerPrimary" sx={{ color: '#949083' }}>1st Place Rate</Typography>
                                    </Box>
                                </Box>
                            </Box>

                            {/* Top Played Champions */}
                            <Box
                                sx={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'flex-start',
                                    p: 3,
                                    width: '45%',
                                }}
                            >
                                <Typography className="headerPrimary" sx={{ mb: 2, fontSize: '1.2em' }}>TOP PLAYED CHAMPIONS</Typography>
                                <Box
                                    sx={{
                                        display: 'flex',
                                        justifyContent: 'space-around',
                                        width: '100%',
                                    }}
                                >
                                    {profileData.topChampions.map((champion, index) => (
                                        <Box key={index} sx={{ textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                                            <Avatar
                                                variant="square"
                                                src={getChampionImage(champion.championName, "icon")}
                                                alt={champion.championName}
                                                sx={{ width: '4vw', height: '4vw', mb: 1 }}
                                            />
                                            <Typography className="headerPrimary" sx={{ color: '#B68C34', fontSize: '1.2em' }}>{champion.totalWins}W {champion.totalMatchNumber - champion.totalWins}L</Typography>
                                            <Typography className="headerPrimary" sx={{ color: '#949083' }}>{`${(champion.totalWins / champion.totalMatchNumber * 100).toFixed(1)}% WR`}</Typography>
                                        </Box>
                                    ))}
                                </Box>
                            </Box>
                        </Box>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
};

export default Profile;
