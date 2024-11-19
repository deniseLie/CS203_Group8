package csd.backend.Account.MS.repository.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.Player.Player;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findById(Long playerId);
    Optional<Player> findByUsername(String username);

    boolean existsById(Long playerId);
    void deleteByPlayerId(Long playerId);
}
