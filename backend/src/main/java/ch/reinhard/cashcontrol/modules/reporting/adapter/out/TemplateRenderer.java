package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class TemplateRenderer {

    private final static String TEMPLATE_NAME = "steuerrechnungs_daten";
    private final SpringTemplateEngine templateEngine;

    public String render(Context context) {
        return templateEngine.process(TEMPLATE_NAME, context);
    }
}
