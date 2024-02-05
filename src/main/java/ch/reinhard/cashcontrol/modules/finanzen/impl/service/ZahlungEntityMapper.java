package ch.reinhard.cashcontrol.modules.finanzen.impl.service;


import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungViewDto;
import ch.reinhard.cashcontrol.modules.finanzen.impl.domain.ZahlungEntity;
import ch.reinhard.cashcontrol.modules.finanzen.impl.domain.ZahlungEntityDetails;
import ch.reinhard.cashcontrol.modules.finanzen.impl.domain.ZahlungView;

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

    public static ZahlungViewDto toZahlungViewDto(ZahlungView source) {
        return new ZahlungViewDto(
                source.getId(),
                source.getVersion(),
                source.getDatum(),
                source.getEmpfaenger(),
                source.getKategorieId(),
                source.getKategorie_bezeichnung(),
                source.getText(),
                source.getBetrag()
        );
    }


    public static List<ZahlungDto> toZahlungDtoList(List<ZahlungEntity> zahlungEntityList) {
        return zahlungEntityList.stream().map(ZahlungEntityMapper::toZahlungDto).toList();
    }

    public static List<ZahlungViewDto> toZahlungViewDtoList(Iterable<ZahlungView> zahlungViewIterable) {
        var zahlungViewList = new ArrayList<ZahlungView>();
        zahlungViewIterable.forEach(zahlungViewList::add);
        return zahlungViewList.stream().map(ZahlungEntityMapper::toZahlungViewDto).toList();
    }
}
