package ch.reinhard.cashcontrol.modules.steuern.impl.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "erbschaft", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class Erbschaft {

    @Id
    String id;

    @Version
    Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    Integer jahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    ErbschaftArt art;

    @NotNull
    BigDecimal betrag;

    @NotNull
    LocalDate datum;

    @NotNull
    String geber;

    @NotNull
    String verwandtschaftsverhaeltnis;

    @NotNull
    String gegenstand;

    String bemerkung;
}
