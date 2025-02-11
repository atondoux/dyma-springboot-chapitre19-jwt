package com.dyma.tennis.web;

import com.dyma.tennis.model.Error;
import com.dyma.tennis.model.Player;
import com.dyma.tennis.model.PlayerToCreate;
import com.dyma.tennis.model.PlayerToUpdate;
import com.dyma.tennis.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tennis Players API")
@RestController
@RequestMapping("/players")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Operation(summary = "Finds players", description = "Finds players", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players list",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Player.class)))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @GetMapping
    public List<Player> list() {
        return playerService.getAllPlayers();
    }

    @Operation(summary = "Finds a player", description = "Finds a player", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Player.class))}),
            @ApiResponse(responseCode = "404", description = "Player with specified identifier was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @GetMapping("{identifier}")
    public Player getPlayer(@PathVariable("identifier") UUID identifier) {
        return playerService.getByIdentifier(identifier);
    }

    @Operation(summary = "Creates a player", description = "Creates a player", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created player",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerToCreate.class))}),
            @ApiResponse(responseCode = "400", description = "Player already exists.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @PostMapping
    public Player createPlayer(@RequestBody @Valid PlayerToCreate playerToCreate) {
        return playerService.create(playerToCreate);
    }

    @Operation(summary = "Updates a player", description = "Updates a player", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated player",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerToUpdate.class))}),
            @ApiResponse(responseCode = "404", description = "Player with specified identifier was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @PutMapping
    public Player updatePlayer(@RequestBody @Valid PlayerToUpdate playerToUpdate) {
        return playerService.update(playerToUpdate);
    }

    @Operation(summary = "Deletes a player", description = "Deletes a player", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player has been deleted"),
            @ApiResponse(responseCode = "404", description = "Player with specified identifier was not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Error.class))}),
            @ApiResponse(responseCode = "403", description = "This user is not authorized to perform this action.")

    })
    @DeleteMapping("{identifier}")
    public void deletePlayer(@PathVariable("identifier") UUID identifier) {
        playerService.delete(identifier);
    }
}