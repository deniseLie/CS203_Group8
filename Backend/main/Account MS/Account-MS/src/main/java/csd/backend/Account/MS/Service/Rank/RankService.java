package csd.backend.Account.MS.service.Rank;

import csd.backend.Account.MS.exception.*;
import csd.backend.Account.MS.model.Rank.Rank;
import csd.backend.Account.MS.repository.Rank.RankRepository;

import org.springframework.stereotype.Service;

@Service
public class RankService {

    private final RankRepository rankRepository;

    public RankService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }
    // Method to determine the rankId based on the points
    public Long getRankIdByPoints(int points) {
        // Find the rank by comparing the points with the points required for each rank
        Rank rank = rankRepository.findAll().stream()
                .filter(r -> r.getPointsRequired() <= points)  // Filter ranks by points
                .max((r1, r2) -> Integer.compare(r1.getPointsRequired(), r2.getPointsRequired())) 
                .orElseThrow(() -> new RankPointNotFoundException(points)); 

        return rank.getRankId();  // Return the rankId
    }

    // Method to get rank name by rankId
    public String getRankNameById(Long rankId) {
        // Find the rank by comparing the rankId
        Rank rank = rankRepository.findById(rankId).orElseThrow(() -> new RankNotFoundException(rankId));

        return rank.getRankName();  // Return the rankName
    }
}
