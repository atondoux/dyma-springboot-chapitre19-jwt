CREATE TABLE player_tournament
(
    player_id bigint NOT NULL,
    tournament_id bigint NOT NULL,
    CONSTRAINT player_tournament_pkey PRIMARY KEY (player_id, tournament_id),
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES public.player (id),
    CONSTRAINT fk_tournament FOREIGN KEY (tournament_id) REFERENCES public.tournament (id)
);