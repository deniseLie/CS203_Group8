import React, { useState } from 'react';
import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  IconButton,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Button,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';

/**
 * PlayerTable Component
 *
 * This component renders a table displaying player details along with options to edit or delete a player.
 * It includes:
 * - Player data (user ID, profile picture, username, player name, and email).
 * - Actions to edit or delete players.
 * - A confirmation dialog for delete actions.
 *
 * @param {object[]} data - Array of player objects to display.
 * @param {function} onDelete - Callback to handle deleting a player.
 * @param {function} onEdit - Callback to handle editing a player.
 * @param {object} profileImages - Object mapping profile picture names to image paths.
 */
const PlayerTable = ({ data, onDelete, onEdit, profileImages }) => {
  // State to manage the delete confirmation dialog
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false); // Dialog visibility
  const [selectedPlayerId, setSelectedPlayerId] = useState(null); // ID of the player to delete

  /**
   * Open the delete confirmation dialog for a specific player.
   *
   * @param {number} playerId - The ID of the player to delete.
   */
  const handleOpenDeleteDialog = (playerId) => {
    setSelectedPlayerId(playerId); // Set the ID of the player to delete
    setOpenDeleteDialog(true); // Open the dialog
  };

  /**
   * Close the delete confirmation dialog.
   */
  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false); // Close the dialog
    setSelectedPlayerId(null); // Reset the selected player ID
  };

  /**
   * Confirm the deletion of a player.
   */
  const handleConfirmDelete = () => {
    onDelete(selectedPlayerId); // Call the delete callback with the selected player ID
    handleCloseDeleteDialog(); // Close the dialog
  };

  return (
    <Box
      sx={{
        backgroundColor: '#ffffff', // White background
        borderRadius: '8px', // Rounded corners
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', // Subtle shadow for depth
        p: 2, // Padding around the content
      }}
    >
      {/* Title */}
      <Typography variant="h5" sx={{ mb: 2 }}>
        Player Table
      </Typography>

      {/* Player Data Table */}
      <Table>
        {/* Table Header */}
        <TableHead>
          <TableRow>
            <TableCell>User Id</TableCell>
            <TableCell>Profile Picture</TableCell>
            <TableCell>Username</TableCell>
            <TableCell>Playername</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {/* Table Rows */}
          {data.map((player) => (
            <TableRow key={player.id}>
              {/* User ID */}
              <TableCell>{player.userId}</TableCell>

              {/* Profile Picture */}
              <TableCell>
                <img
                  src={profileImages[`player${player.profilePicture}`]} // Dynamic profile picture
                  alt={player.username} // Alternative text for the image
                  width="50" // Set width for the image
                  height="50" // Set height for the image
                  style={{ borderRadius: '50%' }} // Circular style
                />
              </TableCell>

              {/* Username */}
              <TableCell>{player.username}</TableCell>

              {/* Player Name */}
              <TableCell>{player.playername}</TableCell>

              {/* Email */}
              <TableCell>{player.email}</TableCell>

              {/* Actions (Edit/Delete) */}
              <TableCell>
                {/* Edit Button */}
                <IconButton color="primary" onClick={() => onEdit(player)}>
                  <EditIcon />
                </IconButton>

                {/* Delete Button */}
                <IconButton color="error" onClick={() => handleOpenDeleteDialog(player.id)}>
                  <DeleteIcon />
                </IconButton>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      {/* Footer: Showing number of players */}
      <Typography sx={{ mt: 2, fontSize: '0.875rem', color: 'text.secondary' }}>
        Showing {data.length} players
      </Typography>

      {/* Delete Confirmation Dialog */}
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
