package com.dyma.tennis.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public record PlayerDescription(
        @NotNull(message = "Identifier is mandatory") UUID identifier,
        @NotBlank(message = "First name is mandatory") String firstName,
        @NotBlank(message = "Last name is mandatory") String lastName,
        @NotNull(message = "Birth date is mandatory") @PastOrPresent(message = "Birth date must be past or present") LocalDate birthDate,
        @Valid Rank rank
) {
}
