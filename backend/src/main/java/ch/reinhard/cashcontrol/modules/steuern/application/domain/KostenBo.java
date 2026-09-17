package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class KostenBo {
    private String id;
    private String ausgabeId;
    private Integer jahr;
    private KostenArtBo art;
    private String empfaenger;
    private String zahlender;
    private BigDecimal betrag;
    private String bemerkung;
}
