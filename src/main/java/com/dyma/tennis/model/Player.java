package com.dyma.tennis.model;

import jakarta.validation.Valid;

import java.util.Set;

public record Player(
        @Valid PlayerDescription info,
        @Valid Set<TournamentDescription> tournaments
) {
}
