package com.dealer.drproj.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.dealer.drproj.JWT.*;
import com.dealer.drproj.config.*;
import com.dealer.drproj.entity.*;
import com.dealer.drproj.repository.*;
import com.dealer.drproj.dto.*;
import com.dealer.drproj.exception.*;

@Service
public class DealerService {
    private final DealerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public DealerService(DealerRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public DealerResponse createDealer(DealerRequest request) {
        if (repository.findById(request.getMsisdn()).isPresent()) {
            throw new DuplicateDealerException("Dealer with this MSISDN exists.");
        }
        Dealer dealer = new Dealer();
        dealer.setName(request.getName());
        dealer.setMsisdn(request.getMsisdn());
        dealer.setPassword(passwordEncoder.encode(request.getPassword()));
        dealer.setType(request.getType());
        repository.save(dealer);
        return mapToResponse(dealer);
    }

    @Transactional(readOnly = true)
    public LoginResponse loginDealer(String msisdn, String plainPassword) {
        Dealer dealer = repository.findById(msisdn)
                .orElseThrow(() -> new DealerNotFoundException("Authentication failed: MSISDN not found."));
                
        if (!passwordEncoder.matches(plainPassword, dealer.getPassword())) {
            throw new InvalidPasswordException("Authentication failed: Invalid credentials.");
        }
        
        String token = jwtService.generateToken(dealer.getMsisdn(), dealer.getType().name());
        return new LoginResponse(token, dealer.getMsisdn(), dealer.getType().name());
    }

    public DealerResponse getDealerByMsisdn(String msisdn) {
        if (msisdn != null && !msisdn.matches("^\\d{10}$")) {
            throw new InvalidMsisdnException("MSISDN must be of length 10 and made of digits.");
        }
        Dealer dealer = repository.findById(msisdn)
                .orElseThrow(() -> new DealerNotFoundException("Dealer with this msisdn is not found."));
        return mapToResponse(dealer);
    }

    public List<DealerResponse> getDealers(String name, String typeRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        
        String currentRole = "ROLE_ANONYMOUS";
        if (authentication != null && authentication.isAuthenticated() 
                && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            currentRole = authentication.getAuthorities().iterator().next().getAuthority();
        }
        List<DealerType> allowedTypes = switch (currentRole) {
            case "ROLE_ANONYMOUS"   -> List.of(DealerType.RETAILER);
            case "ROLE_RETAILER"    -> List.of(DealerType.DISTRIBUTOR);
            case "ROLE_DISTRIBUTOR" -> List.of(DealerType.RETAILER, DealerType.FRANCHISE);
            case "ROLE_FRANCHISE"   -> List.of(DealerType.RETAILER, DealerType.DISTRIBUTOR);
            case "ROLE_ADMIN"       -> List.of(DealerType.RETAILER, DealerType.DISTRIBUTOR, DealerType.FRANCHISE);
            default                 -> List.of(); 
        };

        List<Dealer> result;

        if (typeRequest != null) {
            DealerType targetType;
            try {
                targetType = DealerType.valueOf(typeRequest.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidTypeException("Invalid dealer type requested.");
            }

            if (!allowedTypes.contains(targetType)) {
                return List.of(); 
            }

            if (name != null) {
                result = repository.findByNameAndType(name, targetType);
            } else {
                result = repository.findByType(targetType);
            }
        } else {
            if (name != null) {
                result = repository.findByNameAndTypeIn(name, allowedTypes);
            } else {
                result = repository.findByTypeIn(allowedTypes);
            }
        }

        return result.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public DealerResponse updateDealer(String msisdn, DealerUpdateRequest request) {
        if (msisdn == null || !msisdn.matches("^\\d{10}$")) {
            throw new InvalidMsisdnException("MSISDN must be of length 10 and contain digits only.");
        }
        if (request.getName() == null && request.getType() == null && request.getStatus() == null) {
            throw new EmptyUpdateRequestException("Enter atleast one of the following: \n NAME\nTYPE\nSTATUS");
        }
        Dealer dealer = repository.findById(msisdn)
                .orElseThrow(() -> new DealerNotFoundException("Dealer with this MSISDN does not exist."));
        if (!passwordEncoder.matches(request.getPassword(), dealer.getPassword())) {
            throw new InvalidPasswordException("Invalid Password.");
        }
        if (request.getName() != null) {
            dealer.setName(request.getName());
        }
        if (request.getType() != null) {
            try {
                dealer.setType(DealerType.valueOf(request.getType().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new InvalidTypeException("Invalid dealer type: " + request.getType() + ", The type has to be RETAILER or DISTRIBUTOR or FRANCHISE");
            }
        }
        if (request.getStatus() != null) {
            try {
                dealer.setStatus(DealerStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new InvalidStatusException("Invalid dealer status: " + request.getStatus() + ", The status has to be ACTIVE or INACTIVE.");
            }
        }
        repository.save(dealer);
        return mapToResponse(dealer);
    }

    private DealerResponse mapToResponse(Dealer dealer) {
        return new DealerResponse(
            dealer.getName(), dealer.getMsisdn(), dealer.getType(), dealer.getStatus(), dealer.getCreatedAt()
        );
    }
}