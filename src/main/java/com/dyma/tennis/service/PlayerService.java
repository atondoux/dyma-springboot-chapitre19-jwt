package com.dyma.tennis.service;

import com.dyma.tennis.data.PlayerEntity;
import com.dyma.tennis.data.PlayerRepository;
import com.dyma.tennis.model.Player;
import com.dyma.tennis.model.PlayerToCreate;
import com.dyma.tennis.model.PlayerToUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private final PlayerRepository playerRepository;

    @Autowired
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public List<Player> getAllPlayers() {
        log.info("Invoking getAllPlayers()");
        try {
            return playerRepository.findAll().stream()
                    .map(playerMapper::playerEntityToPlayer)
                    .sorted(Comparator.comparing(player -> player.info().rank().position()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Could not retrieve players", e);
            throw new PlayerDataRetrievalException(e);
        }
    }

    public Player getByIdentifier(UUID identifier) {
        log.info("Invoking getByIdentifier with identifier={}", identifier);
        try {
            Optional<PlayerEntity> player = playerRepository.findOneByIdentifier(identifier);
            if (player.isEmpty()) {
                log.warn("Could not find player with identifier={}", identifier);
                throw new PlayerNotFoundException(identifier);
            }
            return playerMapper.playerEntityToPlayer(player.get());
        } catch (DataAccessException e) {
            log.error("Could not find player with identifier={}", identifier, e);
            throw new PlayerDataRetrievalException(e);
        }
    }

    public Player create(PlayerToCreate playerToCreate) {
        log.info("Invoking create with playerToCreate={}", playerToCreate);

        try {
            Optional<PlayerEntity> player = playerRepository.findOneByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(
                    playerToCreate.firstName(), playerToCreate.lastName(), playerToCreate.birthDate());
            if (player.isPresent()) {
                log.warn("Player to create with firstName={} lastName={} and birthDate={} already exists",
                        playerToCreate.firstName(), playerToCreate.lastName(), playerToCreate.birthDate());
                throw new PlayerAlreadyExistsException(playerToCreate.firstName(), playerToCreate.lastName(), playerToCreate.birthDate());
            }

            PlayerEntity playerToRegister = new PlayerEntity(
                    playerToCreate.lastName(),
                    playerToCreate.firstName(),
                    UUID.randomUUID(),
                    playerToCreate.birthDate(),
                    playerToCreate.points(),
                    999999999);

            PlayerEntity registeredPlayer = playerRepository.save(playerToRegister);

            RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
            List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
            playerRepository.saveAll(newRanking);

            return this.getByIdentifier(registeredPlayer.getIdentifier());
        } catch (DataAccessException e) {
            log.error("Could not create player={}", playerToCreate, e);
            throw new PlayerDataRetrievalException(e);
        }
    }

    public Player update(PlayerToUpdate playerToUpdate) {
        log.info("Invoking update with playerToUpdate={}", playerToUpdate);
        try {
            Optional<PlayerEntity> existingPlayer = playerRepository.findOneByIdentifier(playerToUpdate.identifier());
            if (existingPlayer.isEmpty()) {
                log.warn("Could not find player to update with identifier={}", playerToUpdate.identifier());
                throw new PlayerNotFoundException(playerToUpdate.identifier());
            }
            Optional<PlayerEntity> potentiallyDuplicatedPlayer = playerRepository.findOneByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(
                    playerToUpdate.firstName(), playerToUpdate.lastName(), playerToUpdate.birthDate());
            if (potentiallyDuplicatedPlayer.isPresent() && !potentiallyDuplicatedPlayer.get().getIdentifier().equals(playerToUpdate.identifier())) {
                log.warn("Player to update with firstName={} lastName={} and birthDate={} already exists",
                        playerToUpdate.firstName(), playerToUpdate.lastName(), playerToUpdate.birthDate());
                throw new PlayerAlreadyExistsException(playerToUpdate.firstName(), playerToUpdate.lastName(), playerToUpdate.birthDate());
            }

            existingPlayer.get().setFirstName(playerToUpdate.firstName());
            existingPlayer.get().setLastName(playerToUpdate.lastName());
            existingPlayer.get().setBirthDate(playerToUpdate.birthDate());
            existingPlayer.get().setPoints(playerToUpdate.points());
            PlayerEntity updatedPlayer = playerRepository.save(existingPlayer.get());

            RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
            List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
            playerRepository.saveAll(newRanking);

            return this.getByIdentifier(updatedPlayer.getIdentifier());
        } catch (DataAccessException e) {
            log.error("Could not update player={}", playerToUpdate, e);
            throw new PlayerDataRetrievalException(e);
        }
    }

    public void delete(UUID identifier) {
        log.info("Invoking delete with identifier={}", identifier);
        try {
            Optional<PlayerEntity> playerDelete = playerRepository.findOneByIdentifier(identifier);
            if (playerDelete.isEmpty()) {
                log.warn("Could not find player to delete with identifier={}", identifier);
                throw new PlayerNotFoundException(identifier);
            }

            playerRepository.delete(playerDelete.get());

            RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
            List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
            playerRepository.saveAll(newRanking);

        } catch (DataAccessException e) {
            log.error("Could not delete player with identifier={}", identifier, e);
            throw new PlayerDataRetrievalException(e);
        }
    }
}