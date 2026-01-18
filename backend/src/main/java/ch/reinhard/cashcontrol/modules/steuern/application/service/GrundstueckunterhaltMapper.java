package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.Grundstueckunterhalt;

import java.util.List;

public class GrundstueckunterhaltMapper {

    public static Grundstueckunterhalt toGrundstueckunterhalt(GrundstueckunterhaltDto source) {
        return new Grundstueckunterhalt(
                source.id(),
                source.jahr(),
                source.rgDatum(),
                source.ausfuehrendeFirma(),
                source.arbeitsArt(),
                source.betragNetto(),
                source.anteilAndereKosten(),
                source.anteilUnterhalt());
    }

    public static GrundstueckunterhaltDto toGrundstueckunterhaltDto(Grundstueckunterhalt source) {
        return new GrundstueckunterhaltDto(
                source.getId(),
                source.getJahr(),
                source.getRgDatum(),
                source.getAusfuehrendeFirma(),
                source.getArbeitsArt(),
                source.getBetragNetto(),
                source.getAnteilAndereKosten(),
                source.getAnteilUnterhalt());
    }

    public static List<GrundstueckunterhaltDto> toGrundstueckunterhaltDtoList(List<Grundstueckunterhalt> entityList) {
        return entityList.stream()
                .map(GrundstueckunterhaltMapper::toGrundstueckunterhaltDto)
                .toList();
    }
}
