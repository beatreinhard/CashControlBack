package ch.reinhard.cashcontrol.modules.steuern.impl.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "grundstueckunterhalt", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class Grundstueckunterhalt {
    @Id
    String id;

    @Version
    Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    Integer jahr;

    @NotNull
    LocalDate rgDatum;

    @NotNull
    String ausfuehrendeFirma;

    @NotNull
    String arbeitsArt;

    @NotNull
    BigDecimal betragNetto;             // effektiver Rechnungsbetrag

    @NotNull
    BigDecimal anteilAndereKosten;      // nicht für die Steuerrechnung relevanter Anteil

    @NotNull
    BigDecimal anteilUnterhalt;         // betragNetto minus anteilAndereKosten
}
