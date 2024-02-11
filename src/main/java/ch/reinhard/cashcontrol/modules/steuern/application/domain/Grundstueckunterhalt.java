package ch.reinhard.cashcontrol.modules.steuern.application.domain;

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
    private String id;

    @Version
    private Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    private LocalDate rgDatum;

    @NotNull
    private String ausfuehrendeFirma;

    @NotNull
    private String arbeitsArt;

    @NotNull
    private BigDecimal betragNetto; // effektiver Rechnungsbetrag

    @NotNull
    private BigDecimal anteilAndereKosten; // nicht für die Steuerrechnung relevanter Anteil

    @NotNull
    private BigDecimal anteilUnterhalt; // betragNetto minus anteilAndereKosten

    public void update(Grundstueckunterhalt grundstueckunterhalt) {
        jahr = grundstueckunterhalt.jahr;
        rgDatum = grundstueckunterhalt.rgDatum;
        ausfuehrendeFirma = grundstueckunterhalt.ausfuehrendeFirma;
        arbeitsArt = grundstueckunterhalt.arbeitsArt;
        betragNetto = grundstueckunterhalt.betragNetto;
        anteilAndereKosten = grundstueckunterhalt.anteilAndereKosten;
        anteilUnterhalt = grundstueckunterhalt.anteilUnterhalt;
    }
}
