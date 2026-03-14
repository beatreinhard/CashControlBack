package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.ausgabe;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeViewBo;
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

    public static AusgabeViewBo toAusgabeViewBo(AusgabeView source) {
        return new AusgabeViewBo(
                source.getId(),
                source.getDatum(),
                source.getZahlender(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorie.class),
                source.getText(),
                source.getBetrag(),
                source.getPersonName(),
                source.getPersonVorname());
    }

    public static List<AusgabeViewBo> toAusgabeViewBoList(List<AusgabeView> ausgabeViewList) {
        return ausgabeViewList.stream().map(AusgabePersistenceMapper::toAusgabeViewBo).toList();
    }
}
