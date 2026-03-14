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

create or replace view kosten_view as
select
    k.id,
    k.ausgabe_id,
    k.jahr,
    k.art,
    k.empfaenger,
    k.zahlender,
    k.betrag,
    k.bemerkung,
    p.name      as person_name,
    p.vorname   as person_vorname
from kosten k
         left join person p
                   on k.zahlender = p.id;
