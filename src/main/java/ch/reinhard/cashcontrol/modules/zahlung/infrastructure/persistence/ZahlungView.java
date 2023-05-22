package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "zahlungView", schema = "cashcontrol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZahlungView {
    @Id
    String id;

    Long version;

    LocalDate datum;

    String empfaenger;

    String kategorieId;

    String text;

    String kategorie_bezeichnung;
}
