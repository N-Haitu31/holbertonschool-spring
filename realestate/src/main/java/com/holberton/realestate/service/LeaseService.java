package com.holberton.realestate.service;

import com.holberton.realestate.dto.LeaseRequest;
import com.holberton.realestate.dto.LeaseResponse;
import com.holberton.realestate.exception.InsufficientIncomeException;
import com.holberton.realestate.exception.LeaseNotFoundException;
import com.holberton.realestate.exception.PropertyNotFoundException;
import com.holberton.realestate.exception.PropertyUnavailableException;
import com.holberton.realestate.exception.TenantNotFoundException;
import com.holberton.realestate.model.Lease;
import com.holberton.realestate.model.Property;
import com.holberton.realestate.model.Tenant;
import com.holberton.realestate.repository.LeaseRepository;
import com.holberton.realestate.repository.PropertyRepository;
import com.holberton.realestate.repository.TenantRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaseService {
    private static final Sort BY_ID = Sort.by("id");

    private final LeaseRepository repository;
    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;

    public LeaseService(LeaseRepository repository,
                        PropertyRepository propertyRepository,
                        TenantRepository tenantRepository) {
        this.repository = repository;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public LeaseResponse create(LeaseRequest request) {
        // règle 2 : la propriété existe
        Property property = propertyRepository.findByReference(request.propertyReference())
                .orElseThrow(() -> new PropertyNotFoundException(request.propertyReference()));

        // règle 3 : le locataire existe
        Tenant tenant = tenantRepository.findById(request.tenantId())
                .orElseThrow(() -> new TenantNotFoundException(request.tenantId()));

        // règle 4 : la propriété n'est pas déjà louée
        if (repository.existsByPropertyAndActiveTrue(property)) {
            throw new PropertyUnavailableException(property.getReference());
        }

        // règle 5 : le revenu est suffisant
        int requiredIncome = LeaseRules.INCOME_MULTIPLIER * property.getMonthlyRent();
        if (tenant.getMonthlyIncome() < requiredIncome) {
            throw new InsufficientIncomeException(tenant.getMonthlyIncome(), requiredIncome);
        }

        // règle 6 : tout est bon, on enregistre (le loyer est copié dans le bail)
        Lease saved = repository.save(new Lease(
                property, tenant, request.durationMonths(), property.getMonthlyRent()));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LeaseResponse> findAll() {
        return repository.findAll(BY_ID).stream()
                .map(LeaseService::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LeaseResponse findById(Long id) {
        return repository.findById(id)
                .map(LeaseService::toResponse)
                .orElseThrow(() -> new LeaseNotFoundException(id));
    }

    private static LeaseResponse toResponse(Lease lease) {
        int rent = lease.getMonthlyRent();
        return new LeaseResponse(
                lease.getId(),
                lease.getProperty().getReference(),
                lease.getTenant().getId(),
                lease.getDurationMonths(),
                rent,
                rent * lease.getDurationMonths(),
                rent * LeaseRules.DEPOSIT_MONTHS,
                lease.isActive());
    }
}
