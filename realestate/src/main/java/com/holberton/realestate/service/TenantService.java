package com.holberton.realestate.service;

import com.holberton.realestate.dto.TenantRequest;
import com.holberton.realestate.dto.TenantResponse;
import com.holberton.realestate.exception.DuplicateTenantEmailException;
import com.holberton.realestate.exception.TenantNotFoundException;
import com.holberton.realestate.model.Tenant;
import com.holberton.realestate.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    private final TenantRepository repository;

    public TenantService(TenantRepository repository) {
        this.repository = repository;
    }

    public TenantResponse create(TenantRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateTenantEmailException(request.email());
        }
        Tenant saved = repository.save(
                new Tenant(request.name(), request.email(), request.monthlyIncome()));
        return toResponse(saved);
    }

    public TenantResponse findById(Long id) {
        return repository.findById(id)
                .map(TenantService::toResponse)
                .orElseThrow(() -> new TenantNotFoundException(id));
    }

    private static TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(), tenant.getName(), tenant.getEmail(), tenant.getMonthlyIncome());
    }
}
