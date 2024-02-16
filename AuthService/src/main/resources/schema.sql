DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS SPRING_SESSION_ATTRIBUTES;
DROP TABLE IF EXISTS SPRING_SESSION;

CREATE TABLE IF NOT EXISTS users
(
    user_id
                  INT
        GENERATED
            ALWAYS AS
            IDENTITY
        PRIMARY
            KEY
        UNIQUE,
    username
                  VARCHAR(256) NOT NULL,
    email         VARCHAR(256) NOT NULL UNIQUE,
    password_hash varchar(512) NOT NULL,
    is_activated  boolean      NOT NULL DEFAULT true,
    is_locked     boolean      NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS roles
(
    role_id
        serial,
    name
        varchar(150) not null,
    primary key
        (
         role_id
            )
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id
        bigint
        not
            null,
    role_id
        int
        not
            null,
    primary
        key
        (
         user_id,
         role_id
            ),
    foreign key
        (
         user_id
            ) references users
        (
         user_id
            ),
    foreign key
        (
         role_id
            ) references roles
        (
         role_id
            )
);

CREATE TABLE IF NOT EXISTS SPRING_SESSION
(
    PRIMARY_ID
                          CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY
        (
         PRIMARY_ID
            )
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID
                    CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME  VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA        NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY
        (
         SESSION_PRIMARY_ID,
         ATTRIBUTE_NAME
            ),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY
        (
         SESSION_PRIMARY_ID
            ) REFERENCES SPRING_SESSION
            (
             PRIMARY_ID
                ) ON DELETE CASCADE
);