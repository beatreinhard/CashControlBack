package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "ausgabe_view", schema = "cashcontrol")
@Getter
public class AusgabeView {

    @Id
    @NotNull
    private String id;

    @NotNull
    private LocalDate datum;

    private String zahlender;

    @NotNull
    private String empfaenger;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AusgabeKategorie kategorie;

    private String text;

    @NotNull
    private BigDecimal betrag;

    // aus Person (LEFT JOIN → können null sein)
    private String personName;
    private String personVorname;
}
