package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class AusgabeBo {
    private String id;
    private Long version;
    private LocalDate datum;
    private String empfaenger;
    private AusgabeKategorie kategorie;
    private String text;
    private BigDecimal betrag;
}
