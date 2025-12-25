package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.BerufDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Beruf;

import java.util.List;

class BerufMapper {

    public static Beruf toBeruf(BerufDto source) {
        return new Beruf(
                source.id(),
                source.jahr(),
                source.arbeitnehmer(),
                source.arbeitgeber(),
                source.arbeitsort(),
                source.beschaeftigungsgrad(),
                source.arbeitstage(),
                source.fahrtkilometerProTag(),
                source.grundAutobenutzung(),
                source.ansatzAuswaertigeVerpflegung(),
                source.bemerkung());
    }

    public static BerufDto toBerufDto(Beruf source) {
        return new BerufDto(
                source.getId(),
                source.getJahr(),
                source.getArbeitnehmer(),
                source.getArbeitgeber(),
                source.getArbeitsort(),
                source.getBeschaeftigungsgrad(),
                source.getArbeitstage(),
                source.getFahrtkilometerProTag(),
                source.getGrundAutobenutzung(),
                source.getAnsatzAuswaertigeVerpflegung(),
                source.getBemerkung());
    }

    public static List<BerufDto> toBerufDtoList(List<Beruf> entityList) {
        return entityList.stream().map(BerufMapper::toBerufDto).toList();
    }
}
