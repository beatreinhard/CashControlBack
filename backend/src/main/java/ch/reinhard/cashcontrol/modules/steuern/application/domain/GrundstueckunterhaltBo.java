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
public class GrundstueckunterhaltBo {
    private String id;
    private Integer jahr;
    private LocalDate rgDatum;
    private String ausfuehrendeFirma;
    private String arbeitsArt;
    private BigDecimal betragNetto;
    private BigDecimal anteilAndereKosten;
    private BigDecimal anteilUnterhalt;
}
