package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.openapi.model.VergabungDto;

import java.util.List;

public class VergabungWebMapper {

    public static VergabungBo toVergabungBo(VergabungDto source) {
        // AusgabeId wird hier nicht gemappt, da es im DTO nicht vorhanden ist und nicht vom Controller her kommen kann.
        // Es wird über den Event gesetzt, beim erstellen einer Ausgabe.
        return new VergabungBo(
                source.getId(),
                null,
                source.getVersion(),
                source.getJahr(),
                source.getZahlungsDatum(),
                source.getEmpfaenger(),
                source.getBetrag());
    }

    public static VergabungDto toVergabungDto(VergabungBo source) {
        VergabungDto vergabungDto =  new VergabungDto(
                source.getJahr(),
                source.getZahlungsDatum(),
                source.getEmpfaenger(),
                source.getBetrag());
        vergabungDto.setId(source.getId());
        vergabungDto.setVersion(source.getVersion());
        return vergabungDto;
    }

    public static List<VergabungDto> toVergabungDtoList(List<VergabungBo> boList) {
        return boList.stream().map(VergabungWebMapper::toVergabungDto).toList();
    }
}
