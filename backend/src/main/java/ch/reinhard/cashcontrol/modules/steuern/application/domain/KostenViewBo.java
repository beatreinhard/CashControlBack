package ch.reinhard.cashcontrol.modules.steuern.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class KostenViewBo {
    private String id;
    private String ausgabeId;
    private Integer jahr;
    private KostenArtBo art;
    private String empfaenger;
    private String zahlender;
    private BigDecimal betrag;
    private String bemerkung;
    private String personName;
    private String personVorname;
}
