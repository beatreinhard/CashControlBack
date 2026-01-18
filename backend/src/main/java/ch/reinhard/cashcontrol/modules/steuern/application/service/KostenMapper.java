package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.KostenArtDto;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.Kosten;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenArt;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

class KostenMapper {

    public static Kosten toKosten(KostenDto source) {
        return new Kosten(
                source.id(),
                source.jahr(),
                convert(source.art(), KostenArt.class),
                source.empfaenger(),
                source.bezahler(),
                source.betrag(),
                source.bemerkung());
    }

    public static KostenDto toKostenDto(Kosten source) {
        return new KostenDto(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), KostenArtDto.class),
                source.getEmpfaenger(),
                source.getBezahler(),
                source.getBetrag(),
                source.getBemerkung());
    }

    public static List<KostenDto> toKostenDtoList(List<Kosten> entityList) {
        return entityList.stream().map(KostenMapper::toKostenDto).toList();
    }
}
