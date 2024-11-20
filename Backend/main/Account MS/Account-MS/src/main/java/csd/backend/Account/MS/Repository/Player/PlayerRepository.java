package csd.backend.Account.MS.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.player.Player;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findById(Long playerId);
    Optional<Player> findByUsername(String username);

    boolean existsById(Long playerId);
    void deleteByPlayerId(Long playerId);
}
