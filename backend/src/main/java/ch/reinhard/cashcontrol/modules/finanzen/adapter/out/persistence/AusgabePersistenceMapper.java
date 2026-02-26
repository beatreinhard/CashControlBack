package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import lombok.experimental.UtilityClass;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

@UtilityClass
public class AusgabePersistenceMapper {

    public static AusgabeEntity toAusgabe(AusgabeBo source) {
        return new AusgabeEntity(
                source.getId(),
                source.getDatum(),
                source.getZahlender(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorie.class),
                source.getText(),
                source.getBetrag());
    }

    public static AusgabeBo toAusgabeBo(AusgabeEntity source) {
        return new AusgabeBo(
                source.getId(),
                source.getDatum(),
                source.getZahlender(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorie.class),
                source.getText(),
                source.getBetrag());
    }

    public static List<AusgabeBo> toAusgabeBoList(List<AusgabeEntity> ausgabeEntityList) {
        return ausgabeEntityList.stream().map(AusgabePersistenceMapper::toAusgabeBo).toList();
    }
}
