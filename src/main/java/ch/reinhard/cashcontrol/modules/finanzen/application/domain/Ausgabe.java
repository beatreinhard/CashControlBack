package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ausgabe", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ausgabe {
    @NotNull
    @Id
    private String id;

    @Version
    private Long version;

    @NotNull
    private LocalDate datum;

    @NotNull
    private String empfaenger;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AusgabeKategorie kategorie;

    @NotNull
    private String text;

    @NotNull
    BigDecimal betrag;

    public void update(Ausgabe ausgabe) {
        this.datum = ausgabe.datum;
        this.empfaenger = ausgabe.empfaenger;
        this.kategorie = ausgabe.kategorie;
        this.text = ausgabe.text;
        this.betrag = ausgabe.betrag;
    }

}
