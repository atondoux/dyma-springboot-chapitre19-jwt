package com.dyma.tennis.service;

import java.util.UUID;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(UUID identifier) {
        super("Tournament with identifier " + identifier + " could not be found.");
    }
}
