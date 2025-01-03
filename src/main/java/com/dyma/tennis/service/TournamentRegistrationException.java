package com.dyma.tennis.service;

public class TournamentRegistrationException extends RuntimeException {
    public TournamentRegistrationException(String reason) {
        super(reason);
    }
}
