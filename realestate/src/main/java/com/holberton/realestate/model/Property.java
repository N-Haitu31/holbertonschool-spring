package com.holberton.realestate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private int surface;

    @Column(nullable = false)
    private int monthlyRent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnergyClass energyClass;

    protected Property() {
        // utilisé par Hibernate pour reconstruire les objets lus en base
    }

    public Property(String reference, Address address, int surface,
                    int monthlyRent, EnergyClass energyClass) {
        this.reference = reference;
        this.address = address;
        this.surface = surface;
        this.monthlyRent = monthlyRent;
        this.energyClass = energyClass;
    }

    public String getReference() { return reference; }
    public Address getAddress() { return address; }
    public int getSurface() { return surface; }
    public int getMonthlyRent() { return monthlyRent; }
    public EnergyClass getEnergyClass() { return energyClass; }

    public void setAddress(Address address) { this.address = address; }
    public void setSurface(int surface) { this.surface = surface; }
    public void setMonthlyRent(int monthlyRent) { this.monthlyRent = monthlyRent; }
    public void setEnergyClass(EnergyClass energyClass) { this.energyClass = energyClass; }
}
