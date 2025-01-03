package com.dyma.tennis.web;

import com.dyma.tennis.data.PlayerList;
import com.dyma.tennis.service.PlayerNotFoundException;
import com.dyma.tennis.service.PlayerService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Test
    public void shouldListAllPlayers() throws Exception {
        // Given
        Mockito.when(playerService.getAllPlayers()).thenReturn(PlayerList.ALL);

        // When / Then
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].info.lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$[1].info.lastName", CoreMatchers.is("Djokovic")))
                .andExpect(jsonPath("$[2].info.lastName", CoreMatchers.is("Federer")))
                .andExpect(jsonPath("$[3].info.lastName", CoreMatchers.is("Murray")));
    }

    @Test
    public void shouldRetrievePlayer() throws Exception {
        // Given
        UUID playerToRetrieve = UUID.fromString("b466c6f7-52c6-4f25-b00d-c562be41311e");
        Mockito.when(playerService.getByIdentifier(playerToRetrieve)).thenReturn(PlayerList.RAFAEL_NADAL);

        // When / Then
        mockMvc.perform(get("/players/b466c6f7-52c6-4f25-b00d-c562be41311e"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.lastName", CoreMatchers.is("Nadal")))
                .andExpect(jsonPath("$.info.rank.position", CoreMatchers.is(1)));
    }

    @Test
    public void shouldReturn404NotFound_WhenPlayerDoesNotExist() throws Exception {
        // Given
        UUID playerToRetrieve = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");
        Mockito.when(playerService.getByIdentifier(playerToRetrieve)).thenThrow(new PlayerNotFoundException(playerToRetrieve));

        // When / Then
        mockMvc.perform(get("/players/aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb"))
                .andExpect(status().isNotFound());
    }
}