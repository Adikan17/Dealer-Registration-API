package com.dealer.drproj.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dealer.drproj.entity.*;
import com.dealer.drproj.repository.*;
import com.dealer.drproj.dto.*;
import com.dealer.drproj.exception.*;

@Service
public class DealerService {
    private final DealerRepository repository;
    public DealerService(DealerRepository repository){this.repository=repository;}

    @Transactional
    public DealerResponse createDealer(DealerRequest request){
        if(repository.findById(request.getMsisdn()).isPresent()){
            throw new DuplicateDealerException("Dealer with this MSISDN exists.");
        }
        Dealer dealer=new Dealer();
        dealer.setName(request.getName());
        dealer.setMsisdn(request.getMsisdn());
        dealer.setType(request.getType());
        repository.save(dealer);
        return mapToResponse(dealer);
    }

    public List<DealerResponse> getDealers(){
        return repository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public DealerResponse getDealerByMsisdn(String msisdn){
        Dealer dealer=repository.findById(msisdn).orElseThrow(()->new DealerNotFoundException("Dealer with this msisdn is not found."));
        return mapToResponse(dealer);
    }

    public List<DealerResponse> getDealerByType(String type){
        DealerType dtype;
        try{dtype=DealerType.valueOf(type.toUpperCase());}
        catch(IllegalArgumentException ex){throw new IllegalArgumentException("Invalid dealer type");}
        return repository.findByType(dtype).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public DealerResponse updateDealerStatus(String msisdn, String status){
        Dealer dealer=repository.findById(msisdn).orElseThrow(()->new DealerNotFoundException("Dealer with this msisdn is not found."));
        DealerStatus sts=DealerStatus.valueOf(status);
        dealer.setStatus(sts);
        repository.save(dealer);
        return mapToResponse(dealer);
    }

    private DealerResponse mapToResponse(Dealer dealer){
        return new DealerResponse(
            dealer.getName(),dealer.getMsisdn(),dealer.getType(),dealer.getStatus(),dealer.getCreatedAt()
        );
    }
}
