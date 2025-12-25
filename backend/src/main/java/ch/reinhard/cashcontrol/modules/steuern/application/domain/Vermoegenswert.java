package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vermoegenswert", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class Vermoegenswert {
    @Id
    private String id;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    private String bezeichnung;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer anschaffungsjahr;

    @NotNull
    private BigDecimal anschaffungspreis;

    public void update(Vermoegenswert vermoegenswert) {
        jahr = vermoegenswert.jahr;
        bezeichnung = vermoegenswert.bezeichnung;
        anschaffungsjahr = vermoegenswert.anschaffungsjahr;
        anschaffungspreis = vermoegenswert.anschaffungspreis;
    }
}
