import React from 'react';
import { ListItemButton, ListItemIcon, ListItemText, Collapse, List } from '@mui/material';
import { ExpandLess, ExpandMore } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const SidebarItem = ({ text, icon, subItems, isOpen, onToggle, onClick, isActive, activePage }) => {
    const navigate = useNavigate();

    const handleItemClick = () => {
      if (subItems) {
        onToggle(); // Toggle the dropdown for subitems
      } else {
        navigate(`/${text.toLowerCase()}`); // Navigate to the specified page
        onClick(text); // Set active page
      }
    };

    return (
      <>
        <ListItemButton onClick={handleItemClick} sx={{ color: isActive ? '#b3985e' : '#ffffff' }}>
          <ListItemIcon sx={{ color: isActive ? '#b3985e' : '#ffffff' }}>{icon}</ListItemIcon>
          <ListItemText primary={text} />
          {subItems && (isOpen ? <ExpandLess /> : <ExpandMore />)}
        </ListItemButton>
        {subItems && (
          <Collapse in={isOpen} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              {subItems.map((subItem) => (
                <ListItemButton
                  key={subItem}
                  sx={{
                    pl: 9,
                    color: activePage === subItem ? '#b3985e' : '#ffffff'
                  }}
                  onClick={() => {
                    const routePath = `/${text.toLowerCase()}/${subItem.toLowerCase()}`; // Construct the route path
                    navigate(routePath); // Navigate to the sub-item page
                    onClick(subItem); // Set active page
                  }}
                >
                  <ListItemText primary={subItem} />
                </ListItemButton>
              ))}
            </List>
          </Collapse>
        )}
      </>
    );
};

export default SidebarItem;
