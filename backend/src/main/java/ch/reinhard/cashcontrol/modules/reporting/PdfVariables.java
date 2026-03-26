package ch.reinhard.cashcontrol.modules.reporting;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PdfVariables {
    private String name;
    private String vorname;

}
