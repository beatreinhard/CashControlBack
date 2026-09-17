package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VermoegenswertBo;

import java.util.List;
import java.util.Objects;

public class VermoegenswertPersistenceMapper {

    public static VermoegenswertBo toVermoegenswertBo(VermoegenswertEntity source) {
        if (source == null) {
            return null;
        }
        return new VermoegenswertBo(
                source.getId(),
                source.getJahr(),
                source.getBezeichnung(),
                source.getAnschaffungsjahr(),
                source.getAnschaffungspreis());
    }

    public static VermoegenswertEntity toVermoegenswertEntity(VermoegenswertBo source) {
        return new VermoegenswertEntity(
                source.getId(),
                source.getJahr(),
                source.getBezeichnung(),
                source.getAnschaffungsjahr(),
                source.getAnschaffungspreis());
    }

    public static List<VermoegenswertBo> toVermoegenswertBoList(List<VermoegenswertEntity> entityList) {
        return entityList.stream()
                .map(VermoegenswertPersistenceMapper::toVermoegenswertBo)
                .filter(Objects::nonNull)
                .toList();
    }
}
