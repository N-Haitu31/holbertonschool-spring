package com.holberton.realestate.dto;

import com.holberton.realestate.model.Property;

public final class PropertyMapper {

    private PropertyMapper() {
        // classe utilitaire : on ne l'instancie pas
    }

    public static PropertyResponse toResponse(Property property) {
        Long ownerId = property.getOwner() == null ? null : property.getOwner().getId();
        return new PropertyResponse(
                property.getReference(),
                property.getAddress(),
                property.getSurface(),
                property.getMonthlyRent(),
                property.getEnergyClass(),
                property.getMonthlyRent() * 12,
                ownerId,
                property.isRented());
    }

    public static Property toEntity(PropertyRequest request) {
        return new Property(
                request.reference(),
                request.address(),
                request.surface(),
                request.monthlyRent(),
                request.energyClass());
    }
}
