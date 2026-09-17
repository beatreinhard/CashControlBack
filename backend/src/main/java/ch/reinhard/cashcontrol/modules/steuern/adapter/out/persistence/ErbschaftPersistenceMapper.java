package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftBo;

import java.util.List;
import java.util.Objects;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class ErbschaftPersistenceMapper {

    public static ErbschaftBo toErbschaftBo(ErbschaftEntity source) {
        if (source == null) {
            return null;
        }
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

    public static ErbschaftEntity toErbschaftEntity(ErbschaftBo source) {
        return new ErbschaftEntity(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), ErbschaftArtEntity.class),
                source.getBetrag(),
                source.getDatum(),
                source.getGeber(),
                source.getVerwandtschaftsverhaeltnis(),
                source.getGegenstand(),
                source.getBemerkung());
    }

    public static List<ErbschaftBo> toErbschaftBoList(List<ErbschaftEntity> entityList) {
        return entityList.stream().map(ErbschaftPersistenceMapper::toErbschaftBo).filter(Objects::nonNull).toList();
    }
}
