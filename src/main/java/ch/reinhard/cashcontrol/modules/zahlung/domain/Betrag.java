package ch.reinhard.cashcontrol.modules.zahlung.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Betrag {
    BigDecimal value;
}
