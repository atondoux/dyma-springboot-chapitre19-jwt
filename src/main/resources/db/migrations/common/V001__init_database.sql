CREATE SEQUENCE player_id_seq;

CREATE TABLE player
(
    id integer NOT NULL DEFAULT nextval('player_id_seq'),
    last_name character varying(50) NOT NULL,
    first_name character varying(50) NOT NULL,
    birth_date date NOT NULL,
    points integer NOT NULL,
    rank integer NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE user_id_seq;

CREATE TABLE dyma_user (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    login character varying(50) NOT NULL,
    password character varying(60) NOT NULL,
    last_name character varying(50) NOT NULL,
    first_name character varying(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE dyma_role
(
    name character varying(50) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE dyma_user_role
(
    user_id bigint NOT NULL,
    role_name character varying(50) NOT NULL,
    CONSTRAINT dyma_user_role_pkey PRIMARY KEY (user_id, role_name),
    CONSTRAINT fk_role_name FOREIGN KEY (role_name)
        REFERENCES public.dyma_role (name),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES public.dyma_user (id)
);
