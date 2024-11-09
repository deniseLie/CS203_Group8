import React from 'react';
import { Box, Button, MenuItem, TextField, Stack } from '@mui/material';

const FilterBar = () => (
  <Box sx={{ mb: 3, p: 2, backgroundColor: '#f4f5f7', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)' }}>
    <Stack direction="row" spacing={2} alignItems="center">
      <TextField
        label="Filter By"
        select
        variant="outlined"
        size="small"
        sx={{ minWidth: 120 }}
      >
        <MenuItem value="Date">Date</MenuItem>
        <MenuItem value="Status">Status</MenuItem>
      </TextField>

      <TextField
        label="Search Player"
        variant="outlined"
        size="small"
        sx={{ minWidth: 150 }}
      />

      <TextField
        label="Search Tournament Id"
        variant="outlined"
        size="small"
        sx={{ minWidth: 150 }}
      />

      <Button variant="text" sx={{ color: '#d32f2f', textTransform: 'none' }}>
        Reset Filter
      </Button>
    </Stack>
  </Box>
);

export default FilterBar;
