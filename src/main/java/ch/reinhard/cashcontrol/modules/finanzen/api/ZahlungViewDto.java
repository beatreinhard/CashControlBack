package ch.reinhard.cashcontrol.modules.finanzen.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ZahlungView")
public record ZahlungViewDto(
            String id,
            Long version,
            LocalDate datum,
            String empfaenger,
            String kategorieId,
            String kategorieBezeichnung,
            String text,
            BigDecimal betrag
){}

