CREATE TABLE IF NOT EXISTS USER
(
    id                 UUID NOT NULL,
    created_timestamp  TIMESTAMP NOT NULL,
    date_of_birth      DATE NOT NULL,
    first_name         VARCHAR(255) NOT NULL,
    job_title          VARCHAR(255) NOT NULL,
    last_name          VARCHAR(255) NOT NULL,
    title              INTEGER,
    deleted            BOOLEAN,
    PRIMARY KEY (id)
)
