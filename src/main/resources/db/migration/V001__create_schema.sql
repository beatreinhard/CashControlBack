create schema if not exists cashcontrol;

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
    bezeichnung     varchar(255)    not null
);


