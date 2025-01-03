CREATE SEQUENCE tournament_id_seq;

CREATE TABLE tournament
(
    id integer NOT NULL DEFAULT nextval('tournament_id_seq'),
    identifier uuid NOT NULL UNIQUE,
    name character varying(100) NOT NULL UNIQUE,
    start_date date NOT NULL,
    end_date date NOT NULL,
    prize_money integer,
    capacity smallint NOT NULL,
    PRIMARY KEY (id)
);