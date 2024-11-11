package csd.backend.Account.MS.Repository.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.Model.Player.Player;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);

    boolean existsById(Long playerId);
}
