package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class AusgabePersistenceMapper {

    private AusgabePersistenceMapper() {
    }

    public static AusgabeEntity toAusgabe(AusgabeBo source) {
        return new AusgabeEntity(
                source.id(),
                source.version(),
                source.datum(),
                source.empfaenger(),
                convert(source.kategorie(), AusgabeKategorie.class),
                source.text(),
                source.betrag());
    }

    public static AusgabeBo toAusgabeBo(AusgabeEntity source) {
        return new AusgabeBo(
                source.getId(),
                source.getVersion(),
                source.getDatum(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorie.class),
                source.getText(),
                source.getBetrag());
    }

    public static List<AusgabeBo> toAusgabeBoList(List<AusgabeEntity> ausgabeEntityList) {
        return ausgabeEntityList.stream().map(AusgabePersistenceMapper::toAusgabeBo).toList();
    }


}
