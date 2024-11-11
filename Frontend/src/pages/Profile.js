import React from 'react';
import { Box, Typography, Avatar } from "@mui/material";
import Navbar from "../components/Navbar";
import { championsAssets, championSplashAssets } from '../components/importAssets';

import diamondBanner from '../assets/rank-banners/diamond.png';
import playerIcon from '../assets/4895.jpg';
import diamondRankIcon from '../assets/rankIcon/diamond.png';
// Other assets
import arenaIcon from "../assets/arena_big.png";
import editButton from "../assets/button-edit.png";
import crownIcon from "../assets/win.png";
import swordIcon from "../assets/sword.png";
import ProfileBanner from '../components/ProfileBanner';

const Profile = ({ logout }) => {
    return (
        <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
            {/* Navbar */}
            <Box sx={{ flexShrink: 0, zIndex: 100 }}>
                <Navbar logout={logout} />
            </Box>

            {/* Main Profile Section with Kayn's Splash Art Background */}
            <Box
            sx={{
                backgroundImage: `url(${championSplashAssets.kayn})`, // Set Kayn's splash art as background
                zIndex: 0
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
                {/* Profile Banner on the Left */}
                <Box sx={{ marginLeft: '5%', display: 'flex', alignItems: 'center', zIndex: 2 }}>
                    <ProfileBanner
                        profile={{
                            banner: diamondBanner,
                            playerIcon: playerIcon,
                            playerName: 'hide on bush',
                            rankSymbol: diamondRankIcon,
                            rank: 'Diamond I',
                        }}
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
                            <Typography className='headerPrimary' sx={{ mb: 2, fontSize:'1.2em', alignSelf:'flex-start' }}>ARENA</Typography>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                                <Box sx={{ textAlign: 'center', width: '33%' }}>
                                    <Box
                                        component="img"
                                        src={arenaIcon}
                                        alt="Arena Icon"
                                        sx={{ width: '50px', mb: 1 }}
                                    />
                                    <Typography className='headerPrimary' sx={{fontSize:'1.2em', color:'#B68C34'}}>1500</Typography>
                                    <Typography className='headerPrimary' sx={{ color: '#a0aec0' }}>Games Played</Typography>
                                </Box>

                                <Box sx={{ textAlign: 'center', width: '33%' }}>
                                    <Box
                                        component="img"
                                        src={swordIcon}
                                        alt="Sword Icon"
                                        sx={{ width: '50px', mb: 1, backgroundColor: "rgba(0,0,0,0.5)", borderRadius:2 }}
                                    />
                                    <Typography variant="h6">3RD</Typography>
                                    <Typography className='headerPrimary' sx={{ color: '#a0aec0' }}>Avg Place</Typography>
                                </Box>

                                <Box sx={{ textAlign: 'center', width: '33%' }}>
                                    <Box
                                        component="img"
                                        src={crownIcon}
                                        alt="Crown Icon"
                                        sx={{ width: '50px', mb: 1 }}
                                    />
                                    <Typography variant="h6">20%</Typography>
                                    <Typography variant="body2" sx={{ color: '#a0aec0' }}>1st Place</Typography>
                                </Box>
                            </Box>
                        </Box>

                        {/* Top Played Champions */}
                        <Box
                            sx={{
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                                p: 3,
                                width: '45%',
                            }}
                        >
                            <Typography variant="h6" sx={{ mb: 2 }}>TOP PLAYED CHAMPIONS (MATCHES)</Typography>
                            <Box
                                sx={{
                                    display: 'flex',
                                    justifyContent: 'space-around',
                                    width: '100%',
                                }}
                            >
                                {[...Array(3)].map((_, index) => (
                                    <Box key={index} sx={{ textAlign: 'center' }}>
                                        <Avatar
                                            variant="square"
                                            src={championsAssets.kayn}
                                            alt="Kayn"
                                            sx={{ width: '80px', height: '80px', mb: 1 }}
                                        />
                                        <Typography variant="body1">100</Typography>
                                        <Typography variant="body2" sx={{ color: '#a0aec0' }}>50W 50L (50%)</Typography>
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
