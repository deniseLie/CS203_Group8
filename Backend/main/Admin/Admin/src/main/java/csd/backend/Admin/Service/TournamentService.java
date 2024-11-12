package csd.backend.Admin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import csd.backend.Admin.Repository.*;
import java.util.*;
import csd.backend.Admin.Model.*;
import csd.backend.Admin.Model.Tournament.Tournament;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository; // Tournament ADMIN FUNCT
    
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    // Function create tournament
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    // Method get all tournament
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }
    
    // not necessary.. i think..
    // public String deleteMatch(int matchId) {
    //     if (tournamentRepository.existsById(matchId)) {
    //         tournamentRepository.deleteById(matchId);
    //         return "Tournament deleted successfully";
    //     } else {
    //         return "Tournament not found";
    //     }
    // }
}
