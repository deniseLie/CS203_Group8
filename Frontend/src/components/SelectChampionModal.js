import React, { useState } from 'react';
import { Box, Typography, Modal, IconButton, Select, MenuItem, InputBase, InputAdornment } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import SearchIcon from '@mui/icons-material/Search';
import getChampionImage from '../util/getChampionImage';

const roleIcons = [
  { src: 'assassin.png', alt: 'Assassin' },
  { src: 'fighter.png', alt: 'Fighter' },
  { src: 'mage.png', alt: 'Mage' },
  { src: 'marksman.png', alt: 'Marksman' },
  { src: 'support.png', alt: 'Support' },
  { src: 'tank.png', alt: 'Tank' },
];

const champions = [
  { name: "Kai'Sa", role: ['marksman', 'assassin'] },
  { name: 'Zac', role: ['tank', 'fighter'] },
  { name: 'Yasuo', role: ['fighter', 'assassin'] },
  { name: "Bel'Veth", role: ['fighter', 'assassin'] },
  { name: 'Jinx', role: ['marksman'] },
  { name: 'Senna', role: ['marksman', 'support'] },
  { name: 'Zed', role: ['assassin'] },
  { name: 'Vi', role: ['fighter'] },
  { name: 'Nami', role: ['support', 'mage'] },
  { name: 'Sett', role: ['fighter', 'tank'] },
  { name: 'Annie', role: ['mage'] },
  { name: 'Galio', role: ['tank', 'mage'] },
  { name: 'LeBlanc', role: ['assassin', 'mage'] },
  { name: 'Master Yi', role: ['assassin', 'fighter'] },
  { name: 'Soraka', role: ['support'] },
  { name: 'Teemo', role: ['marksman', 'assassin'] },
  { name: 'Tristana', role: ['marksman', 'assassin'] },
  { name: 'Miss Fortune', role: ['marksman'] },
  { name: 'Singed', role: ['fighter', 'tank'] },
  { name: 'Sona', role: ['support', 'mage'] },
  { name: 'Ahri', role: ['mage', 'assassin'] },
  { name: 'Kayn', role: ['assassin', 'fighter'] },
];

const SelectChampionModal = ({ open, handleClose, onChampionSelect }) => {
  const [selectedRole, setSelectedRole] = useState(null);
  const [sortOption, setSortOption] = useState('latest');
  const [searchQuery, setSearchQuery] = useState('');

  const handleRoleClick = (role) => {
    setSelectedRole(role === selectedRole ? null : role);
  };

  const handleSortChange = (event) => {
    setSortOption(event.target.value);
  };

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value.toLowerCase());
  };

  let filteredChampions = selectedRole
    ? champions.filter((champion) => champion.role.includes(selectedRole))
    : champions;

  if (searchQuery) {
    filteredChampions = filteredChampions.filter((champion) =>
      champion.name.toLowerCase().includes(searchQuery)
    );
  }

  if (sortOption === 'name') {
    filteredChampions.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOption === 'latest') {
    filteredChampions.sort(() => Math.random() - 0.5);
  }

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="champion-modal-title"
      aria-describedby="champion-modal-description"
    >
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: '80vh',
          height: '80vh',
          bgcolor: '#010A13',
          border: '2px solid #775A27',
          boxShadow: 24,
          display: 'flex',
          flexDirection: 'column',
          p: 4,
        }}
      >
        <Box
          sx={{
            position: 'sticky',
            top: 0,
            bgcolor: '#010A13',
            zIndex: 1,
            paddingBottom: 2,
            borderBottom: '2px solid #775A27',
          }}
        >
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginTop: '-15px',
            }}
          >
            <Typography id="champion-modal-title" className="headerPrimary" component="h2">
              CHOOSE YOUR CHAMPION
            </Typography>
            <IconButton
              onClick={handleClose}
              sx={{
                color: '#F0E6D2',
              }}
            >
              <CloseIcon />
            </IconButton>
          </Box>

          <Box
            sx={{
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginTop: 2,
            }}
          >
            <Box sx={{ display: 'flex', flexDirection: 'row' }}>
              {roleIcons.map((icon, index) => (
                <Box
                  key={index}
                  component="img"
                  src={icon.src}
                  alt={icon.alt}
                  onClick={() => handleRoleClick(icon.alt.toLowerCase())}
                  sx={{
                    width: '4vh',
                    height: '4vh',
                    cursor: 'pointer',
                    transition: 'all 0.1s ease-in-out',
                    filter: selectedRole === icon.alt.toLowerCase() ? 'brightness(0) invert(1)' : 'none',
                    '&:hover': {
                      filter: 'brightness(0) invert(1)',
                    },
                    marginRight: 2,
                  }}
                />
              ))}
            </Box>

            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Select
                value={sortOption}
                onChange={handleSortChange}
                sx={{
                  color: '#F0E6D2',
                  fontSize: '10px',
                  bgcolor: '#1C1C1C',
                  border: '1px solid #775A27',
                  height: '3vh',
                  width: '120px',
                  '& .MuiSelect-icon': {
                    color: '#F0E6D2',
                    fontSize: '18px',
                  },
                }}
              >
                <MenuItem value="latest">Sort by Latest</MenuItem>
                <MenuItem value="name">Sort by Name</MenuItem>
              </Select>

              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  border: '1px solid #775A27',
                  bgcolor: '#1C1C1C',
                  paddingLeft: 1,
                  borderRadius: 1,
                  height: '3vh',
                  width: '120px',
                }}
              >
                <InputBase
                  placeholder="Search"
                  value={searchQuery}
                  onChange={handleSearchChange}
                  sx={{
                    color: '#F0E6D2',
                    width: '80px',
                    fontSize: '10px',
                  }}
                  startAdornment={
                    <InputAdornment position="start">
                      <SearchIcon sx={{ color: '#F0E6D2', fontSize: '12px' }} />
                    </InputAdornment>
                  }
                />
              </Box>
            </Box>
          </Box>
        </Box>

        <Box
          sx={{
            flex: 1,
            paddingTop: 2,
            overflowY: 'auto',
            '&::-webkit-scrollbar': { display: 'none' },
            msOverflowStyle: 'none',
            scrollbarWidth: 'none',
          }}
        >
          <Box
            sx={{
              display: 'flex',
              flexWrap: 'wrap',
              justifyContent: 'flex-start',
              gap: '32px',
            }}
          >
            {filteredChampions.map((champion, index) => (
              <Box
                key={index}
                sx={{
                  width: '18%',
                  maxWidth: '80px',
                  textAlign: 'center',
                }}
                onClick={() => onChampionSelect(champion)}
              >
                <Box
                  component="img"
                  src={getChampionImage(champion.name, 'icon')}
                  alt={champion.name}
                  sx={{
                    width: '12vh',
                    height: '12vh',
                    border: '1px solid transparent',
                    cursor: 'pointer',
                    transition: 'all 0.1s ease-in-out',
                    '&:hover': {
                      border: '1px solid #775A27',
                    },
                  }}
                />
                <Typography sx={{ color: '#F0E6D2', fontSize: '10px' }}>
                  {champion.name}
                </Typography>
              </Box>
            ))}
          </Box>
        </Box>
      </Box>
    </Modal>
  );
};

export default SelectChampionModal;
