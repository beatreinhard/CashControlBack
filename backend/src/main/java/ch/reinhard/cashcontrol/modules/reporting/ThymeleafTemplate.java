package ch.reinhard.cashcontrol.modules.reporting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class ThymeleafTemplate {

    private final SpringTemplateEngine pdfTemplateEngine;

    public String parseThymeleafTemplate() {
        Context context = new Context();
        context.setVariable("to", "Baeldung");

        return pdfTemplateEngine.process("reporting/steuerrechnungs_daten", context);
    }
}
