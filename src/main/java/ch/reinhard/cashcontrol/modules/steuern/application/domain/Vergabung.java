package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vergabung", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
@Getter
@Setter
public class Vergabung {
    @Id
    private String id;

    @Version
    private Long version;

    @NotNull
    @Digits(integer = 4, fraction = 0, message = "Das Jahr muss eine 4-stellige Zahl sein.")
    private Integer jahr;

    @NotNull
    private LocalDate zahlungsDatum;

    @NotNull
    private String empfaenger;

    @NotNull
    private BigDecimal betrag;

    public void update(Vergabung vergabung) {
        jahr = vergabung.jahr;
        zahlungsDatum = vergabung.zahlungsDatum;
        empfaenger = vergabung.empfaenger;
        betrag = vergabung.betrag;
    }
}
