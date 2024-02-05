package ch.reinhard.cashcontrol.modules.finanzen.impl.domain;


import jakarta.persistence.Embedded;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Zahlung {
    String id;
    LocalDate datum;
    Empfaenger empfaenger;
    Kategorie kategorie;

    @Embedded
    Betrag betrag;
}
