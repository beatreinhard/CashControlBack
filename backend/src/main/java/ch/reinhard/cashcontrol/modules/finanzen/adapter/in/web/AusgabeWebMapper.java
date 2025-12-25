package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.openapi.model.AusgabeDto;
import ch.reinhard.cashcontrol.openapi.model.AusgabeKategorieDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class AusgabeWebMapper {

    public static AusgabeBo toAusgabeBo(AusgabeDto source) {
        return new AusgabeBo(
                source.getId(),
                source.getDatum(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorie.class),
                source.getText(),
                source.getBetrag());
    }

    public static AusgabeDto toAusgabeDto(AusgabeBo source) {
        AusgabeDto ausgabeDto = new AusgabeDto(
                source.getDatum(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorieDto.class),
                source.getText(),
                source.getBetrag());

        ausgabeDto.setId(source.getId());
        return ausgabeDto;
    }

    public static List<AusgabeDto> toAusgabeDtoList(List<AusgabeBo> ausgabeBoList) {
        return ausgabeBoList.stream().map(AusgabeWebMapper::toAusgabeDto).toList();
    }

}
