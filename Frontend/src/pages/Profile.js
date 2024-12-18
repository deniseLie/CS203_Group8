import React from 'react';
import { Box, Typography, Avatar } from "@mui/material";
import Navbar from "../components/Navbar";
import { championsAssets, championSplashAssets } from '../util/importAssets';

import diamondBanner from '../assets/rank-banners/diamond.png';
import playerIcon from '../assets/summonerIcon/1.jpg';
import diamondRankIcon from '../assets/rankIcon/diamond.png';
// Other assets
import arenaIcon from "../assets/icons/arena_big.png";
import crownIcon from "../assets/icons/win.png";
import swordIcon from "../assets/icons/sword.png";
import ProfileBanner from '../components/ProfileBanner';
import { useAuth } from '../auth/AuthProvider';
import getChampionImage from '../util/getChampionImage';
import axios from 'axios';
import env from 'react-dotenv';
import Cookies from 'js-cookie';
import { rankBannerIconsAssets, rankIconsAssets } from '../util/importAssets';

const Profile = ({ logout }) => {
    const { user } = useAuth();
    const [profileData, setProfileData] = useState(null);

    // Function: Fetch profile data
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

    // If profile data doesnt exist show loading
    if (!profileData) {
        return <Typography>Loading...</Typography>;
    }

    return (
        <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
            {/* Navbar */}
            <Box sx={{ flexShrink: 0, zIndex: 100 }}>
                <Navbar logout={logout} />
            </Box>

            <Box
                sx={{
                    backgroundImage: `url(${championSplashAssets.kayn})`, // Set Kayn's splash art as background
                    backgroundSize:'cover',
                    zIndex: 0,
                    backgroundPosition: 'center -100px', // Moves the background image up (you can adjust the value further)
                }}
            >
                <Box
                    sx={{
                        display: 'flex',
                        flexDirection: 'row', // Row layout to align profile banner to the left
                        color: '#F0E6D2',
                        px: 2,
                        flexGrow: 1, // Makes this section grow to fill available space
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                        position: 'relative',
                        zIndex: 2, // Ensure content is above the gradient
                    }}
                >
                    <Box sx={{ marginLeft: '5%', display: 'flex', alignItems: 'center', zIndex: 2 }}>
                        <ProfileBanner
                            displayType="player"
                        />
                    </Box>

                    {/* Statistics Section, positioned above the gradient */}
                    <Box
                        sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            width: '100%', // Occupy full width
                            paddingBottom: '2%', // Spacing from bottom
                            gap: 4,
                            marginTop: 'auto', // Pushes this section to the bottom of the main container
                            zIndex: 3, // Ensure this content is above the gradient
                        }}
                    >
                        {/* Arena Stats and Top Played Champions Side by Side */}
                        <Box
                            sx={{
                                display: 'flex',
                                width: '80%',
                                alignContent:'flex-start'
                            }}
                        >
                            {/* Arena Stats */}
                            <Box
                                sx={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                    width: '45%',
                                    p: 3,
                                    alignSelf:'flex-end',
                                    justifyItems:'flex-start'
                                }}
                            >
                                <Typography className='headerPrimary' sx={{ mb: 2, fontSize: '1.2em', alignSelf: 'flex-start' }}>ARENA</Typography>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                                    <Box sx={{ textAlign: 'center', width: '33%' }}>
                                        <Box
                                            component="img"
                                            src={arenaIcon}
                                            alt="Arena Icon"
                                            sx={{ width: '50px', mb: 1 }}
                                        />
                                        <Typography className='headerPrimary' sx={{fontSize:'1.2em', color:'#B68C34'}}>{dummyData.arenaStats.gamesPlayed}</Typography>
                                        <Typography className='headerPrimary' sx={{ color: '#949083' }}>Games Played</Typography>
                                    </Box>

                                    <Box sx={{ textAlign: 'center', width: '33%' }}>
                                        <Box
                                            component="img"
                                            src={swordIcon}
                                            alt="Sword Icon"
                                            sx={{ width: '50px', mb: 1, backgroundColor: "rgba(0,0,0,0.5)", borderRadius:2 }}
                                        />
                                        <Typography className="headerPrimary" sx={{fontSize:'1.2em', color:'#B68C34'}} >#{dummyData.arenaStats.avgPlace}</Typography>
                                        <Typography className='headerPrimary' sx={{ color: '#949083' }}>Avg Place</Typography>
                                    </Box>

                                    <Box sx={{ textAlign: 'center', width: '33%' }}>
                                        <Box
                                            component="img"
                                            src={crownIcon}
                                            alt="Crown Icon"
                                            sx={{ width: '50px', mb: 1 }}
                                        />
                                        <Typography className="headerPrimary" sx={{fontSize:'1.2em', color:'#B68C34'}} >{dummyData.arenaStats.firstPlaceRate}%</Typography>
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
                                <Typography className="headerPrimary" sx={{ mb: 2 , fontSize:'1.2em'}}>TOP PLAYED CHAMPIONS</Typography>
                                <Box
                                    sx={{
                                        display: 'flex',
                                        justifyContent: 'space-around',
                                        width: '100%',
                                    }}
                                >
                                    {dummyData.topPlayedChampions.map((champion, index) => (
                                        <Box key={index} sx={{ textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                                            <Avatar
                                                variant="square"
                                                src={champion.img}
                                                alt={champion.name}
                                                sx={{ width: '4vw', height: '4vw', mb: 1 }}
                                            />
                                            <Typography className="headerPrimary" sx={{ color: '#B68C34' ,fontSize:'1.2em'}}>{champion.winLoss}</Typography>
                                            <Typography className="headerPrimary" sx={{ color: '#949083'  }}>{champion.winRate}</Typography>
                                        </Box>
                                    ))}
                                </Box>
                            </Box>
                        </Box>
                    </Box>
                </Box>

                {/* Full-width Gradient Background at the Bottom */}
                <Box
                    sx={{
                        position: 'absolute',
                        bottom: 0,
                        left: 0,
                        width: '100vw', // Ensures it covers the full viewport width
                        height: '33vh', // One third of the viewport height
                        backgroundColor: 'linear-gradient(to top, #000000 0%, transparent 100%)',
                        zIndex: 1,
                    }}
                />
            </Box>
        </Box>
    );
};

export default Profile;