package com.dealer.drproj.controller;
import java.util.*;
import com.dealer.drproj.dto.*;
import com.dealer.drproj.service.DealerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/dealers")
@Validated
public class DealerController {
    private final DealerService service;
    public DealerController(DealerService service){
        this.service=service;
    }

    @PostMapping
    public ResponseEntity<DealerResponse> createDealer(@Valid @RequestBody DealerRequest request){
        DealerResponse Response=service.createDealer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response);
    }

    @GetMapping
    public ResponseEntity<List<DealerResponse>> getDealers(@RequestParam(required = false) String type){
        if(type!=null){
            return ResponseEntity.ok(service.getDealerByType(type));
        }
        return ResponseEntity.ok(service.getDealers());
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<DealerResponse> getDealerByMsisdn(@PathVariable String msisdn){
        return ResponseEntity.ok(service.getDealerByMsisdn(msisdn));
    }

    @PatchMapping("/{msisdn}/{status}")
    public ResponseEntity<DealerResponse>  updateDealerStatus(@PathVariable String msisdn, @PathVariable String status, @RequestParam String password){
        return ResponseEntity.ok(service.updateDealerStatus(msisdn, status, password));
    }
}
