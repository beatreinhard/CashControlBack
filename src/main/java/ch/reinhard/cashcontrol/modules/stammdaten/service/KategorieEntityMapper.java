package ch.reinhard.cashcontrol.modules.stammdaten.service;

import ch.reinhard.cashcontrol.modules.stammdaten.domain.KategorieEntity;
import ch.reinhard.cashcontrol.modules.stammdaten.service.api.KategorieDto;

import java.util.List;

public class KategorieEntityMapper {

    public static KategorieEntity toKategorieEntity(KategorieDto source) {
        return new KategorieEntity(
                source.id(),
                source.version(),
                source.bezeichnung()
        );
    }

    public static KategorieDto toKategorieDto(KategorieEntity source) {
        return new KategorieDto(
                source.getId(),
                source.getVersion(),
                source.getBezeichnung()
        );
    }

    public static List<KategorieDto> toKategorieDtoList(List<KategorieEntity> kategorieEntityList) {
        return kategorieEntityList.stream().map(KategorieEntityMapper::toKategorieDto).toList();
    }
}
