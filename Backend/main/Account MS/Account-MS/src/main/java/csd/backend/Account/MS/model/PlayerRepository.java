package csd.backend.Matching.MS.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    List<Player> findByUsername(String username);
    Player findById(int id);
}
