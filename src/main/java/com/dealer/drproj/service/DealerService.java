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

    public DealerResponse getDealerByMsisdn(String msisdn){
        if(msisdn!=null && !msisdn.matches("^\\d{10}$")){
            throw new InvalidMsisdnException("MSISDN must be of length 10 and made of digits.");
        }
        Dealer dealer=repository.findById(msisdn).orElseThrow(()->new DealerNotFoundException("Dealer with this msisdn is not found."));
        return mapToResponse(dealer);
    }

    // public List<DealerResponse> getDealers(){
    //     return repository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    // }
    // public List<DealerResponse> getDealerByType(String type){
    //     DealerType dtype;
    //     try{dtype=DealerType.valueOf(type.toUpperCase());}
    //     catch(IllegalArgumentException ex){throw new InvalidTypeException("Invalid dealer type: "+type+", The type has to be RETAILER or DISTRIBUTOR or FRANCHISE");}
    //     return repository.findByType(dtype).stream().map(this::mapToResponse).collect(Collectors.toList());
    // }

    public List<DealerResponse> getDealers(String name,String type){
        List<Dealer> result;
        if(type!=null && name!=null){
            DealerType dtype;
            try{dtype=DealerType.valueOf(type.toUpperCase());}
            catch(IllegalArgumentException ex){throw new InvalidTypeException("Invalid dealer type: "+type+", The type has to be RETAILER or DISTRIBUTOR or FRANCHISE");}
            result=repository.findByNameAndType(name, dtype);
        }
        else if(name!=null){
            result=repository.findByName(name);
        }
        else if(type!=null){
            DealerType dtype;
            try{dtype=DealerType.valueOf(type.toUpperCase());}
            catch(IllegalArgumentException ex){throw new InvalidTypeException("Invalid dealer type: "+type+", The type has to be RETAILER or DISTRIBUTOR or FRANCHISE");}
            result=repository.findByType(dtype);
        }
        else{
            result=repository.findAll();
        }
        return result.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // @Transactional
    // public DealerResponse updateDealerStatus(String msisdn, String status, String password){
    //     if(msisdn!=null && !msisdn.matches("^//d{10}$")){
    //         throw new InvalidMsisdnException("MSISDN must be of length 10 and made of digits.");
    //     }
    //     Dealer dealer=repository.findById(msisdn).orElseThrow(()->new DealerNotFoundException("Dealer with this msisdn is not found."));
    //     if(!passwordEncoder.matches(password, dealer.getPassword())){throw new RuntimeException("Invalid Password.");}
    //     DealerStatus sts=DealerStatus.valueOf(status.toUpperCase());
    //     dealer.setStatus(sts);
    //     repository.save(dealer);
    //     return mapToResponse(dealer);
    // }

    @Transactional
    public DealerResponse updateDealer(String msisdn, StatusUpdateRequest request){
        if(msisdn==null || !msisdn.matches("^\\d{10}$")){
            throw new InvalidMsisdnException("MSISDN must be of length 10 and contain digits only.");
        }
        if(request.getName()==null && request.getType()==null && request.getStatus()==null){
            throw new EmptyUpdateRequestException("Enter atleast on of the following: NAME or TYPE or STATUS");
        }
        Dealer dealer=repository.findById(msisdn).orElseThrow(()->new DealerNotFoundException("Dealer with this MSISDN does not exist."));
        if(!passwordEncoder.matches(request.getPassword(),dealer.getPassword())){
            throw new InvalidPasswordException("Invalid Password.");
        }
        if(request.getName()!=null){dealer.setName(request.getName());}
        if(request.getType()!=null){
            try{dealer.setType(DealerType.valueOf(request.getType().toUpperCase()));}
            catch(IllegalArgumentException ex){throw new InvalidTypeException("Invalid dealer type: "+request.getType()+", The type has to be RETAILER or DISTRIBUTOR or FRANCHISE");}
        }
        if(request.getStatus()!=null){
            try{dealer.setStatus(DealerStatus.valueOf(request.getStatus().toUpperCase()));}
            catch(IllegalArgumentException ex){throw new InvalidStatusException("Invalid dealer status: "+request.getStatus()+", The status has to be ACTIVE or INACTIVE.");}
        }
        repository.save(dealer);
        return mapToResponse(dealer);
    }

    private DealerResponse mapToResponse(Dealer dealer){
        return new DealerResponse(
            dealer.getName(),dealer.getMsisdn(),dealer.getType(),dealer.getStatus(),dealer.getCreatedAt()
        );
    }
}
