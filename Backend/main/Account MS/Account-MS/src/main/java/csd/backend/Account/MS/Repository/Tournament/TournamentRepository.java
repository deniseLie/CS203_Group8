package csd.backend.Account.MS.repository.Tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.Tournament.*;

import java.util.*;

public interface TournamentRepository extends JpaRepository<Tournament, Long>{

    // Find tournament by ID
    Optional<Tournament> findById(Long tournamentId);

    // Find all tournaments
    List<Tournament> findAll();
}
