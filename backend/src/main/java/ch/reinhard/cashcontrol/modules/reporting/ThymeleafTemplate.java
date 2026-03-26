package ch.reinhard.cashcontrol.modules.reporting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class ThymeleafTemplate {

    private final SpringTemplateEngine pdfTemplateEngine;

    public String parseThymeleafTemplate() {

        PdfVariables pdfVariables = PdfVariables.builder().
                name("Reinhard").
                vorname("Beat").
                build();

        Context context = new Context();
        context.setVariable("data", pdfVariables);

        return pdfTemplateEngine.process("reporting/steuerrechnungs_daten", context);
    }
}
