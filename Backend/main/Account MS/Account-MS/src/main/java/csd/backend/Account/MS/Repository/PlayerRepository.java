package csd.backend.Account.MS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.Player;
import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);
}
