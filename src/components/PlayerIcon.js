import React from 'react';
import { Avatar } from '@mui/material'; 
import { Link } from 'react-router-dom'; 

const PlayerIcon = ({
  alt = "Player Avatar", 
  src, 
  width = 50, 
  height = 50, 
  link = "/profile", 
  clickable = true, 
  className = "",
}) => {
  const outerStyle = {
    width: `${width + 8}vh`,  // Add 8px to account for the border/padding
    height: `${height + 8}vh`, // Add 8px to account for the border/padding
  };

  return (
    <div
      className={`avatarWithGradientBorder ${className}`}
      style={outerStyle} // Apply dynamic styles here
    >
      {clickable ? (
        <Link to={link}>
          <Avatar
            alt={alt}
            src={src}
            sx={{
              width: `${width}vh`, 
              height: `${height}vh`,
              borderRadius: '50%',
            }}
          />
        </Link>
      ) : (
        <Avatar
          alt={alt}
          src={src}
          sx={{
            width: `${width}vh`, 
            height: `${height}vh`,
            borderRadius: '50%',
          }}
        />
      )}
    </div>
  );
};

export default PlayerIcon;
