package com.dealer.drproj.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dealer.drproj.entity.DealerType;
import com.dealer.drproj.entity.Dealer;

public interface DealerRepository extends JpaRepository<Dealer,String>{
    Optional<Dealer> findByMsisdn(String msisdn);
    List<Dealer> findByType(DealerType Type);
    List<Dealer> findByName(String name);
    List<Dealer> findByNameAndType(String name,DealerType Type);
}