package com.holberton.realestate.dto;

import com.holberton.realestate.service.LeaseRules;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaseRequest(
        @NotBlank String propertyReference,
        @NotNull Long tenantId,
        @Min(LeaseRules.MIN_DURATION_MONTHS) @Max(LeaseRules.MAX_DURATION_MONTHS)
        int durationMonths) {}
