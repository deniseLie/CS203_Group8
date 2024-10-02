package csd.backend.Ranking.MS;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    private TournamentRepository tournamentRepository;

    @Autowired 
    public TournamentService(TournamentRepository tournamentRepository){
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }
    
}
