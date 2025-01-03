package com.dyma.tennis.data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TournamentEntityList {

    public static TournamentEntity AUSTRALIAN_OPEN = new TournamentEntity(
            UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"),
            "Australian Open",
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(14),
            1000000,
            32
    );

    public static TournamentEntity FRENCH_OPEN = new TournamentEntity(
            UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"),
            "French Open",
            LocalDate.now().plusDays(15),
            LocalDate.now().plusDays(29),
            2000000,
            32
    );

    public static TournamentEntity WIMBLEDON = new TournamentEntity(
            UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13"),
            "Wimbledon",
            LocalDate.now().plusDays(30),
            LocalDate.now().plusDays(44),
            3000000,
            32
    );

    public static TournamentEntity US_OPEN = new TournamentEntity(
            UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14"),
            "US Open",
            LocalDate.now().plusDays(45),
            LocalDate.now().plusDays(59),
            4000000,
            32
    );

    public static List<TournamentEntity> ALL = Arrays.asList(AUSTRALIAN_OPEN, FRENCH_OPEN, WIMBLEDON, US_OPEN);
}
