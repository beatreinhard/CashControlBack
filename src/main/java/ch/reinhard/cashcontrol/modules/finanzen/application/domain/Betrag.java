package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Betrag {
    BigDecimal value;
}
