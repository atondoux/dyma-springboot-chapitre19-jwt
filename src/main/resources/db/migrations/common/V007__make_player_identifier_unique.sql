ALTER TABLE player
ALTER COLUMN identifier SET NOT NULL;

ALTER TABLE player
ADD CONSTRAINT identifier_unique UNIQUE (identifier);

ALTER TABLE player
ADD CONSTRAINT player_unique UNIQUE (first_name, last_name, birth_date);