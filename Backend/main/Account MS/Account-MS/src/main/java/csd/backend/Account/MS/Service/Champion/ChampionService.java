package csd.backend.Account.MS.service.champion;

import csd.backend.Account.MS.exception.champion.*;
import csd.backend.Account.MS.model.champion.*;
import csd.backend.Account.MS.repository.champion.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChampionService {

    private final ChampionRepository championRepository;

    public ChampionService(ChampionRepository championRepository) {
        this.championRepository = championRepository;
    }

    // Get all champions
    public List<Champion> getAllChampions() {
        return championRepository.findAll();
    }

    // Get champion by ID
    public Champion getChampionById(Long championId) {
        return championRepository.findById(championId)
            .orElseThrow(() -> new ChampionNotFoundException(championId));
    }

    // Add a new champion
    public Champion addChampion(Champion champion) {
        return championRepository.save(champion);
    }

    // Update an existing champion
    public Champion updateChampion(Long championId, Champion championDetails) {
        Champion champion = championRepository.findById(championId)
                .orElseThrow(() -> new ChampionNotFoundException(championId));

        champion.setChampionName(championDetails.getChampionName());
        champion.setChampionRole(championDetails.getChampionRole());

        return championRepository.save(champion);
    }

    // Delete a champion
    public void deleteChampion(Long championId) {
        Champion champion = championRepository.findById(championId)
                .orElseThrow(() -> new ChampionNotFoundException(championId));

        championRepository.delete(champion);
    }
}
