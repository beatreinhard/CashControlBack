package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.openapi.model.AusgabeKategorieDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class AusgabeWebMapper {

    // TODO: remove when old controller is deleted and openapi is fully implemented
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


    public static AusgabeBo toAusgabeBo(ch.reinhard.cashcontrol.openapi.model.AusgabeDto source) {
        return new AusgabeBo(
                source.getId(),
                source.getVersion(),
                source.getDatum(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorie.class),
                source.getText(),
                source.getBetrag());
    }

    // TODO: implement the other way round for openapi when needed
    // TODO: remove when old controller is deleted and openapi is fully implemented
    public static ch.reinhard.cashcontrol.openapi.model.AusgabeDto toAusgabeDto(AusgabeBo source) {
        ch.reinhard.cashcontrol.openapi.model.AusgabeDto ausgabeDto = new ch.reinhard.cashcontrol.openapi.model.AusgabeDto(
                source.datum(),
                source.empfaenger(),
                convert(source.kategorie(), AusgabeKategorieDto.class),
                source.text(),
                source.betrag());

        ausgabeDto.setId(source.id());
        ausgabeDto.setVersion(source.version());
        return ausgabeDto;
    }

    // TODO: implement the other way round for openapi when needed
    public static List<ch.reinhard.cashcontrol.openapi.model.AusgabeDto> toAusgabeDtoList(List<AusgabeBo> ausgabeBoList) {
        return ausgabeBoList.stream().map(AusgabeWebMapper::toAusgabeDto).toList();
    }

}
