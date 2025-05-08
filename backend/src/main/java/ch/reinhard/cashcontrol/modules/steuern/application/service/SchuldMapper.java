package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.api.SchuldArtDto;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldDto;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Schuld;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldArt;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

class SchuldMapper {

    public static Schuld toSchuld(SchuldDto source) {
        return new Schuld(
                source.id(),
                source.version(),
                source.jahr(),
                convert(source.art(), SchuldArt.class),
                source.glaeubiger(),
                source.betrag(),
                source.zinsen());
    }

    public static SchuldDto toSchuldDto(Schuld source) {
        return new SchuldDto(
                source.getId(),
                source.getVersion(),
                source.getJahr(),
                convert(source.getArt(), SchuldArtDto.class),
                source.getGlaeubiger(),
                source.getBetrag(),
                source.getZinsen());
    }

    public static List<SchuldDto> toSchuldDtoList(List<Schuld> entityList) {
        return entityList.stream().map(SchuldMapper::toSchuldDto).toList();
    }
}
