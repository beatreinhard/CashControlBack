package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.api.AusgabeKategorieDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.AusgabeDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungViewDto;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.Ausgabe;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeKategorie;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.ZahlungEntityDetails;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.ZahlungView;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldArt;

import java.util.ArrayList;
import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class AusgabeMapper {

    public static Ausgabe toAusgabe(AusgabeDto source) {
        return new Ausgabe(
                source.id(),
                source.version(),
                source.datum(),
                source.empfaenger(),
                convert(source.kategorie(), AusgabeKategorie.class),
                source.text(),
                source.betrag());
    }

    public static AusgabeDto toAusgabeDto(Ausgabe source) {
        return new AusgabeDto(
                source.getId(),
                source.getVersion(),
                source.getDatum(),
                source.getEmpfaenger(),
                convert(source.getKategorie(), AusgabeKategorieDto.class),
                source.getText(),
                source.getBetrag());
    }

    public static List<AusgabeDto> toAusgabeDtoList(List<Ausgabe> ausgabeList) {
        return ausgabeList.stream().map(AusgabeMapper::toAusgabeDto).toList();
    }

}
