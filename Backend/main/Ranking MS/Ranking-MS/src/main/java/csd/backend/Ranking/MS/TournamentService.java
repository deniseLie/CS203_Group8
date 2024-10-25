package csd.backend.Ranking.MS;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TournamentService {
    @Autowired
    private RestTemplate restTemplate;
    private final String MATCHING_MS_BASE_URL = "http://localhost:8081"; 

    private TournamentRepository tournamentRepository;

    @Autowired 
    public TournamentService(TournamentRepository tournamentRepository){
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament createTournament(Tournament tournament) {
        if (tournament.getTimestampStart().isAfter(tournament.getTimestampEnd())) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        return tournamentRepository.save(tournament);
    }

    public List<Player> getPlayersByIds(List<Long> playerIds) {
        List<Player> players = new ArrayList<>();

        for (Long id : playerIds) {
            String url = MATCHING_MS_BASE_URL + "/getPlayerById/" + id;
            Player player = restTemplate.getForObject(url, Player.class);
            players.add(player);
        }

        return players;
    }
    
}
