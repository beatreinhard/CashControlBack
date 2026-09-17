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
public class BerufBo {
    private String id;
    private Integer jahr;
    private String arbeitnehmer;
    private String arbeitgeber;
    private String arbeitsort;
    private Integer beschaeftigungsgrad;
    private Integer arbeitstage;
    private Integer fahrtkilometerProTag;
    private String grundAutobenutzung;
    private BigDecimal ansatzAuswaertigeVerpflegung;
    private String bemerkung;
}
