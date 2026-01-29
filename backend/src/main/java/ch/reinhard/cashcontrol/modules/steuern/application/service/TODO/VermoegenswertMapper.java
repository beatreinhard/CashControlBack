package ch.reinhard.cashcontrol.modules.steuern.application.service.TODO;

import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Vermoegenswert;

import java.util.List;

public class VermoegenswertMapper {

    public static Vermoegenswert toVermoegenswert(VermoegenswertDto source) {
        return new Vermoegenswert(
                source.id(),
                source.jahr(),
                source.bezeichnung(),
                source.anschaffungsjahr(),
                source.anschaffungspreis());
    }

    public static VermoegenswertDto toVermoegenswertDto(Vermoegenswert source) {
        return new VermoegenswertDto(
                source.getId(),
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
