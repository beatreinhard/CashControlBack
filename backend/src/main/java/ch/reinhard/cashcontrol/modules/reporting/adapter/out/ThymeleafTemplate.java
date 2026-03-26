package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.PdfVariablesBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ThymeleafTemplate {

    private final SpringTemplateEngine pdfTemplateEngine;

    public String parseThymeleafTemplate() {

        PersonBo personBo1 = new PersonBo();
        personBo1.setName("Reinhard");
        personBo1.setVorname("Beat");
        personBo1.setAhvnummer("111.222.333.44");
        PersonBo personBo2 = new PersonBo();
        personBo2.setName("Reinhard");
        personBo2.setVorname("Denise");
        personBo2.setAhvnummer("333.666.111.99");
        PersonBo personBo3 = new PersonBo();
        personBo3.setName("Reinhard");
        personBo3.setVorname("Luc");
        personBo3.setAhvnummer("noch keine");

        PdfVariablesBo pdfVariablesBo = PdfVariablesBo.builder().
                name("Reinhard").
                vorname("Beat").
                persons(List.of(personBo1, personBo2, personBo3)).
                build();

        Context context = new Context();
        context.setVariable("data", pdfVariablesBo);

        return pdfTemplateEngine.process("reporting/steuerrechnungs_daten", context);
    }
}
