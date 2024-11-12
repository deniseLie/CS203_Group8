package csd.backend.Admin.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Admin.Model.User.Player;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long>{
    Optional<Player> findByPlayerId(Long playerId);
}