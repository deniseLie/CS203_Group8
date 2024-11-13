import React, { useState } from 'react';
import { Box, Typography, Table, TableBody, TableCell, TableHead, TableRow, IconButton, Dialog, DialogActions, DialogContent, DialogTitle, Button } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { Link } from 'react-router-dom';

const PlayerTable = ({ data, onDelete }) => {
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [selectedPlayerId, setSelectedPlayerId] = useState(null);

  const handleOpenDeleteDialog = (playerId) => {
    setSelectedPlayerId(playerId);
    setOpenDeleteDialog(true);
  };

  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false);
    setSelectedPlayerId(null);
  };

  const handleConfirmDelete = () => {
    onDelete(selectedPlayerId);
    handleCloseDeleteDialog();
  };

  return (
    <Box sx={{ backgroundColor: '#ffffff', borderRadius: '8px', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', p: 2 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>Player Table</Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Player Id</TableCell>
            <TableCell>Username</TableCell>
            <TableCell>Playername</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Auth Provider</TableCell>
            <TableCell>Details</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((player, index) => (
            <TableRow key={index}>
              <TableCell>{player.id}</TableCell>
              <TableCell>{player.username}</TableCell>
              <TableCell>{player.playername}</TableCell>
              <TableCell>{player.email}</TableCell>
              <TableCell>{player.authProvider}</TableCell>
              <TableCell><Link to={`/players/edit/${player.id}`}>View</Link></TableCell>
              <TableCell>
                <IconButton color="error" onClick={() => handleOpenDeleteDialog(player.id)}>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Typography sx={{ mt: 2, fontSize: '0.875rem', color: 'text.secondary' }}>
        Showing {data.length} players
      </Typography>

      {/* Confirmation Dialog */}
      <Dialog open={openDeleteDialog} onClose={handleCloseDeleteDialog}>
        <DialogTitle>Delete User</DialogTitle>
        <DialogContent>
          <Typography>Are you sure you want to delete this user?</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDeleteDialog} color="primary">
            Cancel
          </Button>
          <Button onClick={handleConfirmDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default PlayerTable;
