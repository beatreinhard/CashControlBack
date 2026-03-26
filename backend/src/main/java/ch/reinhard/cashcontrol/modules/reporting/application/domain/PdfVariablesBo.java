package ch.reinhard.cashcontrol.modules.reporting.application.domain;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PdfVariablesBo {
    private String name;
    private String vorname;
    private List<PersonBo> persons;

}
