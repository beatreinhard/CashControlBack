package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

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
public class AusgabeEntity {
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

    public void update(AusgabeEntity ausgabeEntity) {
        this.datum = ausgabeEntity.datum;
        this.empfaenger = ausgabeEntity.empfaenger;
        this.kategorie = ausgabeEntity.kategorie;
        this.text = ausgabeEntity.text;
        this.betrag = ausgabeEntity.betrag;
    }

}
