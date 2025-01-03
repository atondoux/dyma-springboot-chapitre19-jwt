INSERT INTO public.dyma_user(login, password, last_name, first_name) VALUES
('admin', '$2a$12$RkcdJn2kLrAS9fmvDv/CWehqID8nB3XBWXOtazhQ2PY1ZFwDB3L76', 'Dyma', 'Admin'),
('user', '$2a$12$VRnUGZfeEsWHG9jb7NyvQuhpISK65N2LtWyqXAi5t1CBWIQ34uRNa', 'Doe', 'John');

INSERT INTO public.dyma_user_role(user_id, role_name) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');