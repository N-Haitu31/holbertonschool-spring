package com.holberton.realestate.service;

import com.holberton.realestate.dto.PropertyMapper;
import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.dto.PropertyResponse;
import com.holberton.realestate.exception.DuplicateReferenceException;
import com.holberton.realestate.exception.OwnerNotFoundException;
import com.holberton.realestate.exception.PropertyNotFoundException;
import com.holberton.realestate.model.Owner;
import com.holberton.realestate.model.Property;
import com.holberton.realestate.repository.OwnerRepository;
import com.holberton.realestate.repository.PropertyRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private static final Sort BY_REFERENCE = Sort.by("reference");

    private final PropertyRepository repository;
    private final OwnerRepository ownerRepository;

    public PropertyService(PropertyRepository repository, OwnerRepository ownerRepository) {
        this.repository = repository;
        this.ownerRepository = ownerRepository;
    }

    public List<PropertyResponse> findAll() {
        return toResponses(repository.findAll(BY_REFERENCE));
    }

    public List<PropertyResponse> search(String city, Integer maxRent) {
        if (city != null && maxRent != null) {
            return toResponses(repository.findByAddressCityIgnoreCaseAndMonthlyRentLessThanEqual(
                    city, maxRent, BY_REFERENCE));
        }
        if (city != null) {
            return toResponses(repository.findByAddressCityIgnoreCase(city, BY_REFERENCE));
        }
        if (maxRent != null) {
            return toResponses(repository.findByMonthlyRentLessThanEqual(maxRent, BY_REFERENCE));
        }
        return findAll();
    }

    public PropertyResponse findByReference(String reference) {
        return repository.findByReference(reference)
                .map(PropertyMapper::toResponse)
                .orElseThrow(() -> new PropertyNotFoundException(reference));
    }

    public PropertyResponse create(PropertyRequest request) {
        if (repository.existsByReference(request.reference())) {
            throw new DuplicateReferenceException(request.reference());
        }
        Property saved = repository.save(PropertyMapper.toEntity(request));
        return PropertyMapper.toResponse(saved);
    }

    // la référence du corps est ignorée
    public PropertyResponse update(String reference, PropertyRequest request) {
        Property property = repository.findByReference(reference)
                .orElseThrow(() -> new PropertyNotFoundException(reference));
        property.setAddress(request.address());
        property.setSurface(request.surface());
        property.setMonthlyRent(request.monthlyRent());
        property.setEnergyClass(request.energyClass());
        return PropertyMapper.toResponse(repository.save(property));
    }

    public void delete(String reference) {
        Property property = repository.findByReference(reference)
                .orElseThrow(() -> new PropertyNotFoundException(reference));
        repository.delete(property);
    }

    public PropertyResponse assignOwner(String reference, Long ownerId) {
        Property property = repository.findByReference(reference)
                .orElseThrow(() -> new PropertyNotFoundException(reference));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(ownerId));
        property.setOwner(owner);
        return PropertyMapper.toResponse(repository.save(property));
    }

    private List<PropertyResponse> toResponses(List<Property> properties) {
        return properties.stream().map(PropertyMapper::toResponse).toList();
    }
}
