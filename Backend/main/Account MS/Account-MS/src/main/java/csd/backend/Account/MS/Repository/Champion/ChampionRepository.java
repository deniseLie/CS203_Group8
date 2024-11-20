package csd.backend.Account.MS.repository.champion;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.champion.Champion;

import java.util.*;

public interface ChampionRepository extends JpaRepository<Champion, Long> {
    Optional<Champion> findByChampionId(Long championId);
}

