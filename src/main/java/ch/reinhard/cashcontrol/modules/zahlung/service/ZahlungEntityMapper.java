package ch.reinhard.cashcontrol.modules.zahlung.service;


import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.ZahlungEntity;
import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.ZahlungEntityDetails;
import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api.ZahlungDto;

import java.util.ArrayList;
import java.util.List;

public class ZahlungEntityMapper {

    public static ZahlungEntity toZahlungEntity(ZahlungDto source) {
        return new ZahlungEntity(
                source.id(),
                source.version(),
                source.datum(),
                source.empfaenger(),
                source.kategorieId(),
                source.text(),
                source.betrag()
        );
    }

    public static ZahlungEntityDetails toZahlungEntityDetails(ZahlungDetailsDto source) {
        return new ZahlungEntityDetails(
                source.datum(),
                source.empfaenger(),
                source.kategorieId(),
                source.text(),
                source.betrag()
        );
    }

    public static ZahlungDto toZahlungDto(ZahlungEntity source) {
        return new ZahlungDto(
                source.getId(),
                source.getVersion(),
                source.getDatum(),
                source.getEmpfaenger(),
                source.getKategorieId(),
                source.getText(),
                source.getBetrag()
        );
    }

    public static List<ZahlungDto> toZahlungDtoList(List<ZahlungEntity> zahlungEntityList) {
        return zahlungEntityList.stream().map(ZahlungEntityMapper::toZahlungDto).toList();
    }

    public static List<ZahlungDto> toZahlungDtoList(Iterable<ZahlungEntity> zahlungEntityIterable) {
        var zahlungEntityList = new ArrayList<ZahlungEntity>();
        zahlungEntityIterable.forEach(zahlungEntityList::add);
        return toZahlungDtoList(zahlungEntityList);
    }
}
