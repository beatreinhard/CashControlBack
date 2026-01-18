package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

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
    private String id;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ErbschaftArt art;

    @NotNull
    private BigDecimal betrag;

    @NotNull
    private LocalDate datum;

    @NotNull
    private String geber;

    @NotNull
    private String verwandtschaftsverhaeltnis;

    @NotNull
    private String gegenstand;

    private String bemerkung;

    public void update(Erbschaft erbschaft) {
        jahr = erbschaft.jahr;
        art = erbschaft.art;
        betrag = erbschaft.betrag;
        datum = erbschaft.datum;
        geber = erbschaft.geber;
        verwandtschaftsverhaeltnis = erbschaft.verwandtschaftsverhaeltnis;
        gegenstand = erbschaft.gegenstand;
        bemerkung = erbschaft.bemerkung;
    }
}
