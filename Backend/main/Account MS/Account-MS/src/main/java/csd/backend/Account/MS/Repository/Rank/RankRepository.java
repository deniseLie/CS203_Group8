package csd.backend.Account.MS.repository.rank;

import org.springframework.data.jpa.repository.JpaRepository;

import csd.backend.Account.MS.model.rank.Rank;

public interface RankRepository extends JpaRepository<Rank, Long>{
    
}
