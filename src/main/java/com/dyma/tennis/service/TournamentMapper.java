package com.dyma.tennis.service;

import com.dyma.tennis.data.TournamentEntity;
import com.dyma.tennis.model.PlayerDescription;
import com.dyma.tennis.model.Rank;
import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentDescription;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TournamentMapper {

    public Tournament tournamentEntityToTournament(TournamentEntity tournamentEntity) {
        TournamentDescription description = new TournamentDescription(
                tournamentEntity.getIdentifier(),
                tournamentEntity.getName(),
                tournamentEntity.getStartDate(),
                tournamentEntity.getEndDate(),
                tournamentEntity.getPrizeMoney(),
                tournamentEntity.getCapacity()
        );
        Set<PlayerDescription> players = tournamentEntity.getPlayers().stream()
                .map(playerEntity -> new PlayerDescription(
                                playerEntity.getIdentifier(),
                                playerEntity.getFirstName(),
                                playerEntity.getLastName(),
                                playerEntity.getBirthDate(),
                                new Rank(playerEntity.getRank(), playerEntity.getPoints())
                        )
                )
                .collect(Collectors.toSet());

        return new Tournament(description, players);
    }
}
