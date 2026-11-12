package com.holberton.realestate.controller;

import com.holberton.realestate.dto.OwnerAssignmentRequest;
import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.dto.PropertyResponse;
import com.holberton.realestate.service.PropertyService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyService service;

    public PropertyController(PropertyService service) {
        this.service = service;
    }

    @GetMapping
    public List<PropertyResponse> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer maxRent) {
        return service.search(city, maxRent);
    }

    @GetMapping("/{reference}")
    public PropertyResponse get(@PathVariable String reference) {
        return service.findByReference(reference);
    }

    @PostMapping
    public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest request) {
        PropertyResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/properties/" + created.reference()))
                .body(created);
    }

    @PutMapping("/{reference}")
    public PropertyResponse update(@PathVariable String reference,
                                   @Valid @RequestBody PropertyRequest request) {
        return service.update(reference, request);
    }

    @DeleteMapping("/{reference}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String reference) {
        service.delete(reference);
    }

    @PutMapping("/{reference}/owner")
    public PropertyResponse assignOwner(@PathVariable String reference,
                                        @Valid @RequestBody OwnerAssignmentRequest request) {
        return service.assignOwner(reference, request.ownerId());
    }
}
