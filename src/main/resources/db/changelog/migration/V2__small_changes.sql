-- liquibase formatted sql

-- changeset lunguion:1720992490558-1
ALTER TABLE users
    ADD username VARCHAR(255);

-- changeset lunguion:1720992490558-2
ALTER TABLE users DROP COLUMN name;

