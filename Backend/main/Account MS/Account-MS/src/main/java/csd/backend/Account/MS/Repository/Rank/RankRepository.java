package csd.backend.Account.MS.Repository.Rank;

import org.springframework.data.jpa.repository.JpaRepository;
import csd.backend.Account.MS.Model.Rank.Rank;

public interface RankRepository extends JpaRepository<Rank, Long>{
    
}
