package csd.backend.Account.MS.Repository.Champion;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.Champion.Champion;
import java.util.*;

public interface ChampionRepository extends JpaRepository<Champion, Long> {
    Optional<Champion> findByChampionId(Long championId);
}

