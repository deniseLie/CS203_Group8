import React from 'react';
import { ListItemButton, ListItemIcon, ListItemText, Collapse, List } from '@mui/material';
import { ExpandLess, ExpandMore } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

/**
 * SidebarItem Component
 *
 * This component renders a sidebar item with optional sub-items. It supports navigation,
 * dynamic styling for active items, and expandable/collapsible functionality for sub-items.
 *
 * @param {string} text - The text to display for the item.
 * @param {ReactNode} icon - The icon to display for the item.
 * @param {string[]} [subItems] - Optional list of sub-items for this sidebar item.
 * @param {boolean} isOpen - Whether the sub-items are currently expanded.
 * @param {function} onToggle - Function to toggle the expanded/collapsed state.
 * @param {function} onClick - Callback to handle setting the active page.
 * @param {boolean} isActive - Whether the item is the currently active page.
 * @param {string} activePage - The name of the currently active page.
 */
const SidebarItem = ({ text, icon, subItems, isOpen, onToggle, onClick, isActive, activePage }) => {
  const navigate = useNavigate();

  /**
   * Handles clicking the main item. Navigates to the page or toggles the dropdown.
   */
  const handleItemClick = () => {
    if (subItems) {
      onToggle(); // Toggle the dropdown for sub-items
    } else {
      navigate(`/${text.toLowerCase()}`); // Navigate to the main item's route
      onClick(text); // Update the active page state
    }
  };

  return (
    <>
      {/* Main Sidebar Item */}
      <ListItemButton
        onClick={handleItemClick} // Handle click for navigation or toggling
        sx={{
          color: isActive ? '#b3985e' : '#ffffff', // Highlight active items
        }}
      >
        {/* Icon for the item */}
        <ListItemIcon
          sx={{
            color: isActive ? '#b3985e' : '#ffffff', // Highlight icon for active items
          }}
        >
          {icon}
        </ListItemIcon>

        {/* Text label for the item */}
        <ListItemText primary={text} />

        {/* Expand/collapse icon for items with sub-items */}
        {subItems && (isOpen ? <ExpandLess /> : <ExpandMore />)}
      </ListItemButton>

      {/* Sub-items Section */}
      {subItems && (
        <Collapse in={isOpen} timeout="auto" unmountOnExit>
          <List component="div" disablePadding>
            {subItems.map((subItem) => (
              <ListItemButton
                key={subItem} // Unique key for each sub-item
                sx={{
                  pl: 9, // Indentation for sub-items
                  color: activePage === subItem ? '#b3985e' : '#ffffff', // Highlight active sub-items
                }}
                onClick={() => {
                  const routePath = `/${text.toLowerCase()}/${subItem.toLowerCase()}`; // Construct the route path
                  navigate(routePath); // Navigate to the sub-item route
                  onClick(subItem); // Update the active page state
                }}
              >
                {/* Text label for the sub-item */}
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
