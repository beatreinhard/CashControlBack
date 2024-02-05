package ch.reinhard.cashcontrol.modules.finanzen.impl.domain;

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
    String id;

    Long version;

    LocalDate datum;

    String empfaenger;

    String kategorieId;
    String kategorie_bezeichnung;

    String text;
    BigDecimal betrag;

}
