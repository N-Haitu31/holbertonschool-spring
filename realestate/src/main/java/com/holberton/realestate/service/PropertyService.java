package com.holberton.realestate.service;

import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.model.Address;
import com.holberton.realestate.model.EnergyClass;
import com.holberton.realestate.model.Property;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private final List<Property> properties = new ArrayList<>();

    public PropertyService() {
        properties.add(new Property("P-001",
                new Address("12 Rue de la Paix", "75002", "Paris"), 25, 950, EnergyClass.D));
        properties.add(new Property("P-002",
                new Address("8 Chemin des Vignes", "33000", "Bordeaux"), 120, 1800, EnergyClass.B));
        properties.add(new Property("P-003",
                new Address("1 Place Bellecour", "69002", "Lyon"), 80, 2500, EnergyClass.C));
    }

    public List<Property> findAll() {
        return properties.stream()
                .sorted(Comparator.comparing(Property::getReference))
                .toList();
    }

    public Optional<Property> findByReference(String reference) {
        return properties.stream()
                .filter(p -> p.getReference().equals(reference))
                .findFirst();
    }

    // Optional vide = la référence existe déjà
    public Optional<Property> create(PropertyRequest request) {
        if (findByReference(request.reference()).isPresent()) {
            return Optional.empty();
        }
        Property property = new Property(request.reference(), request.address(),
                request.surface(), request.monthlyRent(), request.energyClass());
        properties.add(property);
        return Optional.of(property);
    }

    // Optional vide = référence inconnue ; la référence du corps est ignorée
    public Optional<Property> update(String reference, PropertyRequest request) {
        Optional<Property> found = findByReference(reference);
        found.ifPresent(p -> {
            p.setAddress(request.address());
            p.setSurface(request.surface());
            p.setMonthlyRent(request.monthlyRent());
            p.setEnergyClass(request.energyClass());
        });
        return found;
    }

    // false = référence inconnue
    public boolean delete(String reference) {
        return properties.removeIf(p -> p.getReference().equals(reference));
    }
}
