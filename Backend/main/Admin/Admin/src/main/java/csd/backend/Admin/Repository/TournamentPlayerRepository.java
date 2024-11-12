package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import csd.backend.Admin.Model.*;
import csd.backend.Admin.Model.Tournament.TournamentPlayer;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Integer>{
    
}
