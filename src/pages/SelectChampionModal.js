import React, { useState } from 'react';
import { Box, Typography, Modal, IconButton, Select, MenuItem, InputBase, InputAdornment } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close'
import SearchIcon from '@mui/icons-material/Search'

// Manually importing all the role icons
import assassinIcon from '../assets/championClass/roleicon-assassin.png';
import fighterIcon from '../assets/championClass/roleicon-fighter.png';
import mageIcon from '../assets/championClass/roleicon-mage.png';
import marksmanIcon from '../assets/championClass/roleicon-marksman.png';
import supportIcon from '../assets/championClass/roleicon-support.png';
import tankIcon from '../assets/championClass/roleicon-tank.png';

// Manually importing all the champion images
import annieImage from '../assets/champions/1.png';
import galioImage from '../assets/champions/3.png';
import leBlancImage from '../assets/champions/7.png';
import masterYiImage from '../assets/champions/11.png';
import sorakaImage from '../assets/champions/16.png';
import teemoImage from '../assets/champions/17.png';
import tristanaImage from '../assets/champions/18.png';
import missFortuneImage from '../assets/champions/21.png';
import singedImage from '../assets/champions/27.png';
import sonaImage from '../assets/champions/37.png';
import ahriImage from '../assets/champions/103.png';
import kaynImage from '../assets/champions/141.png';
import kaiSaImage from '../assets/champions/145.png';
import zacImage from '../assets/champions/154.png';
import yasuoImage from '../assets/champions/157.png';
import belVethImage from '../assets/champions/200.png';
import jinxImage from '../assets/champions/222.png';
import sennaImage from '../assets/champions/235.png';
import zedImage from '../assets/champions/238.png';
import viImage from '../assets/champions/254.png';
import namiImage from '../assets/champions/267.png';
import settImage from '../assets/champions/875.png';

const roleIcons = [
  { src: assassinIcon, alt: 'Assassin' },
  { src: fighterIcon, alt: 'Fighter' },
  { src: mageIcon, alt: 'Mage' },
  { src: marksmanIcon, alt: 'Marksman' },
  { src: supportIcon, alt: 'Support' },
  { src: tankIcon, alt: 'Tank' },
];

const champions = [
  { src: kaiSaImage, alt: 'Kai\'Sa', name: 'Kai\'Sa', role: ['marksman', 'assassin'] },
  { src: zacImage, alt: 'Zac', name: 'Zac', role: ['tank', 'fighter'] },
  { src: yasuoImage, alt: 'Yasuo', name: 'Yasuo', role: ['fighter', 'assassin'] },
  { src: belVethImage, alt: 'Bel\'Veth', name: 'Bel\'Veth', role: ['fighter', 'assassin'] },
  { src: jinxImage, alt: 'Jinx', name: 'Jinx', role: ['marksman'] },
  { src: sennaImage, alt: 'Senna', name: 'Senna', role: ['marksman', 'support'] },
  { src: zedImage, alt: 'Zed', name: 'Zed', role: ['assassin'] },
  { src: viImage, alt: 'Vi', name: 'Vi', role: ['fighter'] },
  { src: namiImage, alt: 'Nami', name: 'Nami', role: ['support', 'mage'] },
  { src: settImage, alt: 'Sett', name: 'Sett', role: ['fighter', 'tank'] },
  { src: annieImage, alt: 'Annie', name: 'Annie', role: ['mage'] },
  { src: galioImage, alt: 'Galio', name: 'Galio', role: ['tank', 'mage'] },
  { src: leBlancImage, alt: 'LeBlanc', name: 'LeBlanc', role: ['assassin', 'mage'] },
  { src: masterYiImage, alt: 'Master Yi', name: 'Master Yi', role: ['assassin', 'fighter'] },
  { src: sorakaImage, alt: 'Soraka', name: 'Soraka', role: ['support'] },
  { src: teemoImage, alt: 'Teemo', name: 'Teemo', role: ['marksman', 'assassin'] },
  { src: tristanaImage, alt: 'Tristana', name: 'Tristana', role: ['marksman', 'assassin'] },
  { src: missFortuneImage, alt: 'Miss Fortune', name: 'Miss Fortune', role: ['marksman'] },
  { src: singedImage, alt: 'Singed', name: 'Singed', role: ['fighter', 'tank'] },
  { src: sonaImage, alt: 'Sona', name: 'Sona', role: ['support', 'mage'] },
  { src: ahriImage, alt: 'Ahri', name: 'Ahri', role: ['mage', 'assassin'] },
  { src: kaynImage, alt: 'Kayn', name: 'Kayn', role: ['assassin', 'fighter'] }
];

const SelectChampionModal = ({ open, handleClose }) => {
  // State to track the selected role, sorting option, and search query
  const [selectedRole, setSelectedRole] = useState(null);
  const [sortOption, setSortOption] = useState('latest');
  const [searchQuery, setSearchQuery] = useState('');

  // Handler to select/unselect a role
  const handleRoleClick = (role) => {
    setSelectedRole(role === selectedRole ? null : role); // Deselect if clicked again
  };

  // Handler for sort selection
  const handleSortChange = (event) => {
    setSortOption(event.target.value); // Set the selected sort option
  };

  // Handler for search input change
  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value.toLowerCase()); // Update search query and convert to lowercase for case-insensitive matching
  };

  // Filter champions based on selected role
  let filteredChampions = selectedRole
    ? champions.filter((champion) => champion.role && champion.role.includes(selectedRole))
    : champions;

  // Apply search filtering
  if (searchQuery) {
    filteredChampions = filteredChampions.filter((champion) =>
      champion.name.toLowerCase().includes(searchQuery)
    );
  }

  // Sort champions based on the sort option
  if (sortOption === 'name') {
    // Sort alphabetically by name
    filteredChampions = filteredChampions.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOption === 'latest') {
    // Randomize the order of champions for "latest"
    filteredChampions = filteredChampions.sort(() => Math.random() - 0.5);
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
          p: 4
        }}
      >
        {/* Fixed Header: Title, Role Icons, and Filters */}
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
          {/* Flex container for title and close button */}
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginTop: '-15px'
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

          {/* Role icons and filters container */}
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginTop: 2,
            }}
          >
            {/* Role icons */}
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'row',
              }}
            >
              {roleIcons.map((icon, index) => (
                <Box
                  key={index}
                  component="img"
                  src={icon.src}
                  alt={icon.alt}
                  onClick={() => handleRoleClick(icon.alt.toLowerCase())} // Filter champions on click
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

            {/* Filters */}
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'center',
                gap: 2,
              }}
            >
              {/* Sort Dropdown */}
              <Select
                value={sortOption}
                onChange={handleSortChange} // Handle sort option change
                sx={{
                  color: '#F0E6D2',
                  fontSize: '10px',
                  bgcolor: '#1C1C1C',
                  border: '1px solid #775A27',
                  height: '3vh',
                  width: '120px',
                  paddingRight: '0px',
                  '& .MuiSelect-icon': {
                    color: '#F0E6D2',
                    fontSize: '18px',
                  }
                }}
              >
                <MenuItem value="latest">Sort by Latest</MenuItem>
                <MenuItem value="name">Sort by Name</MenuItem>
              </Select>

              {/* Search Input */}
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
                  value={searchQuery} // Set the input value to searchQuery
                  onChange={handleSearchChange} // Handle search input changes
                  sx={{
                    color: '#F0E6D2',
                    width: '80px',
                    fontSize: '10px',
                  }}
                  startAdornment={(
                    <InputAdornment position="start">
                      <SearchIcon sx={{ color: '#F0E6D2', fontSize: '12px' }} />
                    </InputAdornment>
                  )}
                />
              </Box>
            </Box>
          </Box>
        </Box>

        {/* Scrollable Champion List */}
        <Box
          sx={{
            flex: 1, // Take up remaining space
            paddingTop: 2,
            overflowY: 'auto',
            '&::-webkit-scrollbar': { display: 'none' }, // Hide scrollbar
            msOverflowStyle: 'none',
            scrollbarWidth: 'none'
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
                  textAlign: 'center'
                }}
              >
                <Box
                  component="img"
                  src={champion.src}
                  alt={champion.alt}
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
