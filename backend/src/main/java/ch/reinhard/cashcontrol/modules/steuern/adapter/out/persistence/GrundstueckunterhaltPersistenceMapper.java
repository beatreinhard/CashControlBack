package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.GrundstueckunterhaltBo;

import java.util.List;
import java.util.Objects;

public class GrundstueckunterhaltPersistenceMapper {

    public static GrundstueckunterhaltBo toGrundstueckunterhaltBo(GrundstueckunterhaltEntity source) {
        if (source == null) {
            return null;
        }
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

    public static GrundstueckunterhaltEntity toGrundstueckunterhaltEntity(GrundstueckunterhaltBo source) {
        return new GrundstueckunterhaltEntity(
                source.getId(),
                source.getJahr(),
                source.getRgDatum(),
                source.getAusfuehrendeFirma(),
                source.getArbeitsArt(),
                source.getBetragNetto(),
                source.getAnteilAndereKosten(),
                source.getAnteilUnterhalt());
    }

    public static List<GrundstueckunterhaltBo> toGrundstueckunterhaltBoList(
            List<GrundstueckunterhaltEntity> entityList) {
        return entityList.stream()
                .map(GrundstueckunterhaltPersistenceMapper::toGrundstueckunterhaltBo)
                .filter(Objects::nonNull)
                .toList();
    }
}
