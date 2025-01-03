package com.dyma.tennis.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
    Optional<TournamentEntity> findOneByIdentifier(UUID identifier);

    Optional<TournamentEntity> findOneByNameIgnoreCase(String name);
}
