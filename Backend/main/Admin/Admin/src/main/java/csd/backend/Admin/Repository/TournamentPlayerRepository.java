package csd.backend.Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import csd.backend.Admin.Model.*;

public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Integer>{
    
}
