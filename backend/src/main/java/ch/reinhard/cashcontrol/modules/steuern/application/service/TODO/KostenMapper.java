package ch.reinhard.cashcontrol.modules.steuern.application.service.TODO;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.Kosten;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenArt;
import ch.reinhard.cashcontrol.openapi.model.KostenArtDto;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

class KostenMapper {

    public static Kosten toKosten(KostenDto source) {
        return new Kosten(
                source.getId(),
                source.getAusgabeId(),
                source.getJahr(),
                convert(source.getArt(), KostenArt.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag(),
                source.getBemerkung());
    }

    public static KostenDto toKostenDto(Kosten source) {
        var kosten =  new KostenDto(
                source.getJahr(),
                convert(source.getArt(), KostenArtDto.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag());
        kosten.setId(source.getId());
        kosten.setBemerkung(source.getBemerkung());
        kosten.setAusgabeId(source.getAusgabeId());
        return kosten;
    }

    public static List<KostenDto> toKostenDtoList(List<Kosten> entityList) {
        return entityList.stream().map(KostenMapper::toKostenDto).toList();
    }
}
