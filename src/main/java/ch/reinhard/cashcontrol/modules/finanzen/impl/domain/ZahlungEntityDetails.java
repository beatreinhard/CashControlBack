package ch.reinhard.cashcontrol.modules.finanzen.impl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ZahlungEntityDetails {
    LocalDate datum;
    String empfaenger;
    String kategorieId;
    String text;
    BigDecimal betrag;
}
