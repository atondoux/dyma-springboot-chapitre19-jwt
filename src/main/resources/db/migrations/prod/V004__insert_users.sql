INSERT INTO public.dyma_user(login, password, last_name, first_name) VALUES
('admin', '$2a$12$VLMmCnWg6g1ZWfctUUYpWeyfArfbPzlq1EC1hi5BPSQeJWMwjmpdy', 'Dyma', 'Admin'),
('visitor', '$2a$12$ACcMbD/j30wmsucWNZpMaeJaO2w0tBIswOzDMOjZhVvEp6RzPhgWS', 'Doe', 'John');

INSERT INTO public.dyma_user_role(user_id, role_name) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');