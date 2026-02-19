create schema if not exists cashcontrol;

-- Schema-wechsel
SET search_path = cashcontrol;


create table ausgabe (
    id              varchar(255)    not null primary key,
    betrag          decimal         not null,
    datum           date            not null,
    zahlender       varchar(255),
    empfaenger      varchar(255)    not null,
    kategorie       varchar(255)    not null,
    text            varchar(255)
);


create table vermoegenswert (
    id               varchar(255)    not null primary key,
    jahr             integer         not null,
    bezeichnung       varchar(255)   not null,
    anschaffungsjahr  integer        not null,
    anschaffungspreis decimal        not null
);

create table schuld (
    id              varchar(255)    not null primary key,
    jahr            integer         not null,
    glaeubiger      varchar(255)    not null,
    art             varchar(255)    not null,
    text            varchar(255),
    betrag          decimal         not null,
    zinsen          decimal
);

create table kosten (
    id              varchar(255)    not null primary key,
    ausgabe_id      varchar(255),
    jahr            integer         not null,
    art             varchar(255)    not null,
    empfaenger      varchar(255)    not null,
    zahlender       varchar(255)    not null,
    betrag          decimal         not null,
    bemerkung       varchar(255)
);

create table beruf (
    id                          varchar(255)    not null primary key,
    jahr                        integer         not null,
    arbeitnehmer                varchar(255)    not null,
    arbeitgeber                 varchar(255)    not null,
    arbeitsort                  varchar(255)    not null,
    beschaeftigungsgrad         integer         not null,
    arbeitstage                 integer         not null,
    fahrtkilometer_pro_tag      integer,
    grund_autobenutzung         varchar(255),
    ansatz_auswaertige_verpflegung    decimal   not null,
    bemerkung                   varchar(255)
);

create table erbschaft (
    id                          varchar(255)    not null primary key,
    jahr                        integer         not null,
    art                         varchar(255)    not null,
    betrag                      decimal         not null,
    datum                       date            not null,
    geber                       varchar(255)    not null,
    verwandtschaftsverhaeltnis  varchar(255)    not null,
    gegenstand                  varchar(255)    not null,
    bemerkung                   varchar(255)
);

create table grundstueckunterhalt (
    id                          varchar(255)    not null primary key,
    jahr                        integer         not null,
    rg_datum                     date            not null,
    ausfuehrende_firma          varchar(255)    not null,
    arbeits_art                 varchar(255)    not null,
    betrag_netto                decimal         not null,   -- effektiver Rechnungsbetrag
    anteil_andere_kosten        decimal,                    -- nicht für die Steuerrechnung relevanter Anteil
    anteil_unterhalt            decimal         not null    -- betrag_netto minus anteil_andere_kosten
);

create table vergabung
(
    id             varchar(255) not null primary key,
    ausgabe_id     varchar(255) not null,
    jahr           integer      not null,
    zahlungs_datum date         not null,
    empfaenger     varchar(255) not null,
    betrag         decimal      not null
);
