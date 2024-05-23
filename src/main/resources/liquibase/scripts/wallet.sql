-- liquibase formatted sql

-- changeset denieva:1

CREATE TABLE IF NOT EXISTS wallet
(
    id          BIGINT,
    request_id uuid PRIMARY KEY,
    user_id   integer REFERENCES users (id),
    wallet_creation_date timestamp,
    wallet_amount     float,
    deposit_amount float,
    withdraw_amount float

);
