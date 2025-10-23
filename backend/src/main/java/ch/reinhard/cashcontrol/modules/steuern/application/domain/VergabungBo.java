package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VergabungBo {
    private String id;
    private String ausgabeId;
    private Long version;
    private Integer jahr;
    private LocalDate zahlungsDatum;
    private String empfaenger;
    private BigDecimal betrag;
}
