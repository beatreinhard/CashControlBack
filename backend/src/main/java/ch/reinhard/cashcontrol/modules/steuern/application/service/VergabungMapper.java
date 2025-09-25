package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.Vergabung;
import ch.reinhard.cashcontrol.openapi.model.VergabungDto;

import java.util.List;

// TODO: zwei Mapper machen (fuer API und fuer DB)
public class VergabungMapper {

    public static Vergabung toVergabung(VergabungDto source) {
        return new Vergabung(
                source.getId(),
                source.getVersion(),
                source.getJahr(),
                source.getZahlungsDatum(),
                source.getEmpfaenger(),
                source.getBetrag());
    }

    public static VergabungDto toVergabungDto(Vergabung source) {
        VergabungDto vergabungDto =  new VergabungDto(
                source.getJahr(),
                source.getZahlungsDatum(),
                source.getEmpfaenger(),
                source.getBetrag());
        vergabungDto.setId(source.getId());
        vergabungDto.setVersion(source.getVersion());
        return vergabungDto;
    }

    public static List<VergabungDto> toVergabungDtoList(List<Vergabung> entityList) {
        return entityList.stream().map(VergabungMapper::toVergabungDto).toList();
    }
}
