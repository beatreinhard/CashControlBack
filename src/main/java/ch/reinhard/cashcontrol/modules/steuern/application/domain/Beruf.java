package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
    String id;

    @Version
    Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    Integer jahr;

    @NotNull
    String arbeitnehmer;

    @NotNull
    String arbeitgeber;

    @NotNull
    String arbeitsort;

    @NotNull
    Integer beschaeftigungsgrad;

    @NotNull
    Integer arbeitstage;

    Integer fahrtkilometerProTag;

    String grundAutobenutzung;

    @NotNull
    BigDecimal ansatzAuswaertigeVerpflegung;

    String bemerkung;
}
