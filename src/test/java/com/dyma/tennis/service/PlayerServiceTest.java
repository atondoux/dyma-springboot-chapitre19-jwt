package com.dyma.tennis.service;

import com.dyma.tennis.data.PlayerEntityList;
import com.dyma.tennis.data.PlayerRepository;
import com.dyma.tennis.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    private PlayerMapper playerMapper = new PlayerMapper();

    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService(playerRepository, playerMapper);
    }

    @Test
    public void shouldReturnPlayersRanking() {
        // Given
        Mockito.when(playerRepository.findAll()).thenReturn(PlayerEntityList.ALL);

        // When
        List<Player> allPlayers = playerService.getAllPlayers();

        // Then
        Assertions.assertThat(allPlayers)
                .extracting("info.lastName")
                .containsExactly("Nadal", "Djokovic", "Federer", "Murray");
    }

    @Test
    public void shouldFailToReturnPlayersRanking_WhenDataAccessExceptionOccurs() {
        // Given
        Mockito.when(playerRepository.findAll()).thenThrow(new DataRetrievalFailureException("Data access error"));

        // When / Then
        Assertions.assertThatThrownBy(() -> playerService.getAllPlayers())
                .isInstanceOf(PlayerDataRetrievalException.class)
                .hasMessage("Could not retrieve player data");
    }

    @Test
    public void shouldRetrievePlayer() {
        // Given
        UUID playerToRetrieve = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");
        Mockito.when(playerRepository.findOneByIdentifier(playerToRetrieve)).thenReturn(Optional.of(PlayerEntityList.RAFAEL_NADAL));

        // When
        Player retrievedPlayer = playerService.getByIdentifier(playerToRetrieve);

        // Then
        Assertions.assertThat(retrievedPlayer.info().lastName()).isEqualTo("Nadal");
    }

    @Test
    public void shouldFailToRetrievePlayer_WhenPlayerDoesNotExist() {
        // Given
        UUID unknownPlayer = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");
        Mockito.when(playerRepository.findOneByIdentifier(unknownPlayer)).thenReturn(Optional.empty());

        // When / Then
        Assertions.assertThatThrownBy(() -> playerService.getByIdentifier(unknownPlayer))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage("Player with identifier aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb could not be found.");
    }
}
