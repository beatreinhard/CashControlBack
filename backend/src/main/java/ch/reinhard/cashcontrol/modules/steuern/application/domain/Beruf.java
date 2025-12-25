package ch.reinhard.cashcontrol.modules.steuern.application.domain;

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
public class Beruf {
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

    public void update(Beruf beruf) {
        jahr = beruf.jahr;
        arbeitnehmer = beruf.arbeitnehmer;
        arbeitgeber = beruf.arbeitgeber;
        arbeitsort = beruf.arbeitsort;
        beschaeftigungsgrad = beruf.beschaeftigungsgrad;
        arbeitstage = beruf.arbeitstage;
        fahrtkilometerProTag = beruf.fahrtkilometerProTag;
        grundAutobenutzung = beruf.grundAutobenutzung;
        ansatzAuswaertigeVerpflegung = beruf.ansatzAuswaertigeVerpflegung;
        bemerkung = beruf.bemerkung;
    }
}
