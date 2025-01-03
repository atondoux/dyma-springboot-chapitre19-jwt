package com.dyma.tennis.web;

import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentToCreate;
import com.dyma.tennis.model.TournamentToUpdate;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TournamentControllerEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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
        String url = "http://localhost:" + port + "/tournaments";
        HttpEntity<TournamentToCreate> request = new HttpEntity<>(tournamentToCreate);
        ResponseEntity<Tournament> tournamentResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request, Tournament.class);

        // Then
        Assertions.assertThat(tournamentResponseEntity.getBody().info().name()).isEqualTo("Madrid Master 1000");
    }

    @Test
    public void shouldFailToCreateTournament_WhenTournamentToCreateIsInvalid() {
        // Given
        TournamentToCreate tournamentToCreate = new TournamentToCreate(
                null,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(17),
                500000,
                64
        );

        // When
        String url = "http://localhost:" + port + "/tournaments";
        HttpEntity<TournamentToCreate> request = new HttpEntity<>(tournamentToCreate);
        ResponseEntity<Tournament> tournamentResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request, Tournament.class);

        // Then
        Assertions.assertThat(tournamentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldUpdateTournament() {
        // Given
        TournamentToUpdate tournamentToUpdate = new TournamentToUpdate(
                UUID.fromString("d4a9f8e2-9051-4739-90bc-1cb7e4c7ad42"),
                "Roland Garros",
                LocalDate.of(2025, Month.MAY, 26),
                LocalDate.of(2025, Month.JUNE, 9),
                2500000,
                128
        );

        // When
        String url = "http://localhost:" + port + "/tournaments";
        HttpEntity<TournamentToUpdate> request = new HttpEntity<>(tournamentToUpdate);
        ResponseEntity<Tournament> tournamentResponseEntity = this.restTemplate.exchange(url, HttpMethod.PUT, request, Tournament.class);

        // Then
        Assertions.assertThat(tournamentResponseEntity.getBody().info().name()).isEqualTo("Roland Garros");
    }

    @Test
    public void shouldDeleteTournament() {
        // Given / When
        String url = "http://localhost:" + port + "/tournaments";
        this.restTemplate.exchange(url + "/124edf07-64fa-4ea4-a65e-3bfe96df5781", HttpMethod.DELETE, null, Tournament.class);
        HttpEntity<List<Tournament>> allTournamentsResponseEntity = this.restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tournament>>() {
                }
        );

        // Then
        Assertions.assertThat(allTournamentsResponseEntity.getBody())
                .extracting("info.name")
                .containsExactly("Australian Open", "French Open", "Wimbledon");
    }
}
