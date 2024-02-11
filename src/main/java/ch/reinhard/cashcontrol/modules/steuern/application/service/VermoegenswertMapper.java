package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Vermoegenswert;

import java.util.List;

public class VermoegenswertMapper {

    public static Vermoegenswert toVermoegenswert(VermoegenswertDto source) {
        return new Vermoegenswert(
                source.id(),
                source.version(),
                source.jahr(),
                source.bezeichnung(),
                source.anschaffungsjahr(),
                source.anschaffungspreis());
    }

    public static VermoegenswertDto toVermoegenswertDto(Vermoegenswert source) {
        return new VermoegenswertDto(
                source.getId(),
                source.getVersion(),
                source.getJahr(),
                source.getBezeichnung(),
                source.getAnschaffungsjahr(),
                source.getAnschaffungspreis());
    }

    public static List<VermoegenswertDto> toVermoegenswertDtoList(List<Vermoegenswert> entityList) {
        return entityList.stream()
                .map(VermoegenswertMapper::toVermoegenswertDto)
                .toList();
    }
}
