package ch.reinhard.cashcontrol.modules.finanzen.impl.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "zahlung", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZahlungEntity {
    @Id
    String id;

    @Version
    Long version;

    LocalDate datum;

    String empfaenger;

    String kategorieId;

    String text;

    BigDecimal betrag;

    public ZahlungEntity(String id, ZahlungEntityDetails details) {
        this.id = id;
        this.datum = details.datum;
        this.empfaenger = details.empfaenger;
        this.kategorieId = details.kategorieId;
        this.text = details.text;
        this.betrag = details.betrag;
    }

    public ZahlungEntity update(ZahlungEntityDetails details) {
        this.datum = details.datum;
        this.empfaenger = details.empfaenger;
        this.kategorieId = details.kategorieId;
        this.text = details.text;
        this.betrag = details.betrag;
        return this;
    }

    public ZahlungEntityDetails getDetails() {
        return new ZahlungEntityDetails(
                this.datum,
                this.empfaenger,
                this.kategorieId,
                this.text,
                this.betrag
        );
    }
}
