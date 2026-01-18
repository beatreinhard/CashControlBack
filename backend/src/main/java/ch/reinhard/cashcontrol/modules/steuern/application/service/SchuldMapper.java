package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.SchuldArtEntity;
import ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.SchuldEntity;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldArtDto;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

class SchuldMapper {

    public static SchuldEntity toSchuld(SchuldDto source) {
        return new SchuldEntity(
                source.id(),
                source.jahr(),
                convert(source.art(), SchuldArtEntity.class),
                source.glaeubiger(),
                source.betrag(),
                source.zinsen());
    }

    public static SchuldDto toSchuldDto(SchuldEntity source) {
        return new SchuldDto(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), SchuldArtDto.class),
                source.getGlaeubiger(),
                source.getBetrag(),
                source.getZinsen());
    }

    public static List<SchuldDto> toSchuldDtoList(List<SchuldEntity> entityList) {
        return entityList.stream().map(SchuldMapper::toSchuldDto).toList();
    }
}
