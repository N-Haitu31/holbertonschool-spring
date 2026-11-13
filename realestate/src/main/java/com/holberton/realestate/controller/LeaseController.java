package com.holberton.realestate.controller;

import com.holberton.realestate.dto.LeaseRequest;
import com.holberton.realestate.dto.LeaseResponse;
import com.holberton.realestate.service.LeaseService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leases")
public class LeaseController {
    private final LeaseService service;

    public LeaseController(LeaseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LeaseResponse> create(@Valid @RequestBody LeaseRequest request) {
        LeaseResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/leases/" + created.id()))
                .body(created);
    }

    @GetMapping
    public List<LeaseResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LeaseResponse get(@PathVariable Long id) {
        return service.findById(id);
    }
}
