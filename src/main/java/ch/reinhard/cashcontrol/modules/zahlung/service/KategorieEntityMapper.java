package ch.reinhard.cashcontrol.modules.zahlung.service;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.KategorieEntity;
import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api.KategorieDto;

public class KategorieEntityMapper {

    public static KategorieEntity toKategorieEntity(KategorieDto source) {
        return new KategorieEntity(
                source.id(),
                source.bezeichnung()
        );
    }

    public static KategorieDto toKategorieDto(KategorieEntity source) {
        return new KategorieDto(
                source.getId(),
                source.getBezeichnung()
        );
    }
}
