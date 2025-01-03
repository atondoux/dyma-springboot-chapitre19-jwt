package com.dyma.tennis.data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tournament", schema = "public")
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identifier", nullable = false, unique = true)
    private UUID identifier;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "prize_money", nullable = true)
    private Integer prizeMoney;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ManyToMany(mappedBy = "tournaments", fetch = FetchType.EAGER)
    private Set<PlayerEntity> players = new HashSet<>();

    public TournamentEntity() {
    }

    public TournamentEntity(UUID identifier, String name, LocalDate startDate, LocalDate endDate, Integer prizeMoney, Integer capacity) {
        this.identifier = identifier;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prizeMoney = prizeMoney;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getPrizeMoney() {
        return prizeMoney;
    }

    public void setPrizeMoney(Integer prizeMoney) {
        this.prizeMoney = prizeMoney;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerEntity> players) {
        this.players = players;
    }

    public boolean isFull() {
        return capacity == players.size() - 1;
    }

    public boolean hasPlayer(PlayerEntity playerToRegister) {
        return players.contains(playerToRegister);
    }
}
