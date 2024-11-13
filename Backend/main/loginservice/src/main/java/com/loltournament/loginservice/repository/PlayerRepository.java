package com.loltournament.loginservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.loltournament.loginservice.model.Player;
// import java.util.List;


public interface PlayerRepository extends JpaRepository<Player, Long>  {
    Optional<Player> findByPlayerId(Long playerId);
    Optional<Player> findByUsername(String username);
    Optional<Player> findByEmail(String email);
    Optional<Player> findByUserId(Long userId);
}
