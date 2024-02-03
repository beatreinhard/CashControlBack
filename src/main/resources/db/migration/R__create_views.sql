-- Schema-wechsel
SET search_path = cashcontrol;

DROP VIEW IF EXISTS zahlung_view;

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
