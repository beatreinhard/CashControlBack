package ch.reinhard.cashcontrol.modules.zahlung.domain;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Betrag {
    BigDecimal value;
}
