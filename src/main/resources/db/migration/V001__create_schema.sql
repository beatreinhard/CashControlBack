create schema if not exists cashcontrol;
ALTER SCHEMA cashcontrol OWNER TO cashcontrol;

create table zahlung (
    id              varchar(255)    not null primary key,
    version         bigint,
    betrag          numeric,
    datum           date            not null,
    empfaenger      varchar(255)    not null,
    kategorie_id    varchar(255)    not null,
    text            varchar(255)
);

create index index_kategorie_id on zahlung (
    kategorie_id
);

create table kategorie (
    id              varchar(255)    not null primary key,
    version         bigint,
    bezeichnung     varchar(255)    not null
);


-- Erstelle die View "zahlung_view"
CREATE VIEW zahlung_view AS
SELECT
    z.id,
    z.version,
    z.betrag,
    z.datum,
    z.empfaenger,
    z.kategorie_id,
    z.text,
    k.bezeichnung AS kategorie_bezeichnung
FROM
    zahlung z
        JOIN
    kategorie k ON z.kategorie_id = k.id;


