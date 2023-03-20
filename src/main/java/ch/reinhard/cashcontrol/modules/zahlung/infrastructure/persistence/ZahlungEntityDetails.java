package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ZahlungEntityDetails {
    LocalDate datum;
    String empfaenger;
    String kategorieId;
    Long betrag;
}
