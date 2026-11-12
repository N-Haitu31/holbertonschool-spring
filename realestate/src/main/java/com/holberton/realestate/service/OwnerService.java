package com.holberton.realestate.service;

import com.holberton.realestate.dto.OwnerRequest;
import com.holberton.realestate.dto.OwnerResponse;
import com.holberton.realestate.dto.PropertyMapper;
import com.holberton.realestate.dto.PropertyResponse;
import com.holberton.realestate.exception.DuplicateEmailException;
import com.holberton.realestate.exception.OwnerHasPropertiesException;
import com.holberton.realestate.exception.OwnerNotFoundException;
import com.holberton.realestate.model.Owner;
import com.holberton.realestate.repository.OwnerRepository;
import com.holberton.realestate.repository.PropertyRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    private static final Sort BY_NAME = Sort.by("name");

    private final OwnerRepository repository;
    private final PropertyRepository propertyRepository;

    public OwnerService(OwnerRepository repository, PropertyRepository propertyRepository) {
        this.repository = repository;
        this.propertyRepository = propertyRepository;
    }

    public OwnerResponse create(OwnerRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }
        Owner saved = repository.save(new Owner(request.name(), request.email()));
        return toResponse(saved);
    }

    public List<OwnerResponse> findAll() {
        return repository.findAll(BY_NAME).stream()
                .map(OwnerService::toResponse)
                .toList();
    }

    public OwnerResponse findById(Long id) {
        return repository.findById(id)
                .map(OwnerService::toResponse)
                .orElseThrow(() -> new OwnerNotFoundException(id));
    }

    public List<PropertyResponse> findProperties(Long ownerId) {
        if (!repository.existsById(ownerId)) {
            throw new OwnerNotFoundException(ownerId);
        }
        return propertyRepository.findByOwnerIdOrderByReference(ownerId).stream()
                .map(PropertyMapper::toResponse)
                .toList();
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new OwnerNotFoundException(id);
        }
        if (propertyRepository.existsByOwnerId(id)) {
            throw new OwnerHasPropertiesException(id);
        }
        repository.deleteById(id);
    }

    private static OwnerResponse toResponse(Owner owner) {
        return new OwnerResponse(owner.getId(), owner.getName(), owner.getEmail());
    }
}
