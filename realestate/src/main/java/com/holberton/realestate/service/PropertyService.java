package com.holberton.realestate.service;

import com.holberton.realestate.dto.PropertyMapper;
import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.dto.PropertyResponse;
import com.holberton.realestate.model.Property;
import com.holberton.realestate.repository.PropertyRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private static final Sort BY_REFERENCE = Sort.by("reference");

    private final PropertyRepository repository;

    public PropertyService(PropertyRepository repository) {
        this.repository = repository;
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

    public Optional<PropertyResponse> findByReference(String reference) {
        return repository.findByReference(reference).map(PropertyMapper::toResponse);
    }

    // Optional vide = la référence existe déjà
    public Optional<PropertyResponse> create(PropertyRequest request) {
        if (repository.existsByReference(request.reference())) {
            return Optional.empty();
        }
        Property saved = repository.save(PropertyMapper.toEntity(request));
        return Optional.of(PropertyMapper.toResponse(saved));
    }

    // Optional vide = référence inconnue ; la référence du corps est ignorée
    public Optional<PropertyResponse> update(String reference, PropertyRequest request) {
        return repository.findByReference(reference).map(p -> {
            p.setAddress(request.address());
            p.setSurface(request.surface());
            p.setMonthlyRent(request.monthlyRent());
            p.setEnergyClass(request.energyClass());
            return PropertyMapper.toResponse(repository.save(p));
        });
    }

    // false = référence inconnue
    public boolean delete(String reference) {
        Optional<Property> found = repository.findByReference(reference);
        found.ifPresent(repository::delete);
        return found.isPresent();
    }

    private List<PropertyResponse> toResponses(List<Property> properties) {
        return properties.stream().map(PropertyMapper::toResponse).toList();
    }
}
