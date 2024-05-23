-- liquibase formatted sql

-- changeset denieva:1

CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    login varchar NOT NULL
);
