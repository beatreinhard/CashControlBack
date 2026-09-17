package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VermoegenswertBo;
import ch.reinhard.cashcontrol.openapi.model.VermoegenswertDto;

import java.util.List;

public class VermoegenswertWebMapper {

    public static VermoegenswertBo toVermoegenswertBo(VermoegenswertDto source) {
        return new VermoegenswertBo(
                source.getId(),
                source.getJahr(),
                source.getBezeichnung(),
                source.getAnschaffungsjahr(),
                source.getAnschaffungspreis());
    }

    public static VermoegenswertDto toVermoegenswertDto(VermoegenswertBo source) {
        var vermoegenswertDto = new VermoegenswertDto(
                source.getJahr(),
                source.getBezeichnung(),
                source.getAnschaffungsjahr(),
                source.getAnschaffungspreis());
        vermoegenswertDto.setId(source.getId());
        return vermoegenswertDto;
    }

    public static List<VermoegenswertDto> toVermoegenswertDtoList(List<VermoegenswertBo> boList) {
        return boList.stream().map(VermoegenswertWebMapper::toVermoegenswertDto).toList();
    }
}
