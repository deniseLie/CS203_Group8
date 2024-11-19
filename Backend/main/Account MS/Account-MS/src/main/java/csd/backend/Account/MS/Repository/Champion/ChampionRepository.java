package csd.backend.Account.MS.repository.Champion;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.Champion.Champion;

import java.util.*;

public interface ChampionRepository extends JpaRepository<Champion, Long> {
    Optional<Champion> findByChampionId(Long championId);
}

