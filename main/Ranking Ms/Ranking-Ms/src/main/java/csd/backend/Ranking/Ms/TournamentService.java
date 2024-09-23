package csd.backend.Ranking.Ms;

import java.util.*;

public class TournamentService {

    private TournamentRepository tournamentRepository;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }
    
}
