import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

/**
 * DashboardCard Component
 *
 * This component renders a card to display statistics or content on the dashboard.
 * It supports a title, main count, sub-count, list of additional content, and an optional icon.
 *
 * @param {string} title - The title of the card.
 * @param {number|string} [count] - The main numerical or string value to display (e.g., total count).
 * @param {string} [subCount] - A secondary piece of information (e.g., ongoing count).
 * @param {array} [content] - An array of additional content items (e.g., top players).
 * @param {ReactNode} [icon] - An optional icon to display above the title.
 */
const DashboardCard = ({ title, count, subCount, content, icon }) => (
  <Card
    sx={{
      width: '200px', // Fixed width for consistency
      textAlign: 'center', // Center-align content
      boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.1)', // Subtle shadow for depth
      borderRadius: '12px', // Rounded corners for aesthetics
    }}
  >
    <CardContent>
      {/* Display the icon if provided */}
      {icon && (
        <Box sx={{ mb: 1, color: '#1976d2' }}> {/* Add spacing below the icon */}
          {icon}
        </Box>
      )}

      {/* Card title */}
      <Typography variant="h6">{title}</Typography>

      {/* Main count value */}
      {count && (
        <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
          {count}
        </Typography>
      )}

      {/* Sub-count value */}
      {subCount && (
        <Typography variant="body2" color="textSecondary">
          {subCount}
        </Typography>
      )}

      {/* List of additional content (if provided) */}
      {content &&
        content.map((item, index) => (
          <Typography
            key={index} // Unique key for each content item
            variant="body2"
            sx={{
              color: '#1976d2', // Blue color for emphasis
              textDecoration: 'underline', // Underline for visual distinction
            }}
          >
            {item}
          </Typography>
        ))}
    </CardContent>
  </Card>
);

export default DashboardCard;
