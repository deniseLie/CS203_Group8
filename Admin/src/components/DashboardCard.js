import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

const DashboardCard = ({ title, count, subCount, content, icon }) => (
  <Card sx={{ width: '200px', textAlign: 'center' }}>
    <CardContent>
      {icon && <Box sx={{ mb: 1 }}>{icon}</Box>}
      <Typography variant="h6">{title}</Typography>
      {count && <Typography variant="h4" sx={{ fontWeight: 'bold' }}>{count}</Typography>}
      {subCount && <Typography variant="body2" color="textSecondary">{subCount}</Typography>}
      {content && content.map((item, index) => (
        <Typography key={index} variant="body2" sx={{ color: '#3f51b5', textDecoration: 'underline' }}>
          {item}
        </Typography>
      ))}
    </CardContent>
  </Card>
);

export default DashboardCard;
