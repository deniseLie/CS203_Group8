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
    public MatchResult createMatchResult(MatchResultRequest request) {
        MatchResult matchResult = new MatchResult();
        matchResult.setTournamentId(request.getTournamentId());
        matchResult.setPlayer1(request.getPlayer1());
        matchResult.setPlayer2(request.getPlayer2());
        matchResult.setPlayer3(request.getPlayer3());
        matchResult.setPlayer4(request.getPlayer4());
        matchResult.setPlayer5(request.getPlayer5());
        matchResult.setPlayer6(request.getPlayer6());
        matchResult.setPlayer7(request.getPlayer7());
        matchResult.setPlayer8(request.getPlayer8());
        matchResult.setWinner(request.getWinner());
        matchResult.setLoser(request.getLoser());

        return matchResultRepository.save(matchResult);
    }
}

