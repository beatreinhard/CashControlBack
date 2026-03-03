create or replace view ausgabe_view as
select
    a.id,
    a.betrag,
    a.datum,
    a.zahlender,
    a.empfaenger,
    a.kategorie,
    a.text,
    p.name      as person_name,
    p.vorname   as person_vorname
from ausgabe a
         left join person p
                   on a.zahlender = p.id;
