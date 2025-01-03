package com.dyma.tennis.model;

import jakarta.validation.Valid;

import java.util.Set;

public record Tournament(
        @Valid TournamentDescription info,
        @Valid Set<PlayerDescription> players
) {
}
