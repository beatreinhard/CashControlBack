package ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "kosten_view", schema = "cashcontrol")
@Getter
public class KostenView {
    @NotNull
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

    // aus Person (LEFT JOIN → können null sein)
    private String personName;
    private String personVorname;
}
