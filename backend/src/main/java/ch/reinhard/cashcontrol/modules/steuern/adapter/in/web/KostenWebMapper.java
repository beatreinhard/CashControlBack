package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenViewBo;
import ch.reinhard.cashcontrol.openapi.model.KostenArtDto;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
import ch.reinhard.cashcontrol.openapi.model.KostenViewDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class KostenWebMapper {

    public static KostenBo toKostenBo(KostenDto source) {
        return new KostenBo(
                source.getId(),
                source.getAusgabeId(),
                source.getJahr(),
                convert(source.getArt(), KostenArtBo.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag(),
                source.getBemerkung());
    }

    public static KostenDto toKostenDto(KostenBo source) {
        var kostenDto = new KostenDto(
                source.getJahr(),
                convert(source.getArt(), KostenArtDto.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag());
        kostenDto.setId(source.getId());
        kostenDto.setAusgabeId(source.getAusgabeId());
        kostenDto.setBemerkung(source.getBemerkung());
        return kostenDto;
    }

    public static List<KostenDto> toKostenDtoList(List<KostenBo> boList) {
        return boList.stream().map(KostenWebMapper::toKostenDto).toList();
    }

    public static KostenViewDto toKostenViewDto(KostenViewBo source) {
        var kostenViewDto = new KostenViewDto(
                source.getJahr(),
                convert(source.getArt(), KostenArtDto.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag());
        kostenViewDto.setId(source.getId());
        kostenViewDto.setAusgabeId(source.getAusgabeId());
        kostenViewDto.setBemerkung(source.getBemerkung());
        kostenViewDto.setZahlenderName(source.getPersonName());
        kostenViewDto.setZahlenderVorname(source.getPersonVorname());
        return kostenViewDto;
    }

    public static List<KostenViewDto> toKostenViewDtoList(List<KostenViewBo> boList) {
        return boList.stream().map(KostenWebMapper::toKostenViewDto).toList();
    }
}
