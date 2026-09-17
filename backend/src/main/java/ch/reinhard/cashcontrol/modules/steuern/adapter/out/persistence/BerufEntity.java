package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "beruf", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class BerufEntity {
    @Id
    private String id;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    private String arbeitnehmer;

    @NotNull
    private String arbeitgeber;

    @NotNull
    private String arbeitsort;

    @NotNull
    private Integer beschaeftigungsgrad;

    @NotNull
    private Integer arbeitstage;

    private Integer fahrtkilometerProTag;

    private String grundAutobenutzung;

    @NotNull
    private BigDecimal ansatzAuswaertigeVerpflegung;

    private String bemerkung;

    public void update(BerufEntity berufEntity) {
        jahr = berufEntity.jahr;
        arbeitnehmer = berufEntity.arbeitnehmer;
        arbeitgeber = berufEntity.arbeitgeber;
        arbeitsort = berufEntity.arbeitsort;
        beschaeftigungsgrad = berufEntity.beschaeftigungsgrad;
        arbeitstage = berufEntity.arbeitstage;
        fahrtkilometerProTag = berufEntity.fahrtkilometerProTag;
        grundAutobenutzung = berufEntity.grundAutobenutzung;
        ansatzAuswaertigeVerpflegung = berufEntity.ansatzAuswaertigeVerpflegung;
        bemerkung = berufEntity.bemerkung;
    }
}
