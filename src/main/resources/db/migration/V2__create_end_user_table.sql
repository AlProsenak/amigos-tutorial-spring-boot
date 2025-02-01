CREATE SEQUENCE tutorial.end_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE tutorial.end_user
(
    /* Alternative to `bigint` with sequence is `bigserial`, since it auto-increments, by automatically creating `bigint` and attaching sequence under the hood. */
    id         bigint    NOT NULL UNIQUE DEFAULT nextval('tutorial.end_user_id_sequence'),
    username   text      NOT NULL UNIQUE,
    email      text      NOT NULL UNIQUE,
    birthdate  date      NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,

    CONSTRAINT pk_end_user_table PRIMARY KEY (id)
);

/* The sequence is marked as OWNED BY the column, so that it will be dropped if the column or table is dropped. */
ALTER SEQUENCE tutorial.end_user_id_sequence OWNED BY tutorial.end_user.id;
