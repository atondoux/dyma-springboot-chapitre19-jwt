package com.dyma.tennis.service;

import com.dyma.tennis.data.PlayerEntity;
import com.dyma.tennis.model.Player;
import com.dyma.tennis.model.PlayerDescription;
import com.dyma.tennis.model.Rank;
import com.dyma.tennis.model.TournamentDescription;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlayerMapper {

    public Player playerEntityToPlayer(PlayerEntity playerEntity) {
        PlayerDescription description = new PlayerDescription(
                playerEntity.getIdentifier(),
                playerEntity.getFirstName(),
                playerEntity.getLastName(),
                playerEntity.getBirthDate(),
                new Rank(playerEntity.getRank(), playerEntity.getPoints())
        );
        Set<TournamentDescription> tournaments = playerEntity.getTournaments().stream()
                .map(
                        tournamentEntity ->
                                new TournamentDescription(
                                        tournamentEntity.getIdentifier(),
                                        tournamentEntity.getName(),
                                        tournamentEntity.getStartDate(),
                                        tournamentEntity.getEndDate(),
                                        tournamentEntity.getPrizeMoney(),
                                        tournamentEntity.getCapacity()
                                )
                )
                .collect(Collectors.toSet());

        return new Player(description, tournaments);
    }
}
