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
public class ErbschaftBo {
    private String id;
    private Integer jahr;
    private ErbschaftArtBo art;
    private BigDecimal betrag;
    private LocalDate datum;
    private String geber;
    private String verwandtschaftsverhaeltnis;
    private String gegenstand;
    private String bemerkung;
}
