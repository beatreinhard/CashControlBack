package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class AusgabeWebMapper {

    public static AusgabeBo toAusgabeBo(AusgabeDto source) {
        return new AusgabeBo(
                source.id(),
                source.version(),
                source.datum(),
                source.empfaenger(),
                convert(source.kategorie(), AusgabeKategorie.class),
                source.text(),
                source.betrag());
    }

    public static AusgabeDto toAusgabeDto(AusgabeBo source) {
        return new AusgabeDto(
                source.id(),
                source.version(),
                source.datum(),
                source.empfaenger(),
                convert(source.kategorie(), AusgabeKategorieDto.class),
                source.text(),
                source.betrag());
    }


    public static List<AusgabeDto> toAusgabeDtoList(List<AusgabeBo> ausgabeBoList) {
        return ausgabeBoList.stream().map(AusgabeWebMapper::toAusgabeDto).toList();
    }

}
