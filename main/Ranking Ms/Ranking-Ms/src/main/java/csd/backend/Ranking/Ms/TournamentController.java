package csd.backend.Ranking.Ms;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class TournamentController {
    private TournamentService tournamentService;

    @GetMapping("/getAllTournaments")
    public List<Tournament> getAllTournaments(){
        return tournamentService.getAllTournaments();
    }

}
