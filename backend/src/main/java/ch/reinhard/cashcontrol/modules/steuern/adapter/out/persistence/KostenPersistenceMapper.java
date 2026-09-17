package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenViewBo;

import java.util.List;
import java.util.Objects;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class KostenPersistenceMapper {

    public static KostenBo toKostenBo(KostenEntity source) {
        if (source == null) {
            return null;
        }
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

    public static KostenEntity toKostenEntity(KostenBo source) {
        return new KostenEntity(
                source.getId(),
                source.getAusgabeId(),
                source.getJahr(),
                convert(source.getArt(), KostenArtEntity.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag(),
                source.getBemerkung());
    }

    public static List<KostenBo> toKostenBoList(List<KostenEntity> entityList) {
        return entityList.stream().map(KostenPersistenceMapper::toKostenBo).filter(Objects::nonNull).toList();
    }

    public static KostenViewBo toKostenViewBo(KostenView source) {
        return new KostenViewBo(
                source.getId(),
                source.getAusgabeId(),
                source.getJahr(),
                convert(source.getArt(), KostenArtBo.class),
                source.getEmpfaenger(),
                source.getZahlender(),
                source.getBetrag(),
                source.getBemerkung(),
                source.getPersonName(),
                source.getPersonVorname());
    }

    public static List<KostenViewBo> toKostenViewBoList(List<KostenView> kostenViewList) {
        return kostenViewList.stream().map(KostenPersistenceMapper::toKostenViewBo).toList();
    }
}
