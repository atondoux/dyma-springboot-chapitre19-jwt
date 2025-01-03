package com.dyma.tennis.service;

import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentToCreate;
import com.dyma.tennis.model.TournamentToUpdate;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TournamentServiceIntegrationTest {

    @Autowired
    private TournamentService tournamentService;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void shouldCreateTournament() {
        // Given
        TournamentToCreate tournamentToCreate = new TournamentToCreate(
                "Madrid Master 1000",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );

        // When
        Tournament savedTournament = tournamentService.create(tournamentToCreate);
        Tournament createdTournament = tournamentService.getByIdentifier(savedTournament.info().identifier());

        // Then
        Assertions.assertThat(createdTournament.info().name()).isEqualTo("Madrid Master 1000");
    }

    @Test
    public void shouldFailToCreateAnExistingTournament() {
        // Given
        TournamentToCreate tournamentToCreate = new TournamentToCreate(
                "Madrid Master 1000",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );
        tournamentService.create(tournamentToCreate);
        TournamentToCreate duplicatedTournamentToCreate = new TournamentToCreate(
                "Madrid Master 1000",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );

        // When / Then
        Exception exception = assertThrows(TournamentAlreadyExistsException.class, () -> {
            tournamentService.create(duplicatedTournamentToCreate);
        });
        Assertions.assertThat(exception.getMessage()).contains("Tournament with name Madrid Master 1000 already exists.");
    }

    @Test
    public void shouldUpdateTournament() {
        // Given
        UUID frenchOpenIdentifier = UUID.fromString("d4a9f8e2-9051-4739-90bc-1cb7e4c7ad42");
        TournamentToUpdate tournamentToUpdate = new TournamentToUpdate(
                frenchOpenIdentifier,
                "Roland Garros",
                LocalDate.of(2025, Month.MAY, 26),
                LocalDate.of(2025, Month.JUNE, 9),
                2500000,
                128
        );

        // When
        tournamentService.update(tournamentToUpdate);
        Tournament updatedTournament = tournamentService.getByIdentifier(frenchOpenIdentifier);

        // Then
        Assertions.assertThat(updatedTournament.info().name()).isEqualTo("Roland Garros");
    }

    @Test
    public void shouldDeleteTournament() {
        // Given
        UUID tournamentToDelete = UUID.fromString("124edf07-64fa-4ea4-a65e-3bfe96df5781");

        // When
        tournamentService.delete(tournamentToDelete);

        // Then
        List<Tournament> allTournaments = tournamentService.getAllTournaments();
        Assertions.assertThat(allTournaments)
                .extracting("info.name")
                .containsExactly("Australian Open", "French Open", "Wimbledon");
    }

    @Test
    public void shouldFailToDeleteTournament_WhenTournamentDoesNotExist() {
        // Given
        UUID tournamentToDelete = UUID.fromString("5f8c9b43-8d74-49e8-b821-f43d57e4a9b7");

        // When / Then
        Exception exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentService.delete(tournamentToDelete);
        });
        Assertions.assertThat(exception.getMessage()).isEqualTo("Tournament with identifier 5f8c9b43-8d74-49e8-b821-f43d57e4a9b7 could not be found.");
    }
}
