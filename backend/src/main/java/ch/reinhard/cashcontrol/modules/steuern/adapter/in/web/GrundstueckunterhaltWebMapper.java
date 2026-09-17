package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.GrundstueckunterhaltBo;
import ch.reinhard.cashcontrol.openapi.model.GrundstueckunterhaltDto;

import java.util.List;

public class GrundstueckunterhaltWebMapper {

    public static GrundstueckunterhaltBo toGrundstueckunterhaltBo(GrundstueckunterhaltDto source) {
        return new GrundstueckunterhaltBo(
                source.getId(),
                source.getJahr(),
                source.getRgDatum(),
                source.getAusfuehrendeFirma(),
                source.getArbeitsArt(),
                source.getBetragNetto(),
                source.getAnteilAndereKosten(),
                source.getAnteilUnterhalt());
    }

    public static GrundstueckunterhaltDto toGrundstueckunterhaltDto(GrundstueckunterhaltBo source) {
        var grundstueckunterhaltDto = new GrundstueckunterhaltDto(
                source.getJahr(),
                source.getRgDatum(),
                source.getAusfuehrendeFirma(),
                source.getArbeitsArt(),
                source.getBetragNetto(),
                source.getAnteilAndereKosten(),
                source.getAnteilUnterhalt());
        grundstueckunterhaltDto.setId(source.getId());
        return grundstueckunterhaltDto;
    }

    public static List<GrundstueckunterhaltDto> toGrundstueckunterhaltDtoList(List<GrundstueckunterhaltBo> boList) {
        return boList.stream().map(GrundstueckunterhaltWebMapper::toGrundstueckunterhaltDto).toList();
    }
}
