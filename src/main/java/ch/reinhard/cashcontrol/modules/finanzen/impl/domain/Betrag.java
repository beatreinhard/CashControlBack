package ch.reinhard.cashcontrol.modules.finanzen.impl.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Betrag {
    BigDecimal value;
}
