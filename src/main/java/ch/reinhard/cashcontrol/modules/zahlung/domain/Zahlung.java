package ch.reinhard.cashcontrol.modules.zahlung.domain;


import lombok.Data;

import java.time.LocalDate;

@Data
public class Zahlung {
    String id;
    LocalDate datum;
    Empfaenger empfaenger;
    Kategorie kategorie;
    Betrag betrag;
}
