package com.dealer.drproj.controller;

import java.util.*;
import com.dealer.drproj.dto.*;
import com.dealer.drproj.service.DealerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/dealers")
public class DealerController {
    private final DealerService service;

    public DealerController(DealerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DealerResponse> createDealer(@Valid @RequestBody DealerRequest request) {
        DealerResponse Response = service.createDealer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String msisdn, @RequestParam String password) {
        return ResponseEntity.ok(service.loginDealer(msisdn, password));
    }

    @GetMapping
    public ResponseEntity<List<DealerResponse>> getDealers(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(service.getDealers(name, type));
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<DealerResponse> getDealerByMsisdn(@PathVariable String msisdn) {
        return ResponseEntity.ok(service.getDealerByMsisdn(msisdn));
    }

    @PatchMapping("/update/{msisdn}")
    public ResponseEntity<DealerResponse> updateDealerStatus(
            @PathVariable String msisdn,
            @Valid @RequestBody DealerUpdateRequest request) {
        return ResponseEntity.ok(service.updateDealer(msisdn, request));
    }
}