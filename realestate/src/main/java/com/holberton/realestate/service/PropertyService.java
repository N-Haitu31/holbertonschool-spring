package com.holberton.realestate.service;

import com.holberton.realestate.dto.PropertyRequest;
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

    public List<Property> findAll() {
        return repository.findAll(BY_REFERENCE);
    }

    // c'est le service qui choisit la méthode du repository selon les paramètres reçus
    public List<Property> search(String city, Integer maxRent) {
        if (city != null && maxRent != null) {
            return repository.findByAddressCityIgnoreCaseAndMonthlyRentLessThanEqual(
                    city, maxRent, BY_REFERENCE);
        }
        if (city != null) {
            return repository.findByAddressCityIgnoreCase(city, BY_REFERENCE);
        }
        if (maxRent != null) {
            return repository.findByMonthlyRentLessThanEqual(maxRent, BY_REFERENCE);
        }
        return findAll();
    }

    public Optional<Property> findByReference(String reference) {
        return repository.findByReference(reference);
    }

    // Optional vide = la référence existe déjà
    public Optional<Property> create(PropertyRequest request) {
        if (repository.existsByReference(request.reference())) {
            return Optional.empty();
        }
        Property property = new Property(request.reference(), request.address(),
                request.surface(), request.monthlyRent(), request.energyClass());
        return Optional.of(repository.save(property));
    }

    // Optional vide = référence inconnue ; la référence du corps est ignorée
    public Optional<Property> update(String reference, PropertyRequest request) {
        return repository.findByReference(reference).map(p -> {
            p.setAddress(request.address());
            p.setSurface(request.surface());
            p.setMonthlyRent(request.monthlyRent());
            p.setEnergyClass(request.energyClass());
            return repository.save(p);
        });
    }

    // false = référence inconnue
    public boolean delete(String reference) {
        Optional<Property> found = repository.findByReference(reference);
        found.ifPresent(repository::delete);
        return found.isPresent();
    }
}
