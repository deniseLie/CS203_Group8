import React from 'react'
import Navbar from '../components/Navbar'
import { Box, Typography } from '@mui/material'

const History = () => {
  return (
    <>
        <Navbar/>
        <Box>
            <Typography>
                RECENT GAMES (LAST 20 PLAYED)
            </Typography>
        </Box>
    </>
  )
}

export default History