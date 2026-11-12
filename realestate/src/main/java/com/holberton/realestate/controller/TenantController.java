package com.holberton.realestate.controller;

import com.holberton.realestate.dto.TenantRequest;
import com.holberton.realestate.dto.TenantResponse;
import com.holberton.realestate.service.TenantService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    private final TenantService service;

    public TenantController(TenantService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody TenantRequest request) {
        TenantResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/tenants/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public TenantResponse get(@PathVariable Long id) {
        return service.findById(id);
    }
}
