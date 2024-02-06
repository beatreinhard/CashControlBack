package ch.reinhard.cashcontrol.modules.steuern.application.domain;

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
    String id;

    @Version
    Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    Integer jahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    KostenArt art;

    @NotNull
    String empfaenger;

    @NotNull
    String bezahler;

    @NotNull
    BigDecimal betrag;

    String bemerkung;
}
