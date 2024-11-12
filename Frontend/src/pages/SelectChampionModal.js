import React, { useState } from 'react';
import { Box, Typography, Modal, IconButton, Select, MenuItem, InputBase, InputAdornment } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close'
import SearchIcon from '@mui/icons-material/Search'
import { championsAssets, roleIconsAssets } from '../util/importAssets';

//========= ASSIGNMENT: all the role and champion icons =========
const roleIcons = [
  { src: roleIconsAssets.assassin, alt: 'Assassin' },
  { src: roleIconsAssets.fighter, alt: 'Fighter' },
  { src: roleIconsAssets.mage, alt: 'Mage' },
  { src: roleIconsAssets.marksman, alt: 'Marksman' },
  { src: roleIconsAssets.support, alt: 'Support' },
  { src: roleIconsAssets.tank, alt: 'Tank' },
];

const champions = [
  { src: championsAssets.kaiSa, alt: 'Kai\'Sa', name: 'Kai\'Sa', role: ['marksman', 'assassin'] },
  { src: championsAssets.zac, alt: 'Zac', name: 'Zac', role: ['tank', 'fighter'] },
  { src: championsAssets.yasuo, alt: 'Yasuo', name: 'Yasuo', role: ['fighter', 'assassin'] },
  { src: championsAssets.belVeth, alt: 'Bel\'Veth', name: 'Bel\'Veth', role: ['fighter', 'assassin'] },
  { src: championsAssets.jinx, alt: 'Jinx', name: 'Jinx', role: ['marksman'] },
  { src: championsAssets.senna, alt: 'Senna', name: 'Senna', role: ['marksman', 'support'] },
  { src: championsAssets.zed, alt: 'Zed', name: 'Zed', role: ['assassin'] },
  { src: championsAssets.vi, alt: 'Vi', name: 'Vi', role: ['fighter'] },
  { src: championsAssets.nami, alt: 'Nami', name: 'Nami', role: ['support', 'mage'] },
  { src: championsAssets.sett, alt: 'Sett', name: 'Sett', role: ['fighter', 'tank'] },
  { src: championsAssets.annie, alt: 'Annie', name: 'Annie', role: ['mage'] },
  { src: championsAssets.galio, alt: 'Galio', name: 'Galio', role: ['tank', 'mage'] },
  { src: championsAssets.leBlanc, alt: 'LeBlanc', name: 'LeBlanc', role: ['assassin', 'mage'] },
  { src: championsAssets.masterYi, alt: 'Master Yi', name: 'Master Yi', role: ['assassin', 'fighter'] },
  { src: championsAssets.soraka, alt: 'Soraka', name: 'Soraka', role: ['support'] },
  { src: championsAssets.teemo, alt: 'Teemo', name: 'Teemo', role: ['marksman', 'assassin'] },
  { src: championsAssets.tristana, alt: 'Tristana', name: 'Tristana', role: ['marksman', 'assassin'] },
  { src: championsAssets.missFortune, alt: 'Miss Fortune', name: 'Miss Fortune', role: ['marksman'] },
  { src: championsAssets.singed, alt: 'Singed', name: 'Singed', role: ['fighter', 'tank'] },
  { src: championsAssets.sona, alt: 'Sona', name: 'Sona', role: ['support', 'mage'] },
  { src: championsAssets.ahri, alt: 'Ahri', name: 'Ahri', role: ['mage', 'assassin'] },
  { src: championsAssets.kayn, alt: 'Kayn', name: 'Kayn', role: ['assassin', 'fighter'] }
];

// ========= END OF ASSIGNMENT =========

const SelectChampionModal = ({ open, handleClose, onChampionSelect }) => {
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
                onClick={() => onChampionSelect(champion)} // Call onChampionSelect when clicked
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
