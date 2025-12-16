package com.dyma.tennis.web;

import com.dyma.tennis.model.Player;
import com.dyma.tennis.model.PlayerToCreate;
import com.dyma.tennis.model.PlayerToUpdate;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
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
@AutoConfigureTestRestTemplate
public class PlayerControllerEndToEndTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void shouldCreatePlayer() {
        // Given
        PlayerToCreate playerToCreate = new PlayerToCreate(
                "Carlos",
                "Alcaraz",
                LocalDate.of(2003, Month.MAY, 5),
                4500
        );

        // When
        ResponseEntity<Player> playerResponseEntity = restTemplate.postForEntity(
                "/players",
                playerToCreate,
                Player.class
        );

        // Then
        Assertions.assertThat(playerResponseEntity.getBody().info().lastName()).isEqualTo("Alcaraz");
        Assertions.assertThat(playerResponseEntity.getBody().info().rank().position()).isEqualTo(2);
    }

    @Test
    public void shouldFailToCreatePlayer_WhenPlayerToCreateIsInvalid() {
        // Given
        PlayerToCreate playerToCreate = new PlayerToCreate(
                "Carlos",
                null,
                LocalDate.of(2003, Month.MAY, 5),
                4500
        );

        // When
        ResponseEntity<Player> playerResponseEntity = restTemplate.postForEntity(
                "/players",
                playerToCreate,
                Player.class
        );

        // Then
        Assertions.assertThat(playerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldUpdatePlayerRanking() {
        // Given
        PlayerToUpdate playerToUpdate = new PlayerToUpdate(
                UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e"),
                "Rafael",
                "NadalTest",
                LocalDate.of(1986, Month.JUNE, 3),
                1000
        );

        // When
        HttpEntity<PlayerToUpdate> request = new HttpEntity<>(playerToUpdate);
        ResponseEntity<Player> playerResponseEntity = restTemplate.exchange(
                "/players",
                HttpMethod.PUT,
                request,
                Player.class
        );

        // Then
        Assertions.assertThat(playerResponseEntity.getBody().info().lastName()).isEqualTo("NadalTest");
        Assertions.assertThat(playerResponseEntity.getBody().info().rank().position()).isEqualTo(3);
    }

    @Test
    public void shouldDeletePlayer() {
        // Given / When
        restTemplate.delete("/players/d27aef45-51cd-401b-a04a-b78a1327b793");

        ResponseEntity<List<Player>> allPlayersResponseEntity = restTemplate.exchange(
                "/players",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Player>>() {
                }
        );

        // Then
        Assertions.assertThat(allPlayersResponseEntity.getBody())
                .extracting("info.lastName", "info.rank.position")
                .containsExactly(
                        Tuple.tuple("NadalTest", 1),
                        Tuple.tuple("FedererTest", 2)
                );
    }
}
