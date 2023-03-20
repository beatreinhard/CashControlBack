package ch.reinhard.cashcontrol.config;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.controller.ZahlungController;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "CASHCONTROL API",
                contact = @Contact(
                        email = "cash@control.ch",
                        name = "Team CASHCONTROL",
                        url = "https://control.ch/CASHCONTROL"
                )
        ),
        externalDocs = @ExternalDocumentation(url = "https://control.ch/CASHCONTROL"),
        security = {@SecurityRequirement(name = "OIDC_Enduser")}
)
@Configuration
public class OpenApiConfiguration {

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan(ZahlungController.class.getPackageName())
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
