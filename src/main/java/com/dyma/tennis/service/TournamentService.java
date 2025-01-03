package com.dyma.tennis.service;

import com.dyma.tennis.data.TournamentEntity;
import com.dyma.tennis.data.TournamentRepository;
import com.dyma.tennis.model.Tournament;
import com.dyma.tennis.model.TournamentToCreate;
import com.dyma.tennis.model.TournamentToUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentService.class);

    @Autowired
    private final TournamentRepository tournamentRepository;

    @Autowired
    private final TournamentMapper tournamentMapper;

    public TournamentService(TournamentRepository tournamentRepository, TournamentMapper tournamentMapper) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
    }

    public List<Tournament> getAllTournaments() {
        log.info("Invoking getAllTournaments()");
        try {
            return tournamentRepository.findAll().stream()
                    .map(tournamentMapper::tournamentEntityToTournament)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Could not retrieve tournaments", e);
            throw new TournamentDataRetrievalException(e);
        }
    }

    public Tournament getByIdentifier(UUID identifier) {
        log.info("Invoking getByIdentifier with identifier={}", identifier);
        try {
            Optional<TournamentEntity> tournament = tournamentRepository.findOneByIdentifier(identifier);
            if (tournament.isEmpty()) {
                log.warn("Could not find tournament with identifier={}", identifier);
                throw new TournamentNotFoundException(identifier);
            }
            return tournamentMapper.tournamentEntityToTournament(tournament.get());
        } catch (DataAccessException e) {
            log.error("Could not find tournament with identifier={}", identifier, e);
            throw new TournamentDataRetrievalException(e);
        }
    }

    public Tournament create(TournamentToCreate tournamentToCreate) {
        log.info("Invoking create with tournamentToCreate={}", tournamentToCreate);

        Optional<TournamentEntity> tournament = tournamentRepository.findOneByNameIgnoreCase(tournamentToCreate.name());
        if (tournament.isPresent()) {
            log.warn("Tournament to create with name={} already exists", tournamentToCreate.name());
            throw new TournamentAlreadyExistsException(tournamentToCreate.name());
        }

        try {
            TournamentEntity tournamentToRegister = new TournamentEntity(
                    UUID.randomUUID(),
                    tournamentToCreate.name(),
                    tournamentToCreate.startDate(),
                    tournamentToCreate.endDateDate(),
                    tournamentToCreate.prizeMoney(),
                    tournamentToCreate.capacity());

            TournamentEntity registeredTournament = tournamentRepository.save(tournamentToRegister);

            return this.getByIdentifier(registeredTournament.getIdentifier());
        } catch (DataAccessException e) {
            log.error("Could not create tournament={}", tournamentToCreate, e);
            throw new TournamentDataRetrievalException(e);
        }
    }

    public Tournament update(TournamentToUpdate tournamentToUpdate) {
        log.info("Invoking update with tournamentToUpdate={}", tournamentToUpdate);
        try {
            Optional<TournamentEntity> existingTournament = tournamentRepository.findOneByIdentifier(tournamentToUpdate.identifier());
            if (existingTournament.isEmpty()) {
                log.warn("Could not find tournament to update with identifier={}", tournamentToUpdate.identifier());
                throw new TournamentNotFoundException(tournamentToUpdate.identifier());
            }

            Optional<TournamentEntity> potentiallyDuplicatedTournament = tournamentRepository.findOneByNameIgnoreCase(tournamentToUpdate.name());
            if (potentiallyDuplicatedTournament.isPresent() && !potentiallyDuplicatedTournament.get().getIdentifier().equals(tournamentToUpdate.identifier())) {
                log.warn("Tournament to update with name={} already exists", tournamentToUpdate.name());
                throw new TournamentAlreadyExistsException(tournamentToUpdate.name());
            }

            existingTournament.get().setName(tournamentToUpdate.name());
            existingTournament.get().setStartDate(tournamentToUpdate.startDate());
            existingTournament.get().setEndDate(tournamentToUpdate.endDateDate());
            existingTournament.get().setPrizeMoney(tournamentToUpdate.prizeMoney());
            existingTournament.get().setCapacity(tournamentToUpdate.capacity());

            TournamentEntity updatedTournament = tournamentRepository.save(existingTournament.get());

            return this.getByIdentifier(updatedTournament.getIdentifier());
        } catch (DataAccessException e) {
            log.error("Could not update tournament={}", tournamentToUpdate, e);
            throw new TournamentDataRetrievalException(e);
        }
    }

    public void delete(UUID identifier) {
        log.info("Invoking delete with identifier={}", identifier);
        try {
            Optional<TournamentEntity> tournamentToDelete = tournamentRepository.findOneByIdentifier(identifier);
            if (tournamentToDelete.isEmpty()) {
                log.warn("Could not find tournament to delete with identifier={}", identifier);
                throw new TournamentNotFoundException(identifier);
            }

            tournamentRepository.delete(tournamentToDelete.get());
        } catch (DataAccessException e) {
            log.error("Could not delete tournament with identifier={}", identifier, e);
            throw new TournamentDataRetrievalException(e);
        }
    }
}
