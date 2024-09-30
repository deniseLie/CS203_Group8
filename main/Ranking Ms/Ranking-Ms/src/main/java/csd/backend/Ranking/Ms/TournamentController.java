package csd.backend.Ranking.Ms;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

    
    

    @GetMapping("/getAllTournaments")
    public List<Tournament> getAllTournaments(){
        return tournamentService.getAllTournaments();
    }

    //not fully implemented yet
    // @GetMapping("/{id}")
    // public String getTournamentById(@PathVariable Long id) {
    //     // return "Fetching tournament with ID: " + id;
    //     return tournamentService.
    // }

}
