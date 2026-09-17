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
public class VermoegenswertBo {
    private String id;
    private Integer jahr;
    private String bezeichnung;
    private Integer anschaffungsjahr;
    private BigDecimal anschaffungspreis;
}
