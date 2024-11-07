import React from 'react';
import { Box, ListItem, ListItemText, ListItemIcon, List } from '@mui/material';

const SidebarItem = ({ text, icon, subItems }) => (
  <Box>
    <ListItem sx={{ color: '#ffffff' }}>
      <ListItemIcon sx={{ color: '#ffffff' }}>{icon}</ListItemIcon>
      <ListItemText primary={text} />
    </ListItem>
    {subItems && (
      <List component="div" disablePadding sx={{ pl: 4 }}>
        {subItems.map((item, index) => (
          <ListItem key={index} sx={{ color: '#ffffff' }}>
            <ListItemText primary={item} />
          </ListItem>
        ))}
      </List>
    )}
  </Box>
);

export default SidebarItem;
