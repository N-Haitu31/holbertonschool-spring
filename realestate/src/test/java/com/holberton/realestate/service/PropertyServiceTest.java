package com.holberton.realestate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.dto.PropertyResponse;
import com.holberton.realestate.exception.DuplicateReferenceException;
import com.holberton.realestate.exception.PropertyNotFoundException;
import com.holberton.realestate.model.Address;
import com.holberton.realestate.model.EnergyClass;
import com.holberton.realestate.model.Property;
import com.holberton.realestate.repository.LeaseRepository;
import com.holberton.realestate.repository.OwnerRepository;
import com.holberton.realestate.repository.PropertyRepository;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository repository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private LeaseRepository leaseRepository;

    private PropertyService service;

    private final Address address = new Address("1 rue de Rivoli", "75001", "Paris");

    @BeforeEach
    void setUp() {
        service = new PropertyService(repository, ownerRepository, leaseRepository);
    }

    @Test
    @DisplayName("an unknown reference throws PropertyNotFoundException")
    void unknownReferenceThrowsNotFound() {
        when(repository.findByReference("P-999")).thenReturn(Optional.empty());

        assertThrows(PropertyNotFoundException.class,
                () -> service.findByReference("P-999"));
    }

    @Test
    @DisplayName("a duplicate reference throws DuplicateReferenceException and saves nothing")
    void duplicateReferenceThrowsAndSavesNothing() {
        when(repository.existsByReference("P-001")).thenReturn(true);
        PropertyRequest request = new PropertyRequest("P-001", address, 25, 950, EnergyClass.D);

        assertThrows(DuplicateReferenceException.class, () -> service.create(request));

        verify(repository, never()).save(any(Property.class));
    }

    @Test
    @DisplayName("a valid creation calls save")
    void validCreationCallsSave() {
        when(repository.existsByReference("P-001")).thenReturn(false);
        when(repository.save(any(Property.class))).thenAnswer(call -> call.getArgument(0));
        PropertyRequest request = new PropertyRequest("P-001", address, 25, 950, EnergyClass.D);

        PropertyResponse response = service.create(request);

        verify(repository).save(any(Property.class));
        assertEquals("P-001", response.reference());
    }

    @Test
    @DisplayName("the annual rent is twelve times the monthly rent")
    void annualRentIsTwelveMonths() {
        Property property = new Property("P-001", address, 25, 950, EnergyClass.D);
        when(repository.findByReference("P-001")).thenReturn(Optional.of(property));

        PropertyResponse response = service.findByReference("P-001");

        assertEquals(11400, response.annualRent());
    }
}
