package ch.reinhard.cashcontrol.modules.steuern.application.service.TODO;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenArt;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenEntity;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenView;
import ch.reinhard.cashcontrol.openapi.model.KostenArtDto;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
import ch.reinhard.cashcontrol.openapi.model.KostenViewDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

class KostenMapper {

    public static KostenEntity toKosten(KostenDto source) {
        return new KostenEntity(
                source.getId(),
                source.getAusgabeId(),
                source.getJahr(),
                convert(source.getArt(), KostenArt.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag(),
                source.getBemerkung());
    }

    public static KostenDto toKostenDto(KostenEntity source) {
        if (source == null) {
            return null;
        }
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

    public static List<KostenDto> toKostenDtoList(List<KostenEntity> entityList) {
        return entityList.stream().map(KostenMapper::toKostenDto).toList();
    }

    public static KostenViewDto toKostenViewDto(KostenView source) {
        KostenViewDto kostenViewDto = new KostenViewDto(
                source.getJahr(),
                convert(source.getArt(), KostenArtDto.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag()
        );
        kostenViewDto.setId(source.getId());
        kostenViewDto.setBemerkung(source.getBemerkung());
        kostenViewDto.setAusgabeId(source.getAusgabeId());
        kostenViewDto.setZahlenderName(source.getPersonName());
        kostenViewDto.setZahlenderVorname(source.getPersonVorname());
        return kostenViewDto;
    }

    public static List<KostenViewDto> toKostenViewDtoList(List<KostenView> kostenViewList) {
        return kostenViewList.stream().map(KostenMapper::toKostenViewDto).toList();
    }
}
