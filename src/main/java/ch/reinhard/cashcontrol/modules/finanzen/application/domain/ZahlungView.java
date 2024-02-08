package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "zahlungView", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZahlungView {
    @Id
    private String id;

    private Long version;

    private LocalDate datum;

    private String empfaenger;

    private String kategorieId;
    private String kategorie_bezeichnung;

    private String text;
    private BigDecimal betrag;
}
