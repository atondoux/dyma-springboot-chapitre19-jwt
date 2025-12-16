package com.dyma.tennis.web;

import com.dyma.tennis.data.PlayerList;
import com.dyma.tennis.service.PlayerNotFoundException;
import com.dyma.tennis.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Test
    public void shouldListAllPlayers() {
        // Given
        when(playerService.getAllPlayers()).thenReturn(PlayerList.ALL);

        // When
        var response = mockMvc.get().uri("/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // Then
        var json = response.assertThat().hasStatus(HttpStatus.OK).bodyJson();
        json.extractingPath("$.length()").isEqualTo(4);
        json.extractingPath("$[0].info.lastName").isEqualTo("Nadal");
        json.extractingPath("$[1].info.lastName").isEqualTo("Djokovic");
        json.extractingPath("$[2].info.lastName").isEqualTo("Federer");
        json.extractingPath("$[3].info.lastName").isEqualTo("Murray");
    }

    @Test
    public void shouldRetrievePlayer() {
        // Given
        UUID playerToRetrieve = UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e");
        Mockito.when(playerService.getByIdentifier(playerToRetrieve)).thenReturn(PlayerList.RAFAEL_NADAL);

        // When
        var response = mockMvc.get().uri("/players/b466c6f7-52c6-4f25-b00d-c562be41311e")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // Then
        var json = response.assertThat().hasStatus(HttpStatus.OK).bodyJson();
        json.extractingPath("$.info.lastName").isEqualTo("Nadal");
        json.extractingPath("$.info.rank.position").isEqualTo(1);
    }

    @Test
    public void shouldReturn404NotFound_WhenPlayerDoesNotExist() {
        // Given
        UUID playerToRetrieve = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");
        Mockito.when(playerService.getByIdentifier(playerToRetrieve)).thenThrow(new PlayerNotFoundException(playerToRetrieve));

        // When
        var response = mockMvc.get().uri("/players/aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // Then
        var json = response.assertThat().hasStatus(HttpStatus.NOT_FOUND).bodyJson();
        json.extractingPath("$.errorDetails").isEqualTo("Player with identifier aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb could not be found.");
    }
}
