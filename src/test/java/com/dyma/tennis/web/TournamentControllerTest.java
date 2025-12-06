package com.dyma.tennis.web;

import com.dyma.tennis.data.TournamentList;
import com.dyma.tennis.service.RegistrationService;
import com.dyma.tennis.service.TournamentNotFoundException;
import com.dyma.tennis.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;

@WebMvcTest(controllers = TournamentController.class)
public class TournamentControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private TournamentService tournamentService;

    @MockitoBean
    private RegistrationService registrationService;

    @Test
    public void shouldListAllTournaments() {
        // Given
        Mockito.when(tournamentService.getAllTournaments()).thenReturn(TournamentList.ALL);

        // When
        var response = mockMvc.get().uri("/tournaments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // Then
        var json = response.assertThat().hasStatus(HttpStatus.OK).bodyJson();
        json.extractingPath("$.length()").isEqualTo(4);
        json.extractingPath("$[0].info.name").isEqualTo("Australian Open");
        json.extractingPath("$[1].info.name").isEqualTo("French Open");
        json.extractingPath("$[2].info.name").isEqualTo("Wimbledon");
        json.extractingPath("$[3].info.name").isEqualTo("US Open");
    }

    @Test
    public void shouldRetrieveTournament() {
        // Given
        UUID tournamentToRetrieve = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
        Mockito.when(tournamentService.getByIdentifier(tournamentToRetrieve)).thenReturn(TournamentList.FRENCH_OPEN);

        // When
        var response = mockMvc.get().uri("/tournaments/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // Then
        var json = response.assertThat().hasStatus(HttpStatus.OK).bodyJson();
        json.extractingPath("$.info.name").isEqualTo("French Open");
    }

    @Test
    public void shouldReturn404NotFound_WhenTournamentDoesNotExist() {
        // Given
        UUID tournamentToRetrieve = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12");
        Mockito.when(tournamentService.getByIdentifier(tournamentToRetrieve)).thenThrow(new TournamentNotFoundException(tournamentToRetrieve));

        // When
        var response = mockMvc.get().uri("/tournaments/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // Then
        var json = response.assertThat().hasStatus(HttpStatus.NOT_FOUND).bodyJson();
        json.extractingPath("$.errorDetails").isEqualTo("Tournament with identifier a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12 could not be found.");
    }
}
