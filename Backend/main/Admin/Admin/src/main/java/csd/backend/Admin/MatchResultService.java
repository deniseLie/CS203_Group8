package csd.backend.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MatchResultService {

    @Autowired
    private MatchResultRepository matchResultRepository;

    public MatchResult getMatchResultByTournamentId(int tournamentId) {
        Optional<MatchResult> result = matchResultRepository.findById(tournamentId);
        return result.orElse(null); // Return null if not found, or handle error as needed
    }
}

