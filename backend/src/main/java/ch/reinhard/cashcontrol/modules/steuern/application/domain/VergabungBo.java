package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class VergabungBo {
    private String id;
    private Long version;
    private Integer jahr;
    private LocalDate zahlungsDatum;
    private String empfaenger;
    private BigDecimal betrag;
}
