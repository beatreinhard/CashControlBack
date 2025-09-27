package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;

import java.util.List;

public class VergabungPersistenceMapper {

    public static VergabungBo toVergabungBo(VergabungEntity source) {
        return new VergabungBo(
                source.getId(),
                source.getVersion(),
                source.getJahr(),
                source.getZahlungsDatum(),
                source.getEmpfaenger(),
                source.getBetrag());
    }

    public static VergabungEntity toVergabung(VergabungBo source) {
        return new VergabungEntity(
                source.id(),
                source.version(),
                source.jahr(),
                source.zahlungsDatum(),
                source.empfaenger(),
                source.betrag());
    }

    public static List<VergabungBo> toVergabungBoList(List<VergabungEntity> entityList) {
        return entityList.stream().map(VergabungPersistenceMapper::toVergabungBo).toList();
    }
}
