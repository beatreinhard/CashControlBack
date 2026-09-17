package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class GrundstueckunterhaltEntity {
    @Id
    private String id;

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

    public void update(GrundstueckunterhaltEntity grundstueckunterhaltEntity) {
        jahr = grundstueckunterhaltEntity.jahr;
        rgDatum = grundstueckunterhaltEntity.rgDatum;
        ausfuehrendeFirma = grundstueckunterhaltEntity.ausfuehrendeFirma;
        arbeitsArt = grundstueckunterhaltEntity.arbeitsArt;
        betragNetto = grundstueckunterhaltEntity.betragNetto;
        anteilAndereKosten = grundstueckunterhaltEntity.anteilAndereKosten;
        anteilUnterhalt = grundstueckunterhaltEntity.anteilUnterhalt;
    }
}
