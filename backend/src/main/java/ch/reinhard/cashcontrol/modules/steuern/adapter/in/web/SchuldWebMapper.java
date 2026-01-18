package ch.reinhard.cashcontrol.modules.steuern.adapter.in.web;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.openapi.model.SchuldArtDto;
import ch.reinhard.cashcontrol.openapi.model.SchuldDto;

import java.util.List;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class SchuldWebMapper {
    public static SchuldBo toSchuldBo(SchuldDto source) {
        return new SchuldBo(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), SchuldArtBo.class),
                source.getGlaeubiger(),
                source.getBetrag(),
                source.getZinsen());
    }

    public static SchuldDto toSchuldDto(SchuldBo source) {
        var schuld =  new SchuldDto(
                source.getJahr(),
                convert(source.getArt(), SchuldArtDto.class),
                source.getGlaeubiger(),
                source.getBetrag());
        schuld.setId(source.getId());
        schuld.setZinsen(source.getZinsen());
        return schuld;
    }

    public static List<SchuldDto> toSchuldDtoList(List<SchuldBo> boList) {
        return boList.stream().map(SchuldWebMapper::toSchuldDto).toList();
    }
}
