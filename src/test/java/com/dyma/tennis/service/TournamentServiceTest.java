package com.dyma.tennis.service;

import com.dyma.tennis.data.TournamentEntityList;
import com.dyma.tennis.data.TournamentRepository;
import com.dyma.tennis.model.Tournament;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    private TournamentMapper tournamentMapper = new TournamentMapper();

    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        tournamentService = new TournamentService(tournamentRepository, tournamentMapper);
    }

    @Test
    public void shouldReturnAllTournaments() {
        // Given
        Mockito.when(tournamentRepository.findAll()).thenReturn(TournamentEntityList.ALL);

        // When
        List<Tournament> allTournaments = tournamentService.getAllTournaments();

        // Then
        Assertions.assertThat(allTournaments)
                .extracting("info.name")
                .containsExactly("Australian Open", "French Open", "Wimbledon", "US Open");
    }

    @Test
    public void shouldRetrieveTournament() {
        // Given
        UUID tournamentToRetrieve = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
        Mockito.when(tournamentRepository.findOneByIdentifier(tournamentToRetrieve)).thenReturn(Optional.of(TournamentEntityList.FRENCH_OPEN));

        // When
        Tournament retrievedTournament = tournamentService.getByIdentifier(tournamentToRetrieve);

        // Then
        Assertions.assertThat(retrievedTournament.info().name()).isEqualTo("French Open");
    }
}
