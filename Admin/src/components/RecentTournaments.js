import React from 'react';
import { Box, Typography, Button, Stack, Avatar } from '@mui/material';

const RecentTournaments = () => (
  <Box>
    <Typography variant="h6" sx={{ mb: 2 }}>Most Recent Tournaments</Typography>
    <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
      <table width="100%">
        <thead>
          <tr>
            <th>Tournament Id</th>
            <th>Round No.</th>
            <th>Matches Completed</th>
            <th>Status</th>
            <th>Players</th>
            <th>Start Date-Time</th>
            <th>End Date-Time</th>
          </tr>
        </thead>
        <tbody>
          {[1, 2, 3, 4, 5].map((id) => (
            <tr key={id}>
              <td><a href="#">{`#${9093 + id}`}</a></td>
              <td>2/3</td>
              <td>5/7</td>
              <td><Button size="small" variant="contained" color={id % 2 === 0 ? 'success' : 'warning'}>{id % 2 === 0 ? 'Completed' : 'Ongoing'}</Button></td>
              <td>
                <Stack direction="row" spacing={1}>
                  <Avatar src="/profile1.jpg" sx={{ width: 24, height: 24 }} />
                  <Avatar src="/profile2.jpg" sx={{ width: 24, height: 24 }} />
                  <Avatar src="/profile3.jpg" sx={{ width: 24, height: 24 }} />
                  <Typography>+5</Typography>
                </Stack>
              </td>
              <td>04 Sep 2024 16:40</td>
              <td>{id % 2 === 0 ? '04 Sep 2024 16:58' : '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </Box>
    <Button sx={{ mt: 2 }}>View All Tournaments</Button>
  </Box>
);

export default RecentTournaments;
