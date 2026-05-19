package com.dealer.drproj.service;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import com.dealer.drproj.entity.*;
import com.dealer.drproj.repository.*;
import com.dealer.drproj.config.*;
import com.dealer.drproj.dto.*;
import com.dealer.drproj.exception.*;

@Service
public class DealerService {
    private final DealerRepository repository;
    private final PasswordEncoder passwordEncoder;
    public DealerService(DealerRepository repository, PasswordEncoder passwordEncoder){this.repository=repository;this.passwordEncoder=passwordEncoder;}

    @Transactional
    public DealerResponse createDealer(DealerRequest request){
        if(repository.findById(request.getMsisdn()).isPresent()){
            throw new DuplicateDealerException("Dealer with this MSISDN exists.");
        }
        Dealer dealer=new Dealer();
        dealer.setName(request.getName());
        dealer.setMsisdn(request.getMsisdn());
        dealer.setPassword(passwordEncoder.encode(request.getPassword()));
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
    public DealerResponse updateDealerStatus(String msisdn, String status, String password){
        Dealer dealer=repository.findById(msisdn).orElseThrow(()->new DealerNotFoundException("Dealer with this msisdn is not found."));
        if(!passwordEncoder.matches(password, dealer.getPassword())){throw new RuntimeException("Invalid Password.");}
        DealerStatus sts=DealerStatus.valueOf(status.toUpperCase());
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
