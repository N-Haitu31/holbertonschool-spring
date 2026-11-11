package com.holberton.realestate.controller;

import com.holberton.realestate.dto.PropertyRequest;
import com.holberton.realestate.model.Property;
import com.holberton.realestate.service.PropertyService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyService service;

    public PropertyController(PropertyService service) {
        this.service = service;
    }

    @GetMapping
    public List<Property> list() {
        return service.findAll();
    }

    @GetMapping("/{reference}")
    public ResponseEntity<Property> get(@PathVariable String reference) {
        return service.findByReference(reference)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Property> create(@RequestBody PropertyRequest request) {
        return service.create(request)
                .map(p -> ResponseEntity
                        .created(URI.create("/api/properties/" + p.getReference()))
                        .body(p))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PutMapping("/{reference}")
    public ResponseEntity<Property> update(@PathVariable String reference,
                                           @RequestBody PropertyRequest request) {
        return service.update(reference, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reference}")
    public ResponseEntity<Void> delete(@PathVariable String reference) {
        if (service.delete(reference)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
