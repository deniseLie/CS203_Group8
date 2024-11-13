package csd.backend.Admin.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Admin.Model.Tournament.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findByTournamentId(Long id);
}
