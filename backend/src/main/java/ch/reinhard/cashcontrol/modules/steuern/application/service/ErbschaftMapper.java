package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftArtDto;
import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Erbschaft;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftArt;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

class ErbschaftMapper {

    public static Erbschaft toErbschaft(ErbschaftDto source) {
        return new Erbschaft(
                source.id(),
                source.version(),
                source.jahr(),
                convert(source.art(), ErbschaftArt.class),
                source.betrag(),
                source.datum(),
                source.geber(),
                source.verwandtschaftsverhaeltnis(),
                source.gegenstand(),
                source.bemerkung());
    }

    public static ErbschaftDto toErbschaftDto(Erbschaft source) {
        return new ErbschaftDto(
                source.getId(),
                source.getVersion(),
                source.getJahr(),
                convert(source.getArt(), ErbschaftArtDto.class),
                source.getBetrag(),
                source.getDatum(),
                source.getGeber(),
                source.getVerwandtschaftsverhaeltnis(),
                source.getGegenstand(),
                source.getBemerkung());
    }

    public static List<ErbschaftDto> toErbschaftDtoList(List<Erbschaft> entityList) {
        return entityList.stream().map(ErbschaftMapper::toErbschaftDto).toList();
    }
}
