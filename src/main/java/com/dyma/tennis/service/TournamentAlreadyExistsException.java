package com.dyma.tennis.service;

public class TournamentAlreadyExistsException extends RuntimeException {
    public TournamentAlreadyExistsException(String name) {
        super("Tournament with name " + name + " already exists.");
    }
}
