package com.dyma.tennis.service;

import com.dyma.tennis.data.PlayerEntityList;
import com.dyma.tennis.data.PlayerRepository;
import com.dyma.tennis.data.TournamentEntityList;
import com.dyma.tennis.data.TournamentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void shouldRegisterPlayerToTournament() {
        // Given
        UUID frenchOpen = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
        UUID rafaelNadal = UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e");
        when(tournamentRepository.findOneByIdentifier(frenchOpen)).thenReturn(Optional.of(TournamentEntityList.FRENCH_OPEN));
        when(playerRepository.findOneByIdentifier(rafaelNadal)).thenReturn(Optional.of(PlayerEntityList.RAFAEL_NADAL));

        // When
        registrationService.register(frenchOpen, rafaelNadal);

        // Then
        verify(playerRepository).save(PlayerEntityList.RAFAEL_NADAL);
    }

    @Test
    public void shouldFailToRegister_WhenTournamentIsNotFound() {
        // Given
        UUID frenchOpen = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
        UUID rafaelNadal = UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e");
        when(tournamentRepository.findOneByIdentifier(frenchOpen)).thenReturn(Optional.empty());

        // When / Then
        Assertions.assertThatThrownBy(() -> registrationService.register(frenchOpen, rafaelNadal))
                .isInstanceOf(TournamentRegistrationException.class)
                .hasMessage("Tournament a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12 does not exist");
    }

    @Test
    public void shouldFailToRegister_WhenPlayerIsNotFound() {
        // Given
        UUID frenchOpen = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
        UUID rafaelNadal = UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e");
        when(tournamentRepository.findOneByIdentifier(frenchOpen)).thenReturn(Optional.of(TournamentEntityList.FRENCH_OPEN));
        when(playerRepository.findOneByIdentifier(rafaelNadal)).thenReturn(Optional.empty());

        // When / Then
        Assertions.assertThatThrownBy(() -> registrationService.register(frenchOpen, rafaelNadal))
                .isInstanceOf(TournamentRegistrationException.class)
                .hasMessage("Player b466c6f7-52c6-4f25-b00d-c562be41311e does not exist");
    }
}
