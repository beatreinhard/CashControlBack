package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.BerufBo;

import java.util.List;
import java.util.Objects;

public class BerufPersistenceMapper {

    public static BerufBo toBerufBo(BerufEntity source) {
        if (source == null) {
            return null;
        }
        return new BerufBo(
                source.getId(),
                source.getJahr(),
                source.getArbeitnehmer(),
                source.getArbeitgeber(),
                source.getArbeitsort(),
                source.getBeschaeftigungsgrad(),
                source.getArbeitstage(),
                source.getFahrtkilometerProTag(),
                source.getGrundAutobenutzung(),
                source.getAnsatzAuswaertigeVerpflegung(),
                source.getBemerkung());
    }

    public static BerufEntity toBerufEntity(BerufBo source) {
        return new BerufEntity(
                source.getId(),
                source.getJahr(),
                source.getArbeitnehmer(),
                source.getArbeitgeber(),
                source.getArbeitsort(),
                source.getBeschaeftigungsgrad(),
                source.getArbeitstage(),
                source.getFahrtkilometerProTag(),
                source.getGrundAutobenutzung(),
                source.getAnsatzAuswaertigeVerpflegung(),
                source.getBemerkung());
    }

    public static List<BerufBo> toBerufBoList(List<BerufEntity> entityList) {
        return entityList.stream().map(BerufPersistenceMapper::toBerufBo).filter(Objects::nonNull).toList();
    }
}
