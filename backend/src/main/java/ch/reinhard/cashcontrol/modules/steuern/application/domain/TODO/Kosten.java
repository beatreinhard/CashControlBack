package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "kosten", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class Kosten {
    @Id
    private String id;

    private String ausgabeId;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    private KostenArt art;

    @NotNull
    private String empfaenger;

    @NotNull
    private String zahlender;

    @NotNull
    private BigDecimal betrag;

    private String bemerkung;

    public void update(Kosten kosten) {
        jahr = kosten.jahr;
        art = kosten.art;
        empfaenger = kosten.empfaenger;
        zahlender = kosten.zahlender;
        betrag = kosten.betrag;
        bemerkung = kosten.bemerkung;
    }
}
