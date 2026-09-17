package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.BerufBo;
import ch.reinhard.cashcontrol.openapi.model.BerufDto;

import java.util.List;

public class BerufWebMapper {

    public static BerufBo toBerufBo(BerufDto source) {
        return new BerufBo(
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

    public static BerufDto toBerufDto(BerufBo source) {
        var berufDto = new BerufDto(
                source.getJahr(),
                source.getArbeitnehmer(),
                source.getArbeitgeber(),
                source.getArbeitsort(),
                source.getBeschaeftigungsgrad(),
                source.getArbeitstage(),
                source.getAnsatzAuswaertigeVerpflegung());
        berufDto.setId(source.getId());
        berufDto.setFahrtkilometerProTag(source.getFahrtkilometerProTag());
        berufDto.setGrundAutobenutzung(source.getGrundAutobenutzung());
        berufDto.setBemerkung(source.getBemerkung());
        return berufDto;
    }

    public static List<BerufDto> toBerufDtoList(List<BerufBo> boList) {
        return boList.stream().map(BerufWebMapper::toBerufDto).toList();
    }
}
