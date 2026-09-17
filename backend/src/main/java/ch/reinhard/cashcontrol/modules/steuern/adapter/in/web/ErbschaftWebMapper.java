package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftBo;
import ch.reinhard.cashcontrol.openapi.model.ErbschaftArtDto;
import ch.reinhard.cashcontrol.openapi.model.ErbschaftDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class ErbschaftWebMapper {

    public static ErbschaftBo toErbschaftBo(ErbschaftDto source) {
        return new ErbschaftBo(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), ErbschaftArtBo.class),
                source.getBetrag(),
                source.getDatum(),
                source.getGeber(),
                source.getVerwandtschaftsverhaeltnis(),
                source.getGegenstand(),
                source.getBemerkung());
    }

    public static ErbschaftDto toErbschaftDto(ErbschaftBo source) {
        var erbschaftDto = new ErbschaftDto(
                source.getJahr(),
                convert(source.getArt(), ErbschaftArtDto.class),
                source.getBetrag(),
                source.getDatum(),
                source.getGeber(),
                source.getVerwandtschaftsverhaeltnis(),
                source.getGegenstand());
        erbschaftDto.setId(source.getId());
        erbschaftDto.setBemerkung(source.getBemerkung());
        return erbschaftDto;
    }

    public static List<ErbschaftDto> toErbschaftDtoList(List<ErbschaftBo> boList) {
        return boList.stream().map(ErbschaftWebMapper::toErbschaftDto).toList();
    }
}
