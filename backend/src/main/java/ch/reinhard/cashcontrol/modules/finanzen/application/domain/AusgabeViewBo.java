package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AusgabeViewBo {
    private String id;
    private LocalDate datum;
    private String zahlender;
    private String empfaenger;
    private AusgabeKategorie kategorie;
    private String text;
    private BigDecimal betrag;
    private String personName;
    private String personVorname;
}
