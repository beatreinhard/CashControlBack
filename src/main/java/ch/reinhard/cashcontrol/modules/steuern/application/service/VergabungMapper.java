package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.VergabungDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Vergabung;

import java.util.List;

class VergabungMapper {

    public static Vergabung toVergabung(VergabungDto source) {
        return new Vergabung(
                source.id(),
                source.version(),
                source.jahr(),
                source.zahlungsDatum(),
                source.empfaenger(),
                source.betrag());
    }

    public static VergabungDto toVergabungDto(Vergabung source) {
        return new VergabungDto(
                source.getId(),
                source.getVersion(),
                source.getJahr(),
                source.getZahlungsDatum(),
                source.getEmpfaenger(),
                source.getBetrag());
    }

    public static List<VergabungDto> toVergabungDtoList(List<Vergabung> entityList) {
        return entityList.stream().map(VergabungMapper::toVergabungDto).toList();
    }
}
