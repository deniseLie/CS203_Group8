package csd.backend.Admin.Service;

import csd.backend.Admin.Model.*;
import csd.backend.Admin.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TournamentResultService {

    @Autowired
    private TournamentResultRepository tournamentResultRepository;

    public TournamentResult getTournamentResultByTournamentId(int tournamentId) {
        Optional<TournamentResult> result = tournamentResultRepository.findById(tournamentId);
        return result.orElse(null); // Return null if not found, or handle error as needed
    }
    public TournamentResult createTournamentResult(TournamentResultRequest request) {
        TournamentResult tournamentResult = new TournamentResult();
        tournamentResult.setTournamentId(request.getTournamentId());
        tournamentResult.setPlayer1(request.getPlayer1());
        tournamentResult.setPlayer2(request.getPlayer2());
        tournamentResult.setPlayer3(request.getPlayer3());
        tournamentResult.setPlayer4(request.getPlayer4());
        tournamentResult.setPlayer5(request.getPlayer5());
        tournamentResult.setPlayer6(request.getPlayer6());
        tournamentResult.setPlayer7(request.getPlayer7());
        tournamentResult.setPlayer8(request.getPlayer8());
        tournamentResult.setWinner(request.getWinner());
        tournamentResult.setLoser(request.getLoser());

        return tournamentResultRepository.save(tournamentResult);
    }
}

