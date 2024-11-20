package com.loltournament.loginservice;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.loltournament.loginservice.exception.UserNotFoundException;
import com.loltournament.loginservice.model.Player;
import com.loltournament.loginservice.repository.PlayerRepository;
import com.loltournament.loginservice.security.SecurityConfig;
import com.loltournament.loginservice.service.PlayerService;

@SpringBootTest(classes = PlayerService.class)
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepository;

    @Mock
    private SecurityConfig securityConfig;

    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockPlayer = new Player();
        mockPlayer.setUserId(1L);
        mockPlayer.setUsername("testuser");
        mockPlayer.setEmail("testuser@example.com");
        mockPlayer.setPassword("encrypted_password");
        mockPlayer.setPlayername("Test Player");
    }

    @Test
    void contextLoads() {
        // Verifies the context loads without issues
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        Mockito.when(playerRepository.findByUsername("testuser")).thenReturn(Optional.of(mockPlayer));

        // Act
        UserDetails userDetails = playerService.loadUserByUsername("testuser");

        // Assert
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("testuser", userDetails.getUsername());
        Mockito.verify(playerRepository, Mockito.times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        Mockito.when(playerRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UserNotFoundException.class, () -> playerService.loadUserByUsername("unknownuser"));
        Mockito.verify(playerRepository, Mockito.times(1)).findByUsername("unknownuser");
    }

    @Test
    void testLoadUserById_UserExists() {
        // Arrange
        Mockito.when(playerRepository.findByUserId(1L)).thenReturn(Optional.of(mockPlayer));

        // Act
        UserDetails userDetails = playerService.loadUserById(1L);

        // Assert
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("testuser", userDetails.getUsername());
        Mockito.verify(playerRepository, Mockito.times(1)).findByUserId(1L);
    }

    @Test
    void testLoadUserById_UserNotFound() {
        // Arrange
        Mockito.when(playerRepository.findByUserId(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UserNotFoundException.class, () -> playerService.loadUserById(2L));
        Mockito.verify(playerRepository, Mockito.times(1)).findByUserId(2L);
    }

    @Test
    void testSaveUser() {
        // Arrange
        Mockito.when(playerRepository.save(Mockito.any(Player.class))).thenReturn(mockPlayer);

        // Act
        Long userId = playerService.saveUser(mockPlayer);

        // Assert
        Assertions.assertNotNull(userId);
        Assertions.assertEquals(1L, userId);
        Mockito.verify(playerRepository, Mockito.times(1)).save(mockPlayer);
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(mockPlayer));
        Mockito.when(playerRepository.save(Mockito.any(Player.class))).thenReturn(mockPlayer);

        // Act
        Player updatedPlayer = playerService.updateUser(1L, "newemail@example.com", "newpassword", "New Player Name", "newusername");

        // Assert
        Assertions.assertNotNull(updatedPlayer);
        Assertions.assertEquals("newemail@example.com", updatedPlayer.getEmail());
        Assertions.assertEquals("newusername", updatedPlayer.getUsername());
        Assertions.assertEquals("New Player Name", updatedPlayer.getPlayername());
        Mockito.verify(playerRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(playerRepository, Mockito.times(1)).save(Mockito.any(Player.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        Mockito.when(playerRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(UserNotFoundException.class, () -> 
            playerService.updateUser(2L, "newemail@example.com", "newpassword", "New Player Name", "newusername")
        );
        Mockito.verify(playerRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(playerRepository, Mockito.never()).save(Mockito.any(Player.class));
    }
}
